package com.gilles_m.rp_professions.dto.crafting_recipe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gilles_m.rp_professions.dto.AbstractCraftingRecipeDTO;
import com.google.common.base.MoreObjects;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CraftingRecipeDTO extends AbstractCraftingRecipeDTO {

	//Link the position in the crafting grid to either an item id/material or a full item configuration
	@JsonProperty("recipe")
	private Map<Integer, Object> recipe = new HashMap<>();

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("recipe", recipe.toString())
				.toString();
	}

}
