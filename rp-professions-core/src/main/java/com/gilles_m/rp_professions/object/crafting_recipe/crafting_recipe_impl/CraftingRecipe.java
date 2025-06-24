package com.gilles_m.rp_professions.object.crafting_recipe.crafting_recipe_impl;

import com.gilles_m.rp_professions.object.crafting_recipe.AbstractCraftingRecipe;
import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@Setter
@Getter
public class CraftingRecipe extends AbstractCraftingRecipe {

	protected ItemStack[] pattern = new ItemStack[9];

	public CraftingRecipe() {
		super();
	}

	@Override
	public boolean isPatternMatching(ItemStack[] items) {
		//Check for similarities if multiple at once is allowed
		if(allowMultiple()) {
			for(int i = 0; i < 9; i++) {
				if(items[i] == null && pattern[i] != null) {
					return false;
				}
				if (items[i] != null && !items[i].isSimilar(pattern[i])) {
					return false;
				}
			}

			return true;
		} else {
			return Arrays.equals(pattern, items);
		}
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("id", id)
				.add("category", category)
				.add("reagent", reagent != null ? reagent.toString() : "null")
				.add("workstation", workstation)
				.add("metadata", metaData.toString())
				.add("pattern", Arrays.toString(pattern))
				.toString();
	}

}
