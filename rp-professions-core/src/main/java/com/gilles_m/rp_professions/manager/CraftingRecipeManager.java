package com.gilles_m.rp_professions.manager;

import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.object.Category;
import com.gilles_m.rp_professions.object.Profession;
import com.gilles_m.rp_professions.object.crafting_recipe.AbstractCraftingRecipe;
import com.gilles_m.rp_professions.object.crafting_recipe.crafting_recipe_impl.BrewingStandCraftingRecipe;
import org.bukkit.Bukkit;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class CraftingRecipeManager extends ObjectManager<AbstractCraftingRecipe> {

	private static final CraftingRecipeManager INSTANCE = new CraftingRecipeManager();

	private CraftingRecipeManager() { }

	public void setBrewerInventoryItems(Player player, BrewerInventory inventory, final int slot, ItemStack draggedItem,
										ItemStack clickedItem) {

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RPProfessions.getInstance(), () -> {
			player.setItemOnCursor(draggedItem);
			inventory.setItem(slot, clickedItem);
			player.updateInventory();

			getBrewingRecipeFromPattern(inventory).ifPresent(
					recipe -> {
						if(recipe.canCraft(player, false) && isFueled(inventory.getHolder(), recipe)) {
							recipe.start(inventory);
						}
					});
		}, 1L);
	}

	public Optional<AbstractCraftingRecipe> getRecipeMatchingPattern(@NotNull ItemStack[] pattern) {
		return getAll().stream()
				.filter(craftingRecipe -> craftingRecipe.isPatternMatching(pattern))
				.findFirst();
	}

	public List<AbstractCraftingRecipe> getRecipesMatchingCategory(@NotNull Category category) {
		return getAll().stream()
				.filter(craftingRecipe -> craftingRecipe.getCategory().equals(category))
				.toList();
	}

	public List<AbstractCraftingRecipe> getRecipesFromProfession(Profession profession) {
		return getAll().stream()
				.filter(craftingRecipe -> craftingRecipe.getProfession().equals(profession))
				.toList();
	}

	public Optional<AbstractCraftingRecipe> get(@NotNull ItemStack itemStack) {
		return registeredObjects.stream()
				.filter(recipe -> recipe.getResult().isSimilar(itemStack))
				.findFirst();
	}

	/**
	 * Return the recipes which required level is between the given bounds.
	 *
	 * @param profession the profession
	 * @param low the (excluded) low bound
	 * @param high the (included) high bound
	 * @return a list of recipes
	 */
	public List<AbstractCraftingRecipe> getRecipesWithRequiredLevel(Profession profession, final int low, final int high) {
		return getRecipesFromProfession(profession).stream()
				.filter(craftEntity -> {
					final int requiredLevel = craftEntity.getMetaData().getRequiredLevel();
					return low < requiredLevel && requiredLevel <= high;
				})
				.toList();
	}

	public Optional<BrewingStandCraftingRecipe> getBrewingRecipeFromPattern(BrewerInventory inventory) {
		for(final AbstractCraftingRecipe craft : registeredObjects) {
			if(!(craft instanceof BrewingStandCraftingRecipe recipe)) {
				continue;
			}
			//No need to check the fuel
			if(recipe.getFuel() == null) {
				/*
				 * Check if the ingredient is similar
				 * Check if ONE of THE receptacles is similar (only one is needed)
				 */
				if(isIngredientSimilar(inventory, recipe) && areReceptacleSimilar(inventory, recipe)) {
					return Optional.of(recipe);
				}
			} else {
				//Check the fuel too
				if(isIngredientSimilar(inventory, recipe) && areReceptacleSimilar(inventory, recipe)
						&& isFuelSimilar(inventory, recipe)) {
					return Optional.of(recipe);
				}
			}
		}

		return Optional.empty();
	}

	private boolean areReceptacleSimilar(BrewerInventory inventory, BrewingStandCraftingRecipe recipe) {
		return recipe.getReceptacle().isSimilar(inventory.getItem(0))
				|| recipe.getReceptacle().isSimilar(inventory.getItem(1))
				|| recipe.getReceptacle().isSimilar(inventory.getItem(2));
	}

	private boolean isFuelSimilar(BrewerInventory inventory, BrewingStandCraftingRecipe recipe) {
		return recipe.getFuel().isSimilar(inventory.getFuel());
	}

	private boolean isIngredientSimilar(BrewerInventory inventory, BrewingStandCraftingRecipe recipe) {
		return recipe.getReagent().isSimilar(inventory.getIngredient());
	}

	private boolean isFueled(BrewingStand brewingStand, BrewingStandCraftingRecipe recipe) {
		if(recipe.isFuelConsumed()) {
			return brewingStand.getFuelLevel() > 0;
		} else {
			return true;
		}
	}

	public static CraftingRecipeManager getInstance() {
		return INSTANCE;
	}

}
