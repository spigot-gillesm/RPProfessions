package com.gilles_m.rp_professions.object;

import com.gilles_m.rp_professions.Identifiable;
import com.gilles_m.rp_professions.object.crafting_recipe.AbstractCraftingRecipe;
import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ProfessionItem implements Identifiable {

	private String id;

	private ItemStack itemStack;

	@Nullable
	private AbstractCraftingRecipe craftingRecipe;

	private List<Effect> effects = new ArrayList<>();

	public ProfessionItem(String id, ItemStack itemStack) {
		this.itemStack = itemStack;
		this.id = id;
	}

	public ItemStack getItemStack() {
		//ItemStack is read only. Avoid unwanted modifications
		return itemStack.clone();
	}

	public boolean hasEffects() {
		return !effects.isEmpty();
	}

	@Override
	public final String toString() {
		return MoreObjects.toStringHelper(this)
				.add("id", id)
				.add("itemStack", itemStack.toString())
				.add("craftingRecipe", craftingRecipe != null ? craftingRecipe : "null")
				.add("effects", effects.toString())
				.toString();
	}

}
