package com.gilles_m.rp_professions.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CraftingRecipeMetaDTO {

	@JsonProperty("amount")
	private int amount = 1;

	@JsonProperty("required-level")
	private int requiredLevel = 1;

	@JsonProperty("level-gain")
	private int levelGain = 1;

	@JsonProperty("level-cap")
	private int levelCap = 0;

	@JsonProperty("default")
	private boolean knownByDefault = true;

	@JsonProperty("consumed-on-use")
	private boolean consumedOnUse = true;

	@JsonProperty("allow-multiple")
	private boolean allowMultiple = false;

}
