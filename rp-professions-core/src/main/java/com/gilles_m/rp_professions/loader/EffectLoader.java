package com.gilles_m.rp_professions.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gilles_m.rp_professions.dto.EffectDTO;
import com.gilles_m.rp_professions.dto.effect.MMOItemEffectDTO;
import com.gilles_m.rp_professions.manager.EffectManager;
import com.gilles_m.rp_professions.object.Effect;
import com.gilles_m.rp_professions.object.effect.MMOItemEffect;
import com.gilles_m.rp_professions.object_mapper.effect.MMOItemEffectMapper;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class EffectLoader extends ObjectLoader<Effect> {

	private static final EffectLoader INSTANCE = new EffectLoader();

	private static final String FILE_PATH = "effects.yml";

	private EffectLoader() {
		super(EffectManager.getInstance(), FILE_PATH, "effect.s");

		setLoaderIterable(new LoaderIterable<Effect, EffectDTO>() {
			@Override
			protected Map<String, EffectDTO> initMap() throws IOException {
				final LinkedHashMap<String, Object> objectMap = objectMapper.readValue(file, new TypeReference<>() {});
				final Map<String, EffectDTO> dtoMap = new LinkedHashMap<>();

				objectMap.forEach((key, value) -> {
					if(value instanceof Map) {
						dtoMap.put(key, objectMapper.convertValue(value, MMOItemEffectDTO.class));
					}
				});

				return dtoMap;
			}

			@Override
			protected Effect buildObject(final String id, EffectDTO dto) {
				final MMOItemEffect mmoItemEffect = MMOItemEffectMapper.getInstance().map(dto);
				mmoItemEffect.setId(id);

				return mmoItemEffect;
			}
		});
	}

	public static EffectLoader getInstance() {
		return INSTANCE;
	}

}
