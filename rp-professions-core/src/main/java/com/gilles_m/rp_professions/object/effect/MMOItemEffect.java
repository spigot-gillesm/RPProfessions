package com.gilles_m.rp_professions.object.effect;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gilles_m.rp_professions.manager.DependencyManager;
import com.gilles_m.rp_professions.manager.PlayerEffectManager;
import com.gilles_m.rp_professions.object.Effect;
import com.google.common.base.MoreObjects;
import io.lumine.mythic.lib.api.stat.StatMap;
import io.lumine.mythic.lib.api.stat.modifier.StatModifier;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MMOItemEffect extends Effect {

	@Getter
	@JsonProperty("mmo-id")
	private String mmoId;

	@Setter
	private List<StatModifier> statModifiers = new ArrayList<>();

	@Override
	public void apply(@NotNull Player player) {
		//By default, apply the effect for this effect's duration
		apply(player, this.duration);
	}

	@Override
	public void apply(@NotNull Player player, final double duration) {
		DependencyManager.getInstance()
				.getPlayerMMOStats(player)
				.ifPresent(playerData -> {
					//Check if the player already has this effect registered and removes it if it does
					PlayerEffectManager.getInstance().delete(player, this);

					//Register the new effect for the given player
					new PlayerEffect(this, player, duration).run();
					final StatMap statMap = playerData.getStatMap();

					for(final StatModifier effect : statModifiers) {
						statMap.getInstance(effect.getStat()).addModifier(effect);
					}
				});
	}

	@Override
	public void clear(@NotNull Player player) {
		DependencyManager.getInstance().getPlayerMMOStats(player)
				.ifPresent(playerData -> {
					final StatMap statMap = playerData.getStatMap();

					for(final StatModifier effect : statModifiers) {
						statMap.getInstance(effect.getStat()).remove(effect.getKey());
					}
				});
	}

	public final String toString() {
		return MoreObjects.toStringHelper(this)
				.add("id", id)
				.add("mmoId", mmoId)
				.add("statModifiers", statModifiers.toString())
				.add("duration", duration)
				.add("savedOnQuit", savedOnQuit)
				.add("persistentThroughDeath", persistentThroughDeath)
				.toString();
	}

}
