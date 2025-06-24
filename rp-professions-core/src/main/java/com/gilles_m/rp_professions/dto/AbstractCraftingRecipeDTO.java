package com.gilles_m.rp_professions.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbstractCraftingRecipeDTO {

	@JsonProperty(value = "category", required = true)
	private String category;

	//Can either be a full item configuration or just a single string (= another item's id)
	@JsonProperty("reagent")
	private Object reagent;

	@JsonProperty("station")
	private String workstation;

	@JsonProperty("dynamic")
	private boolean dynamic;

}
