package com.gilles_m.rp_professions.menu.recipe_menu;

import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.manager.CategoryManager;
import com.gilles_m.rp_professions.manager.CraftingRecipeManager;
import com.gilles_m.rp_professions.manager.PlayerManager;
import com.gilles_m.rp_professions.object.Category;
import com.gilles_m.rp_professions.object.Profession;
import com.gilles_m.rp_professions.object.crafting_recipe.AbstractCraftingRecipe;
import com.github.spigot_gillesm.gui_lib.ListingMenu;
import com.github.spigot_gillesm.gui_lib.SimpleButton;
import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * This menu lists all the available crafting recipe's categories of a player's profession. This is the first accessible
 * menu when using the recipes command.
 */
public class CategoriesMenu extends ListingMenu {

	private final Player player;

	public CategoriesMenu(SimpleMenu parentMenu, Player player) {
		super(parentMenu);

		this.player = player;
		setTitle(RPProfessions.getInstance().getStringHolder().recipesMenuTitle);
		setCancelActions(true);
	}

	public CategoriesMenu(Player player) {
		super();

		this.player = player;
		setTitle(RPProfessions.getInstance().getStringHolder().recipesMenuTitle);
		setCancelActions(true);
	}

	@Override
	protected List<SimpleButton> generateButtons() {
		final Optional<Profession> profession = PlayerManager.getInstance().getProfession(player);

		if(profession.isEmpty()) {
			return Collections.emptyList();
		}
		final Map<Category, CategoryRecipesMenu> menus = new LinkedHashMap<>();

		//Setup category by category
		for(final Category category : CategoryManager.getInstance().getAll()) {
			for(final AbstractCraftingRecipe craft : CraftingRecipeManager.getInstance().getRecipesMatchingCategory(category)) {
				if(!craft.canCraft(player, false)) {
					continue;
				}
				if(menus.containsKey(category)) {
					menus.get(category).addCraftEntity(craft);
				} else {
					final var menu = new CategoryRecipesMenu(this);
					menu.addCraftEntity(craft);
					menus.put(category, menu);
				}
			}
		}

		return menus.entrySet()
				.stream()
				.map(entrySet -> generateCategoryButton(entrySet.getKey(), entrySet.getValue()))
				.toList();
	}

	private SimpleButton generateCategoryButton(Category category, CategoryRecipesMenu menu) {
		return new SimpleButton(category.getIcon()) {
			@Override
			public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
				menu.display(player);

				return false;
			}
		};
	}

}
