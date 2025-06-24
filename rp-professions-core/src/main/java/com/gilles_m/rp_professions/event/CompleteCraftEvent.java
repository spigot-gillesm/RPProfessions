package com.gilles_m.rp_professions.event;

import com.gilles_m.rp_professions.object.Profession;
import com.gilles_m.rp_professions.object.crafting_recipe.AbstractCraftingRecipe;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class CompleteCraftEvent extends Event {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private final AbstractCraftingRecipe craftingRecipe;

	private final int craftedAmount;

	private final Player player;

	private final Profession playerProfession;

	public CompleteCraftEvent(Player player, AbstractCraftingRecipe craftingRecipe, Profession playerProfession, final int craftedAmount) {
		this.player = player;
		this.craftingRecipe = craftingRecipe;
		this.craftedAmount = craftedAmount;
		this.playerProfession = playerProfession;
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
