package com.gilles_m.rp_professions.manager;

import com.gilles_m.rp_professions.object.VillagerTradeSet;

public class VillagerTradeSetManager extends ObjectManager<VillagerTradeSet> {

	private static final VillagerTradeSetManager INSTANCE = new VillagerTradeSetManager();

	private VillagerTradeSetManager() { }

	public static VillagerTradeSetManager getInstance() {
		return INSTANCE;
	}

}
