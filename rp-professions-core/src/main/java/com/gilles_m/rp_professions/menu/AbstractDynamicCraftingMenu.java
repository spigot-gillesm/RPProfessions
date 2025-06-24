package com.gilles_m.rp_professions.menu;

import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.event.CompleteCraftEvent;
import com.gilles_m.rp_professions.manager.PlayerManager;
import com.gilles_m.rp_professions.object.crafting_recipe.AbstractCraftingRecipe;
import com.gilles_m.rp_professions.object.crafting_recipe.RecipeRunnable;
import com.github.spigot_gillesm.gui_lib.SimpleButton;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Represent a dynamic crafting menu.
 */
@Setter
public abstract class AbstractDynamicCraftingMenu extends AbstractCraftingMenu {

	protected RecipeRunnable<? extends AbstractCraftingRecipe, ? extends AbstractDynamicCraftingMenu> recipeRunnable;

	protected final SimpleButton failedButton = new SimpleButton.DummyButton(
			SimpleItem.newBuilder()
					.material(Material.RED_STAINED_GLASS_PANE)
					.displayName(RPProfessions.getInstance().getStringHolder().craftFailedButton)
					.build()
					.make()
					.getItemStack()
	);

	protected boolean finished = false;

	protected boolean failed = false;

	protected ItemStack result;

	protected AbstractDynamicCraftingMenu() {
		registerButton(failedButton);
	}

	protected <T extends RecipeRunnable<?, ?>> T getRecipeRunnable(Class<T> clazz) {
		return clazz.cast(recipeRunnable);
	}

	public void runEvent(Player player) {
		PlayerManager.getInstance()
				.getProfession(player)
				.ifPresent(profession -> Bukkit.getServer()
                        .getPluginManager()
                        .callEvent(new CompleteCraftEvent(player, recipeRunnable.getRecipe(), profession, 1))
				);
	}

}
