package com.gilles_m.rp_professions.object.crafting_recipe;

import com.gilles_m.rp_professions.menu.AbstractDynamicCraftingMenu;
import org.bukkit.entity.Player;

public interface DynamicCraftingRecipe<R extends AbstractCraftingRecipe, M extends AbstractDynamicCraftingMenu> {

	RecipeRunnable<R, M> start(Player player, M dynamicCraftMenu);

}
