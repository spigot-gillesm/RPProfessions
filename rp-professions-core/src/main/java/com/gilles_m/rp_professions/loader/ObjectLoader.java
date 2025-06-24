package com.gilles_m.rp_professions.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.gilles_m.rp_professions.Identifiable;
import com.gilles_m.rp_professions.manager.ObjectManager;
import com.gilles_m.rp_professions.updater.ConfigurationUpdater;
import com.github.spigot_gillesm.file_utils.FileUtils;
import com.github.spigot_gillesm.format_lib.Formatter;
import lombok.AccessLevel;
import lombok.Setter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

/**
 * @param <T> the loaded objects type
 */
public abstract class ObjectLoader<T extends Identifiable> {

	private static final String ERROR_STRING = "Error loading %s";

	protected final ObjectManager<T> objectManager;

	protected final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

	private final String objectName;

	private final String filePath;

	//The use of an iterable allows this class to be oblivious to how objects are mapped/built
	@Setter(AccessLevel.PROTECTED)
	private LoaderIterable<T, ?> loaderIterable;

	protected ConfigurationUpdater configurationUpdater;

	protected ObjectLoader(@NotNull ObjectManager<T> objectManager, @Nullable String filePath,
						   @Nullable ConfigurationUpdater configurationUpdater, @NotNull String objectName) {

		this.objectManager = objectManager;
		this.configurationUpdater = configurationUpdater;
		this.filePath = filePath;
		this.objectName = objectName;
	}

	protected ObjectLoader(@NotNull ObjectManager<T> objectManager, @Nullable String filePath, @NotNull String objectName) {
		this(objectManager, filePath, null, objectName);
	}

	protected ObjectLoader(@NotNull ObjectManager<T> objectManager, @NotNull String objectName) {
		this(objectManager, null, objectName);
	}

	public void load() {
		objectManager.clear();
		Formatter.info(String.format("Loading %s...", objectName));

		try {
			loadObjects();
		} catch (IOException | InvalidConfigurationException exception) {
			Formatter.error(String.format(ERROR_STRING, objectName));
			Formatter.error(exception.getMessage());
		}
		Formatter.info(String.format("Loaded %d %s", objectManager.size(), objectName));
	}

	protected void loadObjects() throws IOException, InvalidConfigurationException {
		//Loader might load objects from existing objects instead of a file
		if(filePath == null) {
			loadObject(null);
			return;
		}
		final File file = FileUtils.getResource(filePath);

		if(loaderIterable == null) {
			throw new IllegalArgumentException("The object loader iterable must be defined");
		}
		if(file.isDirectory()) {
			for(final File subFile : file.listFiles()) {
				if(!subFile.isDirectory()) {
					loadObject(subFile);
				}
			}
		} else {
			loadObject(file);
		}
	}

	private void loadObject(File file) throws IOException, InvalidConfigurationException {
		//No need to try and load objects from an empty file
		if(file != null && file.length() == 0) {
			return;
		}
		if(configurationUpdater != null && file != null) {
			configurationUpdater.update(file);
		}
		loaderIterable.init(file);

		for(final T object : loaderIterable) {
			if(object != null) {
				objectManager.register(object);
			}
		}
	}

}
