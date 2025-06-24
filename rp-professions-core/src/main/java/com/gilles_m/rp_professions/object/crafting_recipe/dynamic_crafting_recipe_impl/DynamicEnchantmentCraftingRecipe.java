package com.gilles_m.rp_professions.object.crafting_recipe.dynamic_crafting_recipe_impl;

import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.menu.dynamic_crafting_menu_impl.DynamicEnchantmentMenu;
import com.gilles_m.rp_professions.object.crafting_recipe.DynamicCraftingRecipe;
import com.gilles_m.rp_professions.object.crafting_recipe.RecipeRunnable;
import com.gilles_m.rp_professions.object.crafting_recipe.crafting_recipe_impl.CraftingRecipe;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.util.*;

@Setter
public class DynamicEnchantmentCraftingRecipe extends CraftingRecipe
		implements DynamicCraftingRecipe<DynamicEnchantmentCraftingRecipe, DynamicEnchantmentMenu> {

	private int requiredAmount = 1;

	private int mistakeCost = 1;

	//In ticks
	private int timeLaps = 20 * 4;

	@Override
	public RecipeRunnable<DynamicEnchantmentCraftingRecipe, DynamicEnchantmentMenu> start(Player player, DynamicEnchantmentMenu menu) {
		final var runnable = new DynamicEnchantmentCraftingRecipe.CraftRunnable(this, player, menu);
		runnable.runTaskTimer(RPProfessions.getInstance(), 30, 1);

		return runnable;
	}

	public static class CraftRunnable extends RecipeRunnable<DynamicEnchantmentCraftingRecipe, DynamicEnchantmentMenu> {

		private static final Random RANDOM = new SecureRandom();

		@Getter
		private Material colorToFind;

		@Getter
		private final Map<Integer, Material> colors = new HashMap<>();

		private final List<Material> availableColors = new ArrayList<>(Arrays.asList(
				Material.MAGENTA_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS_PANE,
				Material.LIGHT_BLUE_STAINED_GLASS_PANE, Material.WHITE_STAINED_GLASS_PANE)
		);

		private CraftRunnable(DynamicEnchantmentCraftingRecipe recipe, Player player, DynamicEnchantmentMenu menu) {
			super(player, recipe, menu);
			this.colorToFind = pickColor();
			generateColors();
		}

		private Material pickColor() {
			final int randInt = RANDOM.nextInt(availableColors.size());
			return availableColors.get(randInt);
		}

		private void generateColors() {
			//Generate colors as long as there are empty spots
			while(colors.size() < 45) {
				//Generate random location for a random color
				final int location = RANDOM.nextInt(45);
				Material color = pickColor();

				//one chance out of 2 to change the color if the selected color is the one to find
				//to avoid an overload of that color and make the game too hard
				if(color == colorToFind && RANDOM.nextInt(2) == 0) {
					color = pickColor();
				}
				colors.putIfAbsent(location, color);
			}
		}

		private void reset() {
			this.colorToFind = pickColor();
			generateColors();
			globalTimer = 0;

			//Re-load the buttons in the menu to match them with the new colors
			getMenu().display(player);
		}

		public void clickedOn(final int slot) {
			//If the clicked slot is a color to find
			if(colors.get(slot) == colorToFind) {
				//remove the color from the list
				colors.remove(slot);

				//re display the menu without the selected color
				getMenu().getColorButtons().remove(slot);
				getMenu().display(player);
			} else {
				//If not, decrease the score
				score -= getRecipe().mistakeCost;
				//And reset the board
				reset();
			}
		}

		@Override
		protected boolean execute() {
			//Check if the selected colors have all been removed from the board
			if(!colors.containsValue(colorToFind)) {
				score++;

				if(score >= getRecipe().requiredAmount) {
					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

					getMenu().setFinished(true);
					getMenu().display(player);
					getMenu().runEvent(player);
					cancel();

					return true;
				} else {
					reset();
				}
			}
			//If the score falls below 0 -> failure
			if(score < 0) {
				//Let the menu knows the craft failed
				getMenu().setFailed(true);
				//And re displays it
				getMenu().display(player);
				cancel();

				return true;
			}
			if(globalTimer >= getRecipe().timeLaps) {
				score--;
				reset();
			}

			return false;
		}

	}

}
