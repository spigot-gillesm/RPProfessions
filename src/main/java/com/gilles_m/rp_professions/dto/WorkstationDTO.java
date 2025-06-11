package com.gilles_m.rp_professions.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkstationDTO {

	@JsonProperty(value = "display-name", required = true)
	private String displayName;

	@JsonProperty(value = "materials", required = true)
	private List<String> materials;

	@JsonProperty("restricted")
	private boolean restricted;

	@JsonProperty("fuel")
	private String fuel;

	@JsonProperty(value = "station", required = true)
	private String craftingStation;

	@JsonProperty("tool")
	private String tool;

}
