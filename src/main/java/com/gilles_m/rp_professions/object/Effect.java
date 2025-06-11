package com.gilles_m.rp_professions.object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gilles_m.rp_professions.Identifiable;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Effect implements Identifiable {

    protected String id;

	@JsonProperty("duration")
	protected double duration;

	@JsonProperty("save-on-quit")
	protected boolean savedOnQuit;

	@JsonProperty("persistent-through-death")
	protected boolean persistentThroughDeath;

	protected Effect() { }

	public abstract void apply(@NotNull Player player);

	public abstract void apply(@NotNull Player player, double duration);

	public abstract void clear(@NotNull Player player);

}
