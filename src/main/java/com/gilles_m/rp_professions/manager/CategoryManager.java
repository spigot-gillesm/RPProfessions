package com.gilles_m.rp_professions.manager;

import com.gilles_m.rp_professions.object.Category;

public class CategoryManager extends ObjectManager<Category> {

	private static final CategoryManager INSTANCE = new CategoryManager();

	private CategoryManager() { }

	public static CategoryManager getInstance() {
		return INSTANCE;
	}

}
