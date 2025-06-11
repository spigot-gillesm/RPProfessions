package com.gilles_m.rp_professions.event;

import com.gilles_m.rp_professions.object.RecipePattern;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class InteractWithPatternEvent extends Event {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private final RecipePattern recipePattern;

	private final Player player;

	public InteractWithPatternEvent(Player player, RecipePattern recipePattern) {
		this.player = player;
		this.recipePattern = recipePattern;
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
