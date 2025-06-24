package com.gilles_m.rp_professions.object.crafting_recipe.dynamic_crafting_recipe_impl;

import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.menu.dynamic_crafting_menu_impl.DynamicAnvilMenu;
import com.gilles_m.rp_professions.object.crafting_recipe.DynamicCraftingRecipe;
import com.gilles_m.rp_professions.object.crafting_recipe.RecipeRunnable;
import com.gilles_m.rp_professions.object.crafting_recipe.crafting_recipe_impl.CraftingRecipe;
import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

@Setter
public class DynamicAnvilCraftingRecipe extends CraftingRecipe implements DynamicCraftingRecipe<DynamicAnvilCraftingRecipe, DynamicAnvilMenu> {

	private int requiredHammering = 10;

	private int mistakeCost = 6;

	//In ticks
	private int timeLaps = 30;

	@Override
	public RecipeRunnable<DynamicAnvilCraftingRecipe, DynamicAnvilMenu> start(Player player, DynamicAnvilMenu menu) {
		final var runnable = new CraftRunnable(this, player, menu);
		runnable.runTaskTimer(RPProfessions.getInstance(), 30, 1);

		return runnable;
	}

	@Override
	public final String toString() {
		return MoreObjects.toStringHelper(this)
				.add("id", id)
				.add("category", category)
				.add("reagent", reagent != null ? reagent.toString() : "null")
				.add("workstation", workstation)
				.add("metadata", metaData.toString())
				.add("pattern", Arrays.toString(pattern))
				.add("requiredHammering", requiredHammering)
				.add("mistakeCost", mistakeCost)
				.add("timeLaps", timeLaps)
				.toString();
	}

	public static class CraftRunnable extends RecipeRunnable<DynamicAnvilCraftingRecipe, DynamicAnvilMenu> {

		private static final Random RANDOM = new SecureRandom();

		@Getter
		private int nextHitTargetLocation;

		private CraftRunnable(DynamicAnvilCraftingRecipe recipe, Player player, DynamicAnvilMenu menu) {
			super(player, recipe, menu);

			this.nextHitTargetLocation = new Random().nextInt(27);
		}

		@Override
		protected boolean execute() {
			//If the score is high enough -> success
			if(score >= getRecipe().requiredHammering) {
				player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				//Let the menu knows the craft is over
				getMenu().setFinished(true);
				//And re displays it
				getMenu().display(player);
				getMenu().runEvent(player);
				cancel();

				return true;
			}
			//If the score falls below 0 -> failure
			if(score < 0) {
				player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
				//Let the menu knows the craft failed
				getMenu().setFailed(true);
				//And re displays it
				getMenu().display(player);
				cancel();

				return true;
			}
			//If no click has been made during the given time laps -> considered as fail
			if(globalTimer >= getRecipe().timeLaps) {
				fail();
			}

			return false;
		}

		public void buildUp() {
			//Increase the score
			score++;
			//Reset the global timer
			globalTimer = 0;
			//Reset the position of the next hit target
			setNextHitTarget();
		}

		public void fail() {
			score -= getRecipe().mistakeCost;
			globalTimer = 0;
			setNextHitTarget();
		}

		public void setNextHitTarget() {
			//Generate a position within 3 rows for the hit button to appear
			this.nextHitTargetLocation = RANDOM.nextInt(27);
			getMenu().display(player);
		}

		/**
		 *
		 * @return the score on a scale of 0 to 9 for display purpose
		 */
		public int getProportionalScore() {
			return (int) Math.ceil( ((score * 1.0 / getRecipe().requiredHammering) * 9.0) );
		}

	}

}
