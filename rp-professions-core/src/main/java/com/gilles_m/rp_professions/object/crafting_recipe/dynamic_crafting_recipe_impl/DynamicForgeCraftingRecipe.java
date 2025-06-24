package com.gilles_m.rp_professions.object.crafting_recipe.dynamic_crafting_recipe_impl;

import com.gilles_m.rp_professions.PluginUtils;
import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.menu.dynamic_crafting_menu_impl.DynamicForgeMenu;
import com.gilles_m.rp_professions.object.crafting_recipe.DynamicCraftingRecipe;
import com.gilles_m.rp_professions.object.crafting_recipe.RecipeRunnable;
import com.gilles_m.rp_professions.object.crafting_recipe.crafting_recipe_impl.CraftingRecipe;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.util.Random;

@Setter
public class DynamicForgeCraftingRecipe extends CraftingRecipe implements DynamicCraftingRecipe<DynamicForgeCraftingRecipe, DynamicForgeMenu> {

	private static final Random RANDOM = new SecureRandom();

	@Getter
	private int requiredHeat = 5;

	//How much +/- heat there can be before it fails
	private int tolerance = 2;

	//Unit = Tick(s)
	private long time = (long) 5 * 20;

	//Between 0.0 and 1.0
	private double changeChance = 0.25;

	@Override
	public RecipeRunnable<DynamicForgeCraftingRecipe, DynamicForgeMenu> start(Player player, DynamicForgeMenu menu) {
		final var runnable = new CraftRunnable(this, player, menu);
		runnable.runTaskTimer(RPProfessions.getInstance(), 0, 1);

		return runnable;
	}

	public static class CraftRunnable extends RecipeRunnable<DynamicForgeCraftingRecipe, DynamicForgeMenu> {

		@Getter
		private int heat;

		private long currentTimer;

		/**
		 * Represents the direction in which the heat will be modified
		 * based on the forgeCraft.changeChance. 0 means on the left
		 * (= cooling down), 1 means right (= heating up)
		 */
		private int direction;

		private CraftRunnable(DynamicForgeCraftingRecipe recipe, Player player, DynamicForgeMenu menu) {
			super(player, recipe, menu);

			this.heat = recipe.requiredHeat;
			this.currentTimer = recipe.time;
			this.direction = getDirection();
		}

		/**
		 * Get the direction of the heat used for random modification
		 *
		 * @return a random number between 0 and 1
		 */
		private int getDirection() {
			return RANDOM.nextInt(2);
		}

		@Override
		protected boolean execute() {
			//Check if the smelting process is done
			if(currentTimer <= 0) {
				player.getWorld().playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
				//Let the menu knows the craft is over
				getMenu().setFinished(true);
				//And re displays it
				getMenu().display(player);
				getMenu().runEvent(player);
				cancel();

				return true;
			}
			//Check if the heat exceeded the threshold -> The craft failed
			if(heat > getRecipe().requiredHeat + getRecipe().tolerance || heat < getRecipe().requiredHeat - getRecipe().tolerance) {
				player.getWorld().playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
				//Let the menu knows the craft failed
				getMenu().setFailed(true);
				//And re displays it
				getMenu().display(player);
				cancel();

				return true;
			}
			//Check if the heat should be changed randomly. Can occur only once per second (= every 20 ticks)
			if(RANDOM.nextDouble() <= getRecipe().changeChance && globalTimer >= 20) {
				//Check if the heat should go down
				if(direction == 0) {
					heat--;
				} else {
					//or up
					heat++;
				}
				//Update the menu
				getMenu().display(player);
				//Reset the global timer
				globalTimer = 0;
			}
			//If the heat is right -> decrease the timer
			if(heat == getRecipe().requiredHeat) {
				currentTimer--;
			}

			return false;
		}

		public void heatUp() {
			heat = PluginUtils.clamp(heat + 1, 0, 9);
			//Randomly reset the direction
			direction = getDirection();
		}

		public void coolDown() {
			heat = PluginUtils.clamp(heat - 1, 0, 9);
			//Randomly reset the direction
			direction = getDirection();
		}

	}

}
