package com.gilles_m.rp_professions.menu;

import com.gilles_m.rp_professions.PluginUtils;
import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.manager.CraftingRecipeManager;
import com.gilles_m.rp_professions.menu.recipe_menu.CategoriesMenu;
import com.gilles_m.rp_professions.object.crafting_recipe.crafting_context_impl.AdvancedCraftingContext;
import com.github.spigot_gillesm.gui_lib.SimpleButton;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractCraftingStationMenu extends AbstractCraftingMenu {

	private static final Set<Integer> CRAFTING_SLOTS = Stream.of(3+9, 4+9, 5+9, 12+9, 13+9, 14+9, 21+9, 22+9, 23+9)
			.collect(Collectors.toCollection(HashSet::new));

	public static final int RESULT_SLOT = 25;

	public static final int REAGENTS_SLOT = 49;

	private final SimpleButton craftButton;

	private final SimpleButton showRecipeButton;

	protected AbstractCraftingStationMenu() {
		setResultSlot(RESULT_SLOT);
		final var menuInstance = this;

		this.craftButton = new SimpleButton(getCraftIcon()) {
			@Override
			public boolean action(Player player, final ClickType click, ItemStack draggedItem) {
				final Inventory inventory = player.getOpenInventory().getTopInventory();
				final ItemStack[] content = inventory.getContents();

				final ItemStack[] craftingGridContent = new ItemStack[] {
						content[3+9], content[4+9], content[5+9],
						content[12+9], content[13+9], content[14+9],
						content[21+9], content[22+9], content[23+9]
				};
				//Check if patterns match any item
				CraftingRecipeManager.getInstance()
						.getRecipeMatchingPattern(craftingGridContent)
						.ifPresent(craftingRecipe -> new AdvancedCraftingContext(player, craftingRecipe, menuInstance, inventory, craftingGridContent)
                                .runCraft()
						);

				return false;
			}
		};
		this.showRecipeButton = new SimpleButton(SimpleItem.newBuilder()
				.material(Material.BOOK)
				.displayName(RPProfessions.getInstance().getStringHolder().showRecipeButton)
				.build().make().getItemStack()) {

			@Override
			public boolean action(Player player, final ClickType clickType, ItemStack itemStack) {
				//Open the crafting recipe menu
				new CategoriesMenu(AbstractCraftingStationMenu.this, player).display(player);

				return false;
			}

		};
		setTitle("&8" + getTitle());
		setSize(6*9);
		registerButtons(SimpleButton.DummyButton.FILLING_BUTTON, craftButton, showRecipeButton);
	}

	public void clearGrid(Inventory inventory, final int craftedAmount, final int reagentAmount) {
		if(inventory.getItem(REAGENTS_SLOT) != null && PluginUtils.decreaseOrRemove(inventory.getItem(REAGENTS_SLOT), craftedAmount * reagentAmount)) {
				inventory.setItem(REAGENTS_SLOT, null);
		}
		CRAFTING_SLOTS.forEach(slot -> {
			if(PluginUtils.decreaseOrRemove(inventory.getItem(slot), craftedAmount)) {
				inventory.setItem(slot, null);
			}
		});
	}

	protected abstract ItemStack getCraftIcon();

	@Override
	protected ItemStack getSlotItem(final int slot) {
		if(slot == 19) {
			return craftButton.getIcon();
		} else if(slot == 28) {
			return showRecipeButton.getIcon();
		} else if(slot == RESULT_SLOT || CRAFTING_SLOTS.contains(slot) || slot == REAGENTS_SLOT) {
			return null;
		} else {
			//Fill the rest
			return SimpleButton.DummyButton.FILLING_BUTTON.getIcon();
		}
	}

	@Override
	public void onClose(Player player) {
		final ItemStack[] inventory = player.getOpenInventory().getTopInventory().getContents();

		//Put the remaining items back in the player's inventory
		CRAFTING_SLOTS.stream()
				.filter(slot -> inventory[slot] != null)
				.forEach(slot -> PluginUtils.addToInventoryOrDrop(player, inventory[slot]));

		if(inventory[RESULT_SLOT] != null) {
			PluginUtils.addToInventoryOrDrop(player, inventory[RESULT_SLOT]);
		}
		if(inventory[REAGENTS_SLOT] != null) {
			PluginUtils.addToInventoryOrDrop(player, inventory[REAGENTS_SLOT]);
		}
	}

}
