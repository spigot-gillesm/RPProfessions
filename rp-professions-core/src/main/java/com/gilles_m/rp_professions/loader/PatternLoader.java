package com.gilles_m.rp_professions.loader;

import com.gilles_m.rp_professions.manager.CraftingRecipeManager;
import com.gilles_m.rp_professions.manager.PatternManager;
import com.gilles_m.rp_professions.object.RecipePattern;
import com.gilles_m.rp_professions.object.crafting_recipe.AbstractCraftingRecipe;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PatternLoader extends ObjectLoader<RecipePattern> {

	private static final PatternLoader INSTANCE = new PatternLoader();

	private PatternLoader() {
		super(PatternManager.getInstance(), "pattern.s");

		setLoaderIterable(new LoaderIterable<RecipePattern, AbstractCraftingRecipe>() {

			@Override
			protected Map<String, AbstractCraftingRecipe> initMap() {
				return CraftingRecipeManager.getInstance()
						.getAll()
						.stream()
						.filter(recipe -> !recipe.getMetaData().isKnownByDefault())
						.collect(Collectors.toMap(AbstractCraftingRecipe::getId, recipe -> recipe));
			}

			@Override
			protected RecipePattern buildObject(String id, AbstractCraftingRecipe craftingRecipe) {
				return new RecipePattern(id, craftingRecipe.getMetaData().isConsumedOnUse(), createPhysicalPattern(craftingRecipe));
			}

		});
	}

	private ItemStack createPhysicalPattern(AbstractCraftingRecipe craftingRecipe) {
		return SimpleItem.newBuilder()
				.material(Material.ENCHANTED_BOOK)
				.displayName("&fPattern: " + craftingRecipe.getResult().getItemMeta().getDisplayName())
				.lore(createLore(craftingRecipe))
				.addItemFlags(ItemFlag.HIDE_ENCHANTS)
				.addPersistentString(PatternManager.KEY_ID, craftingRecipe.getId())
				.build()
				.make()
				.getItemStack();
	}

	private List<String> createLore(AbstractCraftingRecipe craftingRecipe) {
		final ItemMeta itemMeta = craftingRecipe.getResult().getItemMeta();
		final List<String> lore = new ArrayList<>(Arrays.asList("",
				"&aUse&7: Teaches you how to make a ",
				itemMeta.getDisplayName(),
				"&7Requires " + craftingRecipe.getProfession().getDisplayName() + " &7lvl " + craftingRecipe.getMetaData().getRequiredLevel(),
				"&7---------------------------",
				"",
				itemMeta.getDisplayName()));

		if(itemMeta.hasLore()) {
			lore.addAll(itemMeta.getLore());
		}

		return lore;
	}

	public static PatternLoader getInstance() {
		return INSTANCE;
	}

}
