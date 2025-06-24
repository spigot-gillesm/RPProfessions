package com.gilles_m.rp_professions.dto.crafting_recipe.dynamic_recipe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gilles_m.rp_professions.dto.crafting_recipe.CraftingRecipeDTO;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicEnchantmentCraftingRecipeDTO extends CraftingRecipeDTO {

	@JsonProperty("required-amount")
	private int requiredAmount = 1;

	@JsonProperty("mistake-cost")
	private int mistakeCost = 1;

	//In ticks
	@JsonProperty("time-laps")
	private int timeLaps = 20 * 4;

}
