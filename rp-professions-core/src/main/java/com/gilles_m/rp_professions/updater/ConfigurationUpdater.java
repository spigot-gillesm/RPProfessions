package com.gilles_m.rp_professions.updater;

import com.github.spigot_gillesm.file_utils.FileUtils;
import com.github.spigot_gillesm.format_lib.Formatter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigurationUpdater {

	private final int currentVersion;

	protected final List<VersionUpdater> versionUpdaters;

	protected ConfigurationUpdater(final int currentVersion, VersionUpdater... versionUpdaters) {
		this.currentVersion = currentVersion;
		this.versionUpdaters = new ArrayList<>(Arrays.asList(versionUpdaters));
	}

	public void update(@NotNull File file) throws IOException, InvalidConfigurationException {
		final YamlConfiguration configuration = FileUtils.getConfiguration(file);
		final int configurationVersion = configuration.getInt("VERSION", 1);

		for(int i = configurationVersion; i < currentVersion; i++) {
			for(final VersionUpdater versionUpdater : versionUpdaters) {
				if(versionUpdater.version == i) {
					Formatter.warning(String.format("Configuration file %s is outdated! Updating...", file.getName()));

					configuration.set("VERSION", versionUpdater.version + 1);
					FileUtils.saveConfiguration(file, configuration);
					versionUpdater.update(configuration);

					FileUtils.saveConfiguration(file, configuration);
					Formatter.info("Done!");
				}
			}
		}
	}

	protected abstract static class VersionUpdater {

		protected final int version;

		protected VersionUpdater(final int version) {
			this.version = version;
		}

		protected abstract void update(YamlConfiguration configuration) throws IOException, InvalidConfigurationException;

	}

}
