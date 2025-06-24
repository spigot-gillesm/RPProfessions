package com.gilles_m.rp_professions.event;

import com.gilles_m.rp_professions.object.effect.PlayerEffect;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class PlayerEffectClearEvent extends Event {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private final PlayerEffect playerEffect;

	private final Player player;

	public PlayerEffectClearEvent(PlayerEffect playerEffect, Player player) {
		this.playerEffect = playerEffect;
		this.player = player;
	}

	@Override
	@NotNull
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

}
