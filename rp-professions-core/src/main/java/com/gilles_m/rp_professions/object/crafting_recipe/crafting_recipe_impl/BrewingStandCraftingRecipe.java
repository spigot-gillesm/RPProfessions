package com.gilles_m.rp_professions.object.crafting_recipe.crafting_recipe_impl;

import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.manager.PlayerManager;
import com.gilles_m.rp_professions.object.crafting_recipe.AbstractCraftingRecipe;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Sound;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

@Setter
@Getter
public class BrewingStandCraftingRecipe extends AbstractCraftingRecipe {

	//The item that must be placed in the fuel/blaze powder slot
	private ItemStack fuel;

	//The item that will be changed into the result. E.g. Mundane potion
	private ItemStack receptacle;

	//Whether the crafting recipe requires to consume some blaze powder
	private boolean fuelConsumed;

	public void start(BrewerInventory brewerInventory) {
		final BrewingStand brewingStand = brewerInventory.getHolder();

		if(brewingStand == null) {
			return;
		}
		if(fuelConsumed && brewingStand.getFuelLevel() < 1) {
			return;
		}
		updateFuelLevel(brewerInventory.getHolder());
		new BrewRunnable(this, brewerInventory, 400).runTaskTimer(RPProfessions.getInstance(), 0, 1);
	}

	private void updateFuelLevel(BrewingStand brewingStand) {
		if(fuelConsumed) {
			final int newFuelLevel = Math.max(brewingStand.getFuelLevel() - 1, 0);
			brewingStand.setFuelLevel(newFuelLevel);
			brewingStand.update(true);
		}
	}

	@Override
	public boolean isPatternMatching(ItemStack[] items) {
		return false;
	}

	@Override
	public boolean canCraft(Player player, final boolean checkMenu) {
		return PlayerManager.getInstance().getProfession(player)
				.filter(playerProfession -> profession.equals(playerProfession)
						&& PlayerManager.getInstance().getProfessionLevel(player) >= metaData.getRequiredLevel())
				.isPresent();
	}

	private static class BrewRunnable extends BukkitRunnable {

		private final BrewingStandCraftingRecipe recipe;

		private final BrewerInventory inventory;

		private final BrewingStand brewingStand;

		private final ItemStack[] originalContent;

		//Start at max time and is decreased by (1 * interval's ticks) tick(s) at every interval
		//-> get decremented
		private int currentTimer;

		private BrewRunnable(BrewingStandCraftingRecipe recipe, BrewerInventory inventory, final int time) {
			this.recipe = recipe;
			this.inventory = inventory;
			this.brewingStand = inventory.getHolder();
			this.originalContent = inventory.getContents();
			this.currentTimer = time;
		}

		@Override
		public void run() {
			if(hasInventoryChanged(inventory.getContents())) {
				cancel();
				return;
			}
			if(currentTimer == 0) {
				brew();
				cancel();
			} else {
				brewingStand.setBrewingTime(currentTimer--);
				brewingStand.update(true);
			}
		}

		private void brew() {
			for(int i = 0; i < 3; i++) {
				//Check if there's a receptacle in the slot
				if(recipe.getReceptacle().isSimilar(inventory.getItem(i))) {
					inventory.setItem(i, recipe.getResult());
				}
			}
			final ItemStack newIngredient = inventory.getIngredient().clone();
			newIngredient.setAmount(newIngredient.getAmount() - 1);
			inventory.setIngredient(newIngredient);

			if(inventory.getFuel() != null) {
				final ItemStack newFuel = inventory.getFuel().clone();
				newFuel.setAmount(newFuel.getAmount() - 1);
				inventory.setFuel(newFuel);
			}
			brewingStand.getWorld().playSound(brewingStand.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1, 1);
		}

		private boolean hasInventoryChanged(ItemStack[] currentContent) {
			for(int i = 0; i < currentContent.length; i ++) {
				if((currentContent[i] == null || originalContent[i] == null) && (currentContent[i] != null || originalContent[i] != null)) {
					return true;
				}
				if(currentContent[i] != null && !currentContent[i].equals(originalContent[i])) {
					return true;
				}
			}

			return false;
		}

	}

}
