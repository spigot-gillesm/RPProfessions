package com.gilles_m.rp_professions.menu.recipe_menu;

import com.gilles_m.rp_professions.PluginUtils;
import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.object.crafting_recipe.AbstractCraftingRecipe;
import com.gilles_m.rp_professions.object.crafting_recipe.crafting_recipe_impl.BrewingStandCraftingRecipe;
import com.gilles_m.rp_professions.object.crafting_recipe.crafting_recipe_impl.CraftingRecipe;
import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.gui_lib.ListingMenu;
import com.github.spigot_gillesm.gui_lib.SimpleButton;
import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.player_lib.DataManager;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * This menu lists all the crafting recipes of a specific category. This is the second accessible menu when using the
 * recipes command.
 */
public class CategoryRecipesMenu extends ListingMenu {

	private final List<AbstractCraftingRecipe> craftingRecipes = new ArrayList<>();

	private final CategoryRecipesMenu instance;

	protected CategoryRecipesMenu(SimpleMenu parentMenu) {
		super(parentMenu);

		this.instance = this;
		setTitle(RPProfessions.getInstance().getStringHolder().recipesMenuTitle);
		setCancelActions(true);
	}

	void addCraftEntity(AbstractCraftingRecipe craftingRecipe) {
		craftingRecipes.add(craftingRecipe);
	}

	@Override
	protected List<SimpleButton> generateButtons() {
		final List<SimpleButton> buttons = new ArrayList<>();

		craftingRecipes.forEach(recipe -> {
			if(recipe instanceof BrewingStandCraftingRecipe brewingRecipe) {
				buttons.add(generateBrewRecipeButton(brewingRecipe));
			} else {
				buttons.add(generateRecipeButton((CraftingRecipe) recipe));
			}
		});

		return buttons;
	}

	private SimpleButton generateBrewRecipeButton(BrewingStandCraftingRecipe brewingStandRecipe) {
		//Clone the item to not modify the actual amount
		final ItemStack icon = brewingStandRecipe.getResult().clone();
		icon.setAmount(1);

		return new SimpleButton(icon) {

			@Override
			public boolean action(Player player, final ClickType click, ItemStack draggedItem) {
				//Open custom menu if the server is running on Paper Spigot
				if(RPProfessions.getInstance().isPaper()) {
					final var menu = new AlternativeBrewingRecipeMenu(instance);
					menu.setRecipe(brewingStandRecipe);
					menu.display(player);
				} else {
					final BrewerInventory inventory = PluginUtils.makeBrewingInventory(
							player,
							brewingStandRecipe.getReagent(),
							brewingStandRecipe.getFuel(),
							brewingStandRecipe.getReceptacle()
					);
					player.openInventory(inventory);

					if(brewingStandRecipe.isFuelConsumed()) {
						Formatter.tell(player, RPProfessions.getInstance().getStringHolder().brewingStandFuelRequiredMessage);
					}

					DataManager.getData(player).setRawValue("BREWING_MENU", this, false);
				}

				return false;
			}

		};
	}

	private SimpleButton generateRecipeButton(CraftingRecipe craftingRecipe) {
		final ItemStack icon = craftingRecipe.getResult().clone();
		icon.setAmount(1);

		return new SimpleButton(icon) {

			@Override
			public boolean action(Player player, final ClickType click, ItemStack draggedItem) {
				final var menu = new RecipeMenu(instance, craftingRecipe);
				menu.display(viewer);

				return false;
			}

		};
	}

	/**
	 * Since opening a custom brewing recipe is not possible using Paper Spigot, a custom menu is used instead.
	 */
	private static class AlternativeBrewingRecipeMenu extends SimpleMenu {

		@Setter
		private BrewingStandCraftingRecipe recipe;

		protected AlternativeBrewingRecipeMenu(SimpleMenu parentMenu) {
			super(parentMenu);

			setSize(6*9);
			setTitle(RPProfessions.getInstance().getStringHolder().brewingRecipeMenuTitle);
			setCancelActions(true);
		}

		private ItemStack getTooltipButton() {
			return SimpleItem.newBuilder()
					.material(Material.PAPER)
					.displayName("&9Info")
					.lore("", "&7Station&f: &5Brewing Stand",
							"&7Advanced&f: &9Yes",
							"&7Must be fueled&f: &9" + (recipe.isDynamic() ? "Yes" : "No"),
							"&7Required level&f: &9" + recipe.getMetaData().getRequiredLevel(),
							"&7Level cap&f: &9" + recipe.getMetaData().getLevelCap(),
							"&7Level gain: &9" + recipe.getMetaData().getLevelGain())
					.build()
					.make()
					.getItemStack();
		}

		@Override
		protected ItemStack getSlotItem(final int slot) {
			if(slot == 11) {
				return recipe.getFuel();
			}
			else if(slot == 13) {
				return recipe.getReagent();
			}
			else if(slot == 25) {
				return recipe.getResult();
			}
			else if(slot == 28) {
				return getTooltipButton();
			}
			else if(slot == 30) {
				return recipe.getReceptacle();
			}
			else if(slot == 31) {
				return recipe.getReceptacle();
			}
			else if(slot == 32) {
				return recipe.getReceptacle();
			}
			else {
				//Fill the rest
				return SimpleButton.DummyButton.FILLING_BUTTON.getIcon();
			}
		}

	}

}
