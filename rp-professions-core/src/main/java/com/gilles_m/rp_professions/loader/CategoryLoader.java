package com.gilles_m.rp_professions.loader;

import com.gilles_m.rp_professions.manager.CategoryManager;
import com.gilles_m.rp_professions.object.Category;
import com.github.spigot_gillesm.item_lib.ItemLoader;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Map;

public class CategoryLoader extends ObjectLoader<Category> {

	private static final CategoryLoader INSTANCE = new CategoryLoader();

	private static final String FILE_PATH = "categories.yml";

	private CategoryLoader() {
		super(CategoryManager.getInstance(), FILE_PATH, "category.ies");

		setLoaderIterable(new LoaderIterable<Category, ItemStack>() {
			@Override
			protected Map<String, ItemStack> initMap() throws IOException {
				return ItemLoader.getInstance().asItemStackMap(file);
			}

			@Override
			protected Category buildObject(String id, ItemStack itemStack) {
				return new Category(id, itemStack);
			}
		});
	}

	public static CategoryLoader getInstance() {
		return INSTANCE;
	}

}
