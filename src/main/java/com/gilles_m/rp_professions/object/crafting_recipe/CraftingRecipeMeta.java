package com.gilles_m.rp_professions.object.crafting_recipe;

import com.google.common.base.MoreObjects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Setter
public class CraftingRecipeMeta {

	@Getter
	private int amount = 1;

	@Getter
	private int requiredLevel = 1;

	@Getter
	private int levelGain = 1;

	@Getter
	private int levelCap = 0;

	@Getter
	private boolean knownByDefault = true;

	@Getter
	private boolean consumedOnUse = true;

	@Getter(AccessLevel.PACKAGE)
	private boolean allowMultiple = false;

	protected CraftingRecipeMeta() {
		setLevelCap();
	}

	private void setLevelCap() {
		//Default level cap value
		if(levelCap <= 0) {
			this.levelCap = requiredLevel + 20;
		}
	}

	@Override
	public final String toString() {
		return MoreObjects.toStringHelper(this)
				.add("amount", amount)
				.add("requiredLevel", requiredLevel)
				.add("levelGain", levelGain)
				.add("levelCap", levelCap)
				.add("knownByDefault", knownByDefault)
				.add("allowMultiple", allowMultiple)
				.toString();
	}

}
