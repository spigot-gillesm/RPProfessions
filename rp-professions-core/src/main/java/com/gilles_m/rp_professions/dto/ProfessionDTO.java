package com.gilles_m.rp_professions.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfessionDTO {

	@JsonProperty(value = "workstations", required = true)
	private List<String> workstations = new ArrayList<>();

	@JsonProperty(value = "display-name", required = true)
	private String displayName;

	@JsonProperty(value = "icon", required = true)
	private String icon;

	@JsonProperty(value = "description")
	private List<String> description = new ArrayList<>();

	@JsonProperty(value = "example-craft")
	private String exampleCraftId;

}
