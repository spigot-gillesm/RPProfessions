package com.gilles_m.rp_professions.manager;

import com.gilles_m.rp_professions.object.Profession;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ProfessionManager extends ObjectManager<Profession> {

	private static final ProfessionManager INSTANCE = new ProfessionManager();

	private ProfessionManager() { }

	@Override
	public Optional<Profession> get(@NotNull final String id) {
		//Ignore case
		return registeredObjects.stream().filter(o -> o.getId().equalsIgnoreCase(id)).findFirst();
	}

	public static ProfessionManager getInstance() {
		return INSTANCE;
	}

}
