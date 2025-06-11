package com.gilles_m.rp_professions.manager;

import com.gilles_m.rp_professions.object.Effect;

public class EffectManager extends ObjectManager<Effect> {

	private static final EffectManager INSTANCE = new EffectManager();

	private EffectManager() { }

	public static EffectManager getInstance() {
		return INSTANCE;
	}

}
