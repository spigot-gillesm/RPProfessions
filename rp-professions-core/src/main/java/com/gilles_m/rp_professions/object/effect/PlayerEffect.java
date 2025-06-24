package com.gilles_m.rp_professions.object.effect;

import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.event.PlayerEffectClearEvent;
import com.gilles_m.rp_professions.event.PlayerEffectGetEvent;
import com.gilles_m.rp_professions.loader.PlayerEffectLoader;
import com.gilles_m.rp_professions.manager.PlayerEffectManager;
import com.gilles_m.rp_professions.object.Effect;
import com.github.spigot_gillesm.player_lib.DataManager;
import com.github.spigot_gillesm.player_lib.PlayerData;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.math.BigDecimal;

public class PlayerEffect {

	@Getter
	private final Effect effect;

	@Getter
	private final Player player;

	private final double duration;

	private long startedAt;

	private BukkitTask task;

	PlayerEffect(Effect effect, Player player, final double duration) {
		this.effect = effect;
		this.player = player;
		this.duration = duration;
	}

	/**
	 * @return the remaining time in seconds
	 */
	private double remainingTime() {
		return BigDecimal.valueOf(duration - (System.currentTimeMillis()  - startedAt) / 1000.0).doubleValue();
	}

	public void run() {
		final var event = new PlayerEffectGetEvent(this, player);

		if(event.isCancelled()) {
			//Do not run the effects if the event is cancelled
			return;
		}
		this.startedAt = System.currentTimeMillis();
		this.task = Bukkit.getServer()
				.getScheduler()
				.runTaskLater(RPProfessions.getInstance(),
						this::clear,
						(long) duration * 20);
		PlayerEffectManager.getInstance().register(this);
	}

	public void clear() {
		if(task != null) {
			task.cancel();
		}
		PlayerEffectManager.getInstance().delete(this);

		if(player == null || !player.isOnline()) {
			return;
		}
		effect.clear(player);
		Bukkit.getServer().getPluginManager().callEvent(new PlayerEffectClearEvent(this, player));
	}

	public void saveState() throws IOException, InvalidConfigurationException {
		if(player == null || !effect.isSavedOnQuit()) {
			return;
		}
		final PlayerData playerData = DataManager.getData(player);
		final YamlConfiguration file = playerData.getConfiguration();

		final ConfigurationSection effectsSection = file.isConfigurationSection(PlayerEffectLoader.EFFECTS_SECTION_NAME) ?
				file.getConfigurationSection(PlayerEffectLoader.EFFECTS_SECTION_NAME) :
				file.createSection(PlayerEffectLoader.EFFECTS_SECTION_NAME);

		effectsSection.set(effect.getId(), remainingTime());
		file.save(playerData.getConfigurationFile());
	}

}
