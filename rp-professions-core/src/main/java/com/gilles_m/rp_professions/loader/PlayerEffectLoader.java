package com.gilles_m.rp_professions.loader;

import com.gilles_m.rp_professions.manager.EffectManager;
import com.github.spigot_gillesm.player_lib.DataManager;
import com.github.spigot_gillesm.player_lib.PlayerData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class PlayerEffectLoader {

	public static final String EFFECTS_SECTION_NAME = "effects";

	private static final PlayerEffectLoader INSTANCE = new PlayerEffectLoader();

	private PlayerEffectLoader() { }

	public void loadPlayerEffects(@NotNull Player player) throws IOException, InvalidConfigurationException {
		final PlayerData playerData = DataManager.getData(player);
		final YamlConfiguration configuration = playerData.getConfiguration();

		if(!configuration.isConfigurationSection(EFFECTS_SECTION_NAME)) {
			return;
		}
		final ConfigurationSection effectsSection = configuration.getConfigurationSection(EFFECTS_SECTION_NAME);

		for(final String effectId : effectsSection.getKeys(false)) {
			EffectManager.getInstance()
					.get(effectId)
					.ifPresent(effect -> effect.apply(player, effectsSection.getDouble(effectId)));
		}
		//Clear the effects section content
		configuration.set(EFFECTS_SECTION_NAME, null);
		configuration.save(playerData.getConfigurationFile());
	}

	public static PlayerEffectLoader getInstance() {
		return INSTANCE;
	}

}
