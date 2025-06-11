package com.gilles_m.rp_professions.manager;

import com.gilles_m.rp_professions.object.station.Workstation;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class WorkstationManager extends ObjectManager<Workstation> {

	private static final WorkstationManager INSTANCE = new WorkstationManager();

	@Override
	public Optional<Workstation> get(@NotNull String id) {
		if(Workstation.WORKBENCH.getId().equals(id)) {
			return Optional.of(Workstation.WORKBENCH);
		}

		return super.get(id);
	}

	public static WorkstationManager getInstance() {
		return INSTANCE;
	}

}
