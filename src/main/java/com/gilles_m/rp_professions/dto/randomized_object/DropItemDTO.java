package com.gilles_m.rp_professions.dto.randomized_object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gilles_m.rp_professions.dto.RandomizedObjectDTO;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DropItemDTO extends RandomizedObjectDTO {

	@JsonProperty("amount")
	private String amount;

}
