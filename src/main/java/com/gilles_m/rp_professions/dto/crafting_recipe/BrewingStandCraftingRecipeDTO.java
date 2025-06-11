package com.gilles_m.rp_professions.dto.crafting_recipe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gilles_m.rp_professions.dto.AbstractCraftingRecipeDTO;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BrewingStandCraftingRecipeDTO extends AbstractCraftingRecipeDTO {

	@JsonProperty("fuel")
	private Object fuel;

	@JsonProperty("receptacle")
	private Object receptacle;

	@JsonProperty("fuel-consumed")
	private boolean fuelConsumed;

}
