package com.gilles_m.rp_professions.dto.effect;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gilles_m.rp_professions.dto.EffectDTO;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MMOItemEffectDTO extends EffectDTO {

	@JsonProperty("mmo-id")
	private String mmoId;

	@JsonProperty("mmo-stats")
	private Map<String, Double> statModifiers = new HashMap<>();

}
