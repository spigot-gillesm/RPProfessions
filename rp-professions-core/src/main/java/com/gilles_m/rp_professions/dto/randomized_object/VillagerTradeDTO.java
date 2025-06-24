package com.gilles_m.rp_professions.dto.randomized_object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gilles_m.rp_professions.dto.RandomizedObjectDTO;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class VillagerTradeDTO extends RandomizedObjectDTO {

	@JsonProperty(required = true, value = "level")
	private int level = 1;

	@JsonProperty("max-use")
	private int maxUse = 16;

	@JsonProperty("result")
	private Object result;

	@JsonProperty(value = "first-ingredient", required = true)
	private Object firstIngredient;

	@JsonProperty("second-ingredient")
	private Object secondIngredient;

	@JsonProperty("experience-reward")
	private boolean experienceReward = true;

	@JsonProperty("villager-experience-reward")
	private int villagerExperienceReward = 1;

}
