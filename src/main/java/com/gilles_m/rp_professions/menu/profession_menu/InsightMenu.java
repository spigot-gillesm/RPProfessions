package com.gilles_m.rp_professions.menu.profession_menu;

import com.gilles_m.rp_professions.PluginUtils;
import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.menu.recipe_menu.RecipeMenu;
import com.gilles_m.rp_professions.object.Profession;
import com.gilles_m.rp_professions.object.crafting_recipe.AbstractCraftingRecipe;
import com.gilles_m.rp_professions.object.crafting_recipe.crafting_recipe_impl.BrewingStandCraftingRecipe;
import com.gilles_m.rp_professions.object.crafting_recipe.crafting_recipe_impl.CraftingRecipe;
import com.github.spigot_gillesm.gui_lib.SimpleButton;
import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * This menu shows an item craftable by a specific profession. This menu is accessible through a profession menu.
 */
public class InsightMenu extends SimpleMenu {

	private SimpleButton exampleCraftButton;

	InsightMenu(SimpleMenu parent, Profession profession) {
		super(parent);

		//Might be replaced by a map
		profession.getExampleItem()
				.ifPresentOrElse(exampleCraft -> {
					final AbstractCraftingRecipe exampleRecipe = exampleCraft.getCraftingRecipe();

					//If the example has no crafting recipe, don't link it to a recipe menu
					if(exampleRecipe == null) {
						this.exampleCraftButton = new SimpleButton.DummyButton(exampleCraft.getItemStack());
					//Check if the recipe is a potion recipe
					} else if(exampleRecipe instanceof BrewingStandCraftingRecipe brewingRecipe) {
                        this.exampleCraftButton = new SimpleButton(exampleCraft.getItemStack()) {

							@Override
							public boolean action(Player player, final ClickType clickType, ItemStack itemStack) {
								player.openInventory(PluginUtils.makeBrewingInventory(player,
										brewingRecipe.getReagent(),
										brewingRecipe.getFuel(),
										brewingRecipe.getReceptacle()));

								return false;
							}

						};
					//Otherwise, regular recipe
					} else {
						this.exampleCraftButton = new SimpleButton(exampleCraft.getItemStack()) {

							@Override
							public boolean action(Player player, final ClickType clickType, ItemStack itemStack) {
								new RecipeMenu(InsightMenu.this, (CraftingRecipe) exampleRecipe).display(player);

								return false;
							}

						};
					}
				}, () -> this.exampleCraftButton = SimpleButton.DummyButton.FILLING_BUTTON);
		setSize(3*9);
		setTitle(RPProfessions.getInstance().getStringHolder().insightMenuTitle);
		registerButtons(exampleCraftButton, SimpleButton.DummyButton.FILLING_BUTTON);
		setCancelActions(true);
	}

	@Override
	protected ItemStack getSlotItem(final int slot) {
		if(slot == 9 + 4) {
			return exampleCraftButton.getIcon();
		}

		return SimpleButton.DummyButton.FILLING_BUTTON.getIcon();
	}

}
