package com.gilles_m.rp_professions.object.randomized_object;

import com.google.common.base.MoreObjects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Setter
@Getter(AccessLevel.PRIVATE)
public class DropItem extends RandomizedObject {

	private RangeInteger amount;

	private ItemStack itemStack;

	public ItemStack getItem() {
		final ItemStack item = itemStack.clone();
		item.setAmount(amount.getInt());

		return item;
	}

	public final String toString() {
		return MoreObjects.toStringHelper(this)
				.add("amount", amount.toString())
				.add("chance", chance)
				.add("itemStack", itemStack.toString())
				.toString();
	}

}
