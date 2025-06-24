package com.gilles_m.rp_professions.menu;

import com.gilles_m.rp_professions.event.CompleteCraftEvent;
import com.gilles_m.rp_professions.manager.PlayerManager;
import com.gilles_m.rp_professions.object.crafting_recipe.AbstractCraftingRecipe;
import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import lombok.AccessLevel;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractCraftingMenu extends SimpleMenu {

	@Setter(AccessLevel.PROTECTED)
	protected int resultSlot;

	protected AbstractCraftingMenu() { }

	public void completeCraft(Player player, Inventory inventory, AbstractCraftingRecipe craftingRecipe, final int craftedAmount) {
		final ItemStack result = craftingRecipe.getResult();
		result.setAmount(result.getAmount() * craftedAmount);
		inventory.setItem(resultSlot, result);

		player.getWorld().playSound(player.getLocation(), getCraftSound(), 1, 1);
		//Run the complete craft event
		PlayerManager.getInstance()
				.getProfession(player)
				.ifPresent(profession -> Bukkit.getServer()
                        .getPluginManager()
                        .callEvent(new CompleteCraftEvent(player, craftingRecipe, profession, craftedAmount))
				);
	}

	protected Sound getCraftSound() {
		return Sound.ENTITY_PLAYER_LEVELUP;
	}

	protected abstract String getTitle();

}
