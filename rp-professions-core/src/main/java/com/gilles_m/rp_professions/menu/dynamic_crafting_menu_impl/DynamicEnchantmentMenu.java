package com.gilles_m.rp_professions.menu.dynamic_crafting_menu_impl;

import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.menu.AbstractDynamicCraftingMenu;
import com.gilles_m.rp_professions.object.crafting_recipe.dynamic_crafting_recipe_impl.DynamicEnchantmentCraftingRecipe;
import com.github.spigot_gillesm.gui_lib.SimpleButton;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class DynamicEnchantmentMenu extends AbstractDynamicCraftingMenu {

	private static final int SELECTED_COLOR_SLOT = 49;

	private static final int RESULT_SLOT = 4+9;

	private SimpleButton currentColorButton = SimpleButton.DummyButton.FILLING_BUTTON;

	@Getter
	private final Map<Integer, SimpleButton> colorButtons = new HashMap<>();

	public DynamicEnchantmentMenu() {
		registerButton(SimpleButton.DummyButton.FILLING_BUTTON);
		setResultSlot(RESULT_SLOT);
		setSize(6*9);
	}

	@Override
	protected String getTitle() {
		return RPProfessions.getInstance().getStringHolder().enchantmentMenuTitle;
	}

	@Override
	public void display(@NotNull Player player) {
		//override display to create the additional buttons. Cannot be done in constructor since
		//the runnable isn't defined yet.
		createButtons();
		super.display(player);
	}

	private void createButtons() {
		//The button showing what color must be selected
		this.currentColorButton = new SimpleButton.DummyButton(SimpleItem.newBuilder()
				.material(getRecipeRunnable().getColorToFind())
				.displayName("&f")
				.localizedName("CANCEL_MENU")
				.build()
				.make()
				.getItemStack());

		//Use colors and put in map
		for(final Map.Entry<Integer, Material> set : getRecipeRunnable().getColors().entrySet()) {
			final int slot = set.getKey();
			final Material color = set.getValue();

			final var button = new SimpleButton(SimpleItem.newBuilder()
					.material(color)
					.displayName("&f")
					.build()
					.make()
					.getItemStack()) {

				@Override
				public boolean action(Player player, final ClickType click, ItemStack draggedItem) {
					getRecipeRunnable().clickedOn(slot);
					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

					return !finished;
				}

			};
			colorButtons.put(slot, button);
			registerButton(button);
		}
	}

	private DynamicEnchantmentCraftingRecipe.CraftRunnable getRecipeRunnable() {
		return getRecipeRunnable(DynamicEnchantmentCraftingRecipe.CraftRunnable.class);
	}

	@Override
	protected ItemStack getSlotItem(final int slot) {
		//If finished -> display the result
		if(slot == RESULT_SLOT && finished) {
			return result;
		}
		//If failed -> display the fail icon
		else if(slot == RESULT_SLOT && failed) {
			return failedButton.getIcon();
		}
		//Display colors if not finished nor failed
		else if(slot == SELECTED_COLOR_SLOT) {
			return currentColorButton.getIcon();
		}
		else if (!(finished || failed)){
			for(final Map.Entry<Integer, SimpleButton> colorButton : colorButtons.entrySet()) {
				if(slot == colorButton.getKey()) {
					return colorButton.getValue().getIcon();
				}
			}
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
