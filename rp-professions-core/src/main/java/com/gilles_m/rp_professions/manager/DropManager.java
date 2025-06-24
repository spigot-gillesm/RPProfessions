package com.gilles_m.rp_professions.manager;

import com.gilles_m.rp_professions.object.Drop;

public class DropManager extends ObjectManager<Drop> {

	private static final DropManager INSTANCE = new DropManager();

	private DropManager() { }

	public static DropManager getInstance() {
		return INSTANCE;
	}

}
