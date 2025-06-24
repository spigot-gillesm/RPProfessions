package com.gilles_m.rp_professions.menu.dynamic_crafting_menu_impl;

import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.menu.AbstractDynamicCraftingMenu;
import com.gilles_m.rp_professions.object.crafting_recipe.dynamic_crafting_recipe_impl.DynamicAnvilCraftingRecipe;
import com.github.spigot_gillesm.gui_lib.SimpleButton;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class DynamicAnvilMenu extends AbstractDynamicCraftingMenu {

	private static final int RESULT_SLOT = 4+9;

	private final SimpleButton hitSpotButton;

	private final SimpleButton wrongSpotButton;

	private final SimpleButton progressBarButton;

	public DynamicAnvilMenu() {
		setResultSlot(RESULT_SLOT);
		this.hitSpotButton = new SimpleButton(SimpleItem.newBuilder()
				.material(Material.BLACK_STAINED_GLASS_PANE)
				.displayName("&f")
				.build()
				.make()
				.getItemStack()) {

			@Override
			public boolean action(Player player, final ClickType click, ItemStack draggedItem) {
				getRecipeRunnable().buildUp();
				player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);

				return false;
			}

		};
		this.wrongSpotButton = new SimpleButton(SimpleItem.newBuilder()
				.material(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
				.displayName("&f")
				.build()
				.make()
				.getItemStack()) {

			@Override
			public boolean action(Player player, final ClickType click, ItemStack draggedItem) {
				getRecipeRunnable().fail();
				player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);

				return false;
			}

		};
		this.progressBarButton = new SimpleButton.DummyButton(
				SimpleItem.newBuilder()
						.material(Material.LIME_STAINED_GLASS_PANE)
						.displayName("&f")
						.build()
						.make()
						.getItemStack()
		);
		setSize(6*9);
		registerButtons(hitSpotButton, wrongSpotButton, progressBarButton, SimpleButton.DummyButton.FILLING_BUTTON);
	}

	private DynamicAnvilCraftingRecipe.CraftRunnable getRecipeRunnable() {
		return getRecipeRunnable(DynamicAnvilCraftingRecipe.CraftRunnable.class);
	}

	@Override
	protected String getTitle() {
		return RPProfessions.getInstance().getStringHolder().anvilMenuTitle;
	}

	@Override
	protected ItemStack getSlotItem(final int slot) {
		//If the craft is still running
		if(!finished && !failed) {
			//Set the progress bar
			for (int i = 0; i < getRecipeRunnable().getProportionalScore(); i++) {
				if(slot == i + 5 * 9) {
					return progressBarButton.getIcon();
				}
			}
			//Set the 'hit' and 'miss' buttons
			if(slot == getRecipeRunnable().getNextHitTargetLocation() + 2*9) {
				return hitSpotButton.getIcon();
			} else if(slot >= 2*9){
				return wrongSpotButton.getIcon();
			} else {
				//Fill the rest
				return SimpleButton.DummyButton.FILLING_BUTTON.getIcon();
			}
		}
		//If it's either finished or failed
		else if(slot == RESULT_SLOT) {
			if(finished) {
				return result;
			}
			//Fail button
			return failedButton.getIcon();
		}

		//Fill the rest
		return SimpleButton.DummyButton.FILLING_BUTTON.getIcon();
	}

	@Override
	public void onClose(Player player) {
		final ItemStack resultSlotItem = player.getOpenInventory().getTopInventory().getContents()[RESULT_SLOT];

		//Do not drop "invalid" items
		if(resultSlotItem == null) {
			return;
		}
		if(registeredButtons.stream().anyMatch(button -> button.getIcon().isSimilar(resultSlotItem))) {
			return;
		}
		player.getWorld().dropItemNaturally(player.getLocation(), resultSlotItem);
	}

}
