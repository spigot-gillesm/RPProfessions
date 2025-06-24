package com.gilles_m.rp_professions.dto.crafting_recipe.dynamic_recipe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gilles_m.rp_professions.dto.crafting_recipe.CraftingRecipeDTO;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicForgeCraftingRecipeDTO extends CraftingRecipeDTO {

	@JsonProperty("required-heat")
	private int requiredHeat = 5;

	//How much +/- heat there can be before it fails
	@JsonProperty("tolerance")
	private int tolerance = 2;

	//Unit = Tick(s)
	@JsonProperty("time")
	private long time = (long) 5 * 20;

	//Between 0.0 and 1.0
	@JsonProperty("change-chance")
	private double changeChance = 0.25;

}
