package com.gilles_m.rp_professions.dto.crafting_recipe.dynamic_recipe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gilles_m.rp_professions.dto.crafting_recipe.CraftingRecipeDTO;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicAnvilCraftingRecipeDTO extends CraftingRecipeDTO {

	@JsonProperty("required-hammering")
	private int requiredHammering = 10;

	@JsonProperty("mistake-cost")
	private int mistakeCost = 6;

	//In ticks
	@JsonProperty("time-laps")
	private int timeLaps = 30;

}
