package com.gilles_m.rp_professions.object;

import com.gilles_m.rp_professions.Identifiable;
import com.google.common.base.MoreObjects;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
public class Category implements Identifiable {

	private final String id;

	private final ItemStack icon;

	public Category(@NotNull String id, ItemStack icon) {
		this.id = id;
		this.icon = icon;
	}

	@Override
	public final String toString() {
		return MoreObjects.toStringHelper(this)
				.add("id", id)
				.add("icon", icon.toString())
				.toString();
	}

}
