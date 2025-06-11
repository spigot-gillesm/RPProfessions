package com.gilles_m.rp_professions.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EffectDTO {

	@JsonProperty("duration")
	protected double duration;

	@JsonProperty("save-on-quit")
	protected boolean savedOnQuit;

	@JsonProperty("persistent-through-death")
	protected boolean persistentThroughDeath;

}
