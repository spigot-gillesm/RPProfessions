package com.gilles_m.rp_professions.object;

import com.gilles_m.rp_professions.Identifiable;
import com.gilles_m.rp_professions.PluginUtils;
import com.gilles_m.rp_professions.object.randomized_object.DropItem;
import com.gilles_m.rp_professions.object.randomized_object.RandomizedObject;
import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class Drop implements Identifiable {

	@Getter
	private final String id;

	@Getter
	private final boolean isBlockDrop;

	private final Set<DropItem> dropItems = new HashSet<>();

	@Setter
	private String world;

	public Drop(String id) {
		this.id = id;
		this.isBlockDrop = PluginUtils.isMaterial(id);
	}

	public void addItem(DropItem dropItem) {
		dropItems.add(dropItem);
	}

	public void drop(Location location) {
		if(!location.getWorld().getName().equals(world)) {
			return;
		}
		final Set<ItemStack> items = new HashSet<>();

		for(final DropItem item : RandomizedObject.randomAbsolute(dropItems)) {
			items.add(item.getItem());
		}
		items.forEach(item -> location.getWorld().dropItemNaturally(location, item));
	}

	public final String toString() {
		return MoreObjects.toStringHelper(this)
				.add("id", id)
				.add("isBlockDrop", isBlockDrop)
				.add("dropItems", dropItems)
				.toString();
	}

}
