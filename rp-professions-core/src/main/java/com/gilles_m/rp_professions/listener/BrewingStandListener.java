package com.gilles_m.rp_professions.listener;

import com.gilles_m.rp_professions.manager.CraftingRecipeManager;
import com.github.spigot_gillesm.player_lib.DataManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

/**
 * This listener allows players to use the brewing stands with any items by re-coding the brewing stand behaviors.
 */
public class BrewingStandListener implements Listener {

	@EventHandler
	private void onInventoryClick(InventoryClickEvent event) {
		if(!(event.getWhoClicked() instanceof Player player)) {
			return;
		}
		if(event.getClickedInventory() == null) {
			return;
		}
		if(event.getClickedInventory().getType() != InventoryType.BREWING) {
			return;
		}
		final ClickType click = event.getClick();
		final BrewerInventory inventory = (BrewerInventory) event.getClickedInventory();
		final ItemStack clickedItem = event.getCurrentItem();
		final ItemStack draggedItem = event.getCursor();
		final int clickedSlot = event.getSlot();

		//Check if it's a demo menu
		if(DataManager.getData(player).getRawValue("BREWING_MENU") != null) {
			event.setCancelled(true);
			return;
		}
		//Do nothing if the player has nothing on its cursor/isn't dragging any item
		if(draggedItem == null || draggedItem.getType() == Material.AIR) {
			return;
		}
		if(clickedItem != null) {
			//Left click means swapping items
			if(click == ClickType.LEFT) {
				//Dragged and Clicked are swapped -> params must be swapped as well
				CraftingRecipeManager.getInstance()
						.setBrewerInventoryItems(player, inventory, clickedSlot, clickedItem.clone(), draggedItem.clone());
			}
			//Right click means adding one item instead of the whole stack
			else if(click == ClickType.RIGHT) {
				final ItemStack newDraggedItem = draggedItem.clone();
				newDraggedItem.setAmount(newDraggedItem.getAmount() - 1);

				//If nothing was already present on the slot, set the item to the dragged item with amount = 1
				if(clickedItem.getType() == Material.AIR) {
					final ItemStack newClickedItem = newDraggedItem.clone();
					newClickedItem.setAmount(1);

					CraftingRecipeManager.getInstance()
							.setBrewerInventoryItems(player, inventory, clickedSlot, newDraggedItem, newClickedItem);
				} else if (clickedItem.isSimilar(draggedItem)){
					//Else, decrement dragged item and increment clicked item
					final ItemStack newClickedItem = clickedItem.clone();
					newClickedItem.setAmount(newClickedItem.getAmount() + 1);

					CraftingRecipeManager.getInstance()
							.setBrewerInventoryItems(player, inventory, clickedSlot, newDraggedItem, newClickedItem);
				} else {
					//If the items are not similar, just swap them like a left click
					CraftingRecipeManager.getInstance()
							.setBrewerInventoryItems(player, inventory, clickedSlot, clickedItem.clone(), draggedItem.clone());
				}
			}
		}
	}

	@EventHandler
	private void onInventoryClosed(InventoryCloseEvent event) {
		//Remove the tag when a menu is closed
		DataManager.getData((Player) event.getPlayer()).removeRawValue("BREWING_MENU");
	}
	
}
