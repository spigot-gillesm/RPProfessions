package com.gilles_m.rp_professions.manager;

import com.gilles_m.rp_professions.object.RecipePattern;
import com.github.spigot_gillesm.item_lib.ItemUtil;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class PatternManager extends ObjectManager<RecipePattern> {

	public static final String KEY_ID = "pattern-id";

	private static final PatternManager INSTANCE = new PatternManager();

	private PatternManager() { }

	private Optional<String> getIdFromItem(ItemStack itemStack) {
		return ItemUtil.getPersistentString(itemStack, KEY_ID);
	}

	public Optional<RecipePattern> getPattern(ItemStack itemStack) {
		return getIdFromItem(itemStack).flatMap(this::get).or(Optional::empty);
	}

	public static PatternManager getInstance() {
		return INSTANCE;
	}

}
