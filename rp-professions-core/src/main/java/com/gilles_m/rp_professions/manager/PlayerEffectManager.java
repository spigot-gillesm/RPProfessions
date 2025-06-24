package com.gilles_m.rp_professions.manager;

import com.gilles_m.rp_professions.object.Effect;
import com.gilles_m.rp_professions.object.effect.PlayerEffect;
import com.github.spigot_gillesm.format_lib.Formatter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PlayerEffectManager {

	private static final PlayerEffectManager INSTANCE = new PlayerEffectManager();

	private final Set<PlayerEffect> registeredEffects = new HashSet<>();

	public void register(@NotNull PlayerEffect playerEffect) {
		registeredEffects.add(playerEffect);
	}

	public void delete(@NotNull PlayerEffect playerEffect) {
		registeredEffects.remove(playerEffect);
	}

	public void delete(@NotNull Player player, @NotNull Effect effect) {
		getPlayerEffects(player).stream()
				.filter(playerEffect -> playerEffect.getEffect().getId().equals(effect.getId()))
				//Avoid concurrent modification
				.collect(Collectors.toSet())
				.forEach(PlayerEffect::clear);
	}

	public Set<PlayerEffect> getPlayerEffects(@NotNull Player player) {
		return registeredEffects.stream()
				.filter(effect -> player.equals(effect.getPlayer()))
				.collect(Collectors.toSet());
	}

	public void clearEffectsOnDeath(@NotNull Player player) {
		getPlayerEffects(player).stream()
				.filter(effect -> !effect.getEffect().isPersistentThroughDeath())
				//To avoid concurrent modification
				.collect(Collectors.toSet())
				.forEach(PlayerEffect::clear);
	}

	public void clearEffectsOnQuit(@NotNull Player player) {
		getPlayerEffects(player)
				.forEach(effect -> {
					try {
						effect.saveState();
					} catch (IOException | InvalidConfigurationException exception) {
						Formatter.error(String.format("Error saving %s player effects:", player.getName()));
						Formatter.error(exception.getMessage());
					}
					effect.clear();
				});
	}

	public static PlayerEffectManager getInstance() {
		return INSTANCE;
	}

}
