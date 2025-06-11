package com.gilles_m.rp_professions.updater;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Objects;

public class ProfessionItemUpdater extends ConfigurationUpdater {

	private static final ProfessionItemUpdater INSTANCE = new ProfessionItemUpdater();

	private static final int CURRENT_VERSION = 2;

	private ProfessionItemUpdater() {
		super(CURRENT_VERSION, new V1Updater());
	}

	public static ProfessionItemUpdater getInstance() {
		return INSTANCE;
	}

	/**
	 * Update the file from version 1 to 2.
	 */
	private static class V1Updater extends ConfigurationUpdater.VersionUpdater {

		private static final String OLD_MMOITEM_KEY_1 = "mmo-item";

		private static final String OLD_MMOITEM_KEY_2 = "mi";

		private static final String OLD_MM_KEY_1 = "mm-item";

		private static final String OLD_MM_KEY_2 = "mm";

		private static final String OLD_IA_KEY_1 = "ia-item";

		private static final String OLD_IA_KEY_2 = "ia";

		private static final String OLD_ORAXEN_KEY = "oraxen-item";

		private V1Updater() {
			super(1);
		}

		@Override
		protected void update(YamlConfiguration configuration) {
			configuration.getKeys(false)
					.stream()
					.filter(configuration::isConfigurationSection)
					.map(configuration::getConfigurationSection)
					.filter(Objects::nonNull)
					.forEach(this::updateSection);
		}

		private void updateSection(ConfigurationSection configurationSection) {
			//Replace mmo items import
			if(configurationSection.contains(OLD_MMOITEM_KEY_1)) {
				configurationSection.set("item", "mi:" + configurationSection.getString(OLD_MMOITEM_KEY_1));
				configurationSection.set(OLD_MMOITEM_KEY_1, null);
			}
			if(configurationSection.contains(OLD_MMOITEM_KEY_2)) {
				configurationSection.set("item", "mi:" + configurationSection.getString(OLD_MMOITEM_KEY_2));
				configurationSection.set(OLD_MMOITEM_KEY_2, null);
			}
			//Replace mm items import
			if(configurationSection.contains(OLD_MM_KEY_1)) {
				configurationSection.set("item", "mm:" + configurationSection.getString(OLD_MM_KEY_1));
				configurationSection.set(OLD_MM_KEY_1, null);
			}
			if(configurationSection.contains(OLD_MM_KEY_2)) {
				configurationSection.set("item", "mm:" + configurationSection.getString(OLD_MM_KEY_2));
				configurationSection.set(OLD_MM_KEY_2, null);
			}
			//Replace ia items import
			if(configurationSection.contains(OLD_IA_KEY_1)) {
				configurationSection.set("item", "ia:" + configurationSection.getString(OLD_IA_KEY_1));
				configurationSection.set(OLD_IA_KEY_1, null);
			}
			if(configurationSection.contains(OLD_IA_KEY_2)) {
				configurationSection.set("item", "ia:" + configurationSection.getString(OLD_IA_KEY_2));
				configurationSection.set(OLD_IA_KEY_2, null);
			}
			//Replace Oraxen items import
			if(configurationSection.contains(OLD_ORAXEN_KEY)) {
				configurationSection.set("item", "oraxen:" + configurationSection.getString(OLD_ORAXEN_KEY));
				configurationSection.set(OLD_ORAXEN_KEY, null);
			}
		}

	}

}
