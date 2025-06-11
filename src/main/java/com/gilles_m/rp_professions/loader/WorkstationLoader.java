package com.gilles_m.rp_professions.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gilles_m.rp_professions.dto.WorkstationDTO;
import com.gilles_m.rp_professions.manager.WorkstationManager;
import com.gilles_m.rp_professions.object.station.Workstation;
import com.gilles_m.rp_professions.object_mapper.WorkstationMapper;

import java.io.IOException;
import java.util.Map;

public class WorkstationLoader extends ObjectLoader<Workstation> {

	private static final WorkstationLoader INSTANCE = new WorkstationLoader();

	private static final String FILE_PATH = "workstations.yml";

	public WorkstationLoader() {
		super(WorkstationManager.getInstance(), FILE_PATH, "workstation.s");

		setLoaderIterable(new LoaderIterable<Workstation, WorkstationDTO>() {
			@Override
			protected Map<String, WorkstationDTO> initMap() throws IOException {
				return objectMapper.readValue(file, new TypeReference<>() {});
			}

			@Override
			protected Workstation buildObject(final String id, WorkstationDTO dto) {
				final Workstation workstation = WorkstationMapper.getInstance().map(dto);
				workstation.setId(id);

				return workstation;
			}
		});
	}

	public static WorkstationLoader getInstance() {
		return INSTANCE;
	}

}
