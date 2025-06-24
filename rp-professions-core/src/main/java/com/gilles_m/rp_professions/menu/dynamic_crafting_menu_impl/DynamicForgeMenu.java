package com.gilles_m.rp_professions.menu.dynamic_crafting_menu_impl;

import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.menu.AbstractDynamicCraftingMenu;
import com.gilles_m.rp_professions.object.crafting_recipe.dynamic_crafting_recipe_impl.DynamicForgeCraftingRecipe;
import com.github.spigot_gillesm.gui_lib.SimpleButton;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class DynamicForgeMenu extends AbstractDynamicCraftingMenu {

	private static final int RESULT_SLOT = 4+9;

	private final SimpleButton heatButton;

	private final SimpleButton coolButton;

	private final SimpleButton tooHotButton;

	private final SimpleButton tooColdButton;

	private final SimpleButton perfectTempButton;

	public DynamicForgeMenu() {
		setResultSlot(RESULT_SLOT);
		this.heatButton = new SimpleButton(SimpleItem.newBuilder()
				.material(Material.LAVA_BUCKET)
				.displayName(RPProfessions.getInstance().getStringHolder().forgeHeatUpButton)
				.build()
				.make()
				.getItemStack()) {

			@Override
			public boolean action(Player player, ClickType click, ItemStack draggedItem) {
				if(!finished) {
					getRecipeRunnable().heatUp();
				}

				//Update the menu only if the crafting process isn't finished yet
				return !finished;
			}

		};
		this.coolButton = new SimpleButton(SimpleItem.newBuilder()
				.material(Material.WATER_BUCKET)
				.displayName(RPProfessions.getInstance().getStringHolder().forgeCoolDownButton)
				.build()
				.make()
				.getItemStack()) {

			@Override
			public boolean action(Player player, ClickType click, ItemStack draggedItem) {
				if(!finished) {
					getRecipeRunnable().coolDown();
				}

				//Update the menu only if the crafting process isn't finished yet
				return !finished;
			}

		};
		this.tooHotButton = new SimpleButton.DummyButton(SimpleItem.newBuilder()
				.material(Material.RED_STAINED_GLASS_PANE)
				.displayName("&f")
				.build()
				.make()
				.getItemStack());
		this.tooColdButton = new SimpleButton.DummyButton(SimpleItem.newBuilder()
				.material(Material.LIGHT_BLUE_STAINED_GLASS_PANE)
				.displayName("&f")
				.build()
				.make()
				.getItemStack());
		this.perfectTempButton = new SimpleButton.DummyButton(SimpleItem.newBuilder()
				.material(Material.LIME_STAINED_GLASS_PANE)
				.displayName("&f")
				.build()
				.make()
				.getItemStack()
		);
		setSize(5*9);
		registerButtons(heatButton, coolButton, tooHotButton, tooColdButton, perfectTempButton, SimpleButton.DummyButton.FILLING_BUTTON);
	}

	@Override
	protected String getTitle() {
		return RPProfessions.getInstance().getStringHolder().forgeMenuTitle;
	}

	private DynamicForgeCraftingRecipe.CraftRunnable getRecipeRunnable() {
		return getRecipeRunnable(DynamicForgeCraftingRecipe.CraftRunnable.class);
	}

	@Override
	protected ItemStack getSlotItem(final int slot) {
		//Do not display the heat bar if the process failed
		if(!failed) {
			for(int i = 0; i < (getRecipeRunnable().getHeat()); i++) {
				if(slot == i + 4 * 9) {
					//Red if too hot
					if(getRecipeRunnable().getHeat() > getRecipeRunnable().getRecipe().getRequiredHeat()) {
						return tooHotButton.getIcon();
					//Blue if too cold
					} else if(getRecipeRunnable().getHeat() < getRecipeRunnable().getRecipe().getRequiredHeat()) {
						return tooColdButton.getIcon();
					} else {
						return perfectTempButton.getIcon();
					}
				}
			}
		}
		if(slot == RESULT_SLOT && finished) {
			return result;
		}
		if(slot == RESULT_SLOT && failed) {
			return failedButton.getIcon();
		}
		if(slot == 3*9 + 3 && !finished && !failed) {
			return coolButton.getIcon();
		}
		else if(slot == 3*9 + 5 && !finished && !failed) {
			return heatButton.getIcon();
		}
		else {
			return SimpleButton.DummyButton.FILLING_BUTTON.getIcon();
		}
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
