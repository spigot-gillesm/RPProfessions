package com.gilles_m.rp_professions.object.randomized_object;

import com.gilles_m.rp_professions.Identifiable;
import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

@Setter
@Getter
public class VillagerTrade extends RandomizedObject implements Identifiable {

	private String id;

	private int level = 1;

	private int maxUse = 16;

	private ItemStack result;

	private ItemStack firstIngredient;

	private ItemStack secondIngredient;

	private boolean experienceReward = true;

	private int villagerExperienceReward = 1;

	@Override
	public final String toString() {
		return MoreObjects.toStringHelper(this)
				.add("level", level)
				.add("chance", chance)
				.add("type", type)
				.add("maxUse", maxUse)
				.add("result", result != null ? result.toString() : "null")
				.add("firstIngredient", firstIngredient != null ? firstIngredient.toString() : "null")
				.add("secondIngredient", secondIngredient != null ? secondIngredient.toString() : "null")
				.add("experienceReward", experienceReward)
				.add("villagerExperienceReward", villagerExperienceReward)
				.toString();
	}

	@Override
	public final int hashCode() {
		return Objects.hash(level, chance, type, maxUse, result, firstIngredient, secondIngredient,
				experienceReward, villagerExperienceReward);
	}

	@Override
	public final boolean equals(Object other) {
		if(other == null) {
			return false;
		}
		if(this == other) {
			return true;
		}
		if(!(other instanceof VillagerTrade otherTrade)) {
			return false;
		}

		return otherTrade.level == this.level
				&& otherTrade.chance == this.chance
				&& otherTrade.type == this.type
				&& otherTrade.maxUse == this.maxUse
				&& otherTrade.experienceReward == this.experienceReward
				&& otherTrade.villagerExperienceReward == this.villagerExperienceReward
				&& Objects.equals(otherTrade.result, this.result)
				&& Objects.equals(otherTrade.firstIngredient, this.firstIngredient)
				&& Objects.equals(otherTrade.secondIngredient, this.secondIngredient);
	}

}
