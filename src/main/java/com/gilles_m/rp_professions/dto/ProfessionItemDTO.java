package com.gilles_m.rp_professions.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.spigot_gillesm.item_lib.dto.ItemConfigurationDTO;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfessionItemDTO extends ItemConfigurationDTO {

	@JsonProperty("item")
	private String item;

}
