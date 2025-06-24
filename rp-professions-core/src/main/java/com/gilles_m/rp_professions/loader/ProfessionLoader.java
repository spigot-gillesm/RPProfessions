package com.gilles_m.rp_professions.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gilles_m.rp_professions.dto.ProfessionDTO;
import com.gilles_m.rp_professions.manager.ProfessionManager;
import com.gilles_m.rp_professions.object.Profession;
import com.gilles_m.rp_professions.object_mapper.ProfessionMapper;
import com.github.spigot_gillesm.file_utils.FileUtils;

import java.io.IOException;
import java.util.Map;

public class ProfessionLoader extends ObjectLoader<Profession> {

	private static final ProfessionLoader INSTANCE = new ProfessionLoader();

	private static final String FILE_PATH = "professions.yml";

	public ProfessionLoader() {
		super(ProfessionManager.getInstance(), FILE_PATH, "profession.s");

		setLoaderIterable(new LoaderIterable<Profession, ProfessionDTO>() {
			@Override
			protected Map<String, ProfessionDTO> initMap() throws IOException {
				return objectMapper.readValue(FileUtils.getResource(FILE_PATH), new TypeReference<>() {});
			}

			@Override
			protected Profession buildObject(final String id, ProfessionDTO dto) {
				final Profession profession = ProfessionMapper.getInstance().map(dto);
				profession.setId(id);

				return profession;
			}
		});
	}

	public static ProfessionLoader getInstance() {
		return INSTANCE;
	}

}
