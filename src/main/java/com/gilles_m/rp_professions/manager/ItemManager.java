package com.gilles_m.rp_professions.manager;

import com.gilles_m.rp_professions.object.ProfessionItem;
import com.github.spigot_gillesm.item_lib.ItemUtil;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ItemManager extends ObjectManager<ProfessionItem> {

	public static final String KEY_ID = "item_id";

	private static final ItemManager INSTANCE = new ItemManager();

	private ItemManager() { }

	public Optional<ProfessionItem> get(@NotNull ItemStack itemStack) {
		final Optional<String> id = ItemUtil.getPersistentString(itemStack, KEY_ID);

		if(id.isPresent()) {
			return get(id.get());
		} else {
			return Optional.empty();
		}
	}

	public boolean doItemMatchId(@NotNull ItemStack itemStack, @NotNull String id) {
		return ItemUtil.getPersistentString(itemStack, KEY_ID)
				.map(string -> string.equals(id))
				.orElse(false);
	}

	public static ItemManager getInstance() {
		return INSTANCE;
	}

}
