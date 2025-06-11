package com.gilles_m.rp_professions.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gilles_m.rp_professions.dto.randomized_object.DropItemDTO;
import com.gilles_m.rp_professions.manager.DropManager;
import com.gilles_m.rp_professions.object.Drop;
import com.gilles_m.rp_professions.object.randomized_object.DropItem;
import com.gilles_m.rp_professions.object_mapper.ProfessionItemMapper;
import com.gilles_m.rp_professions.object_mapper.randomized_object_mapper.DropItemMapper;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class DropLoader extends ObjectLoader<Drop> {

	/*
	 * Just like trade sets, drops.yml must be reworked.
	 */

	private static final DropLoader INSTANCE = new DropLoader();

	private static final String FILE_PATH = "drops.yml";

	private DropLoader() {
		super(DropManager.getInstance(), FILE_PATH, "drop.s");

		setLoaderIterable(new LoaderIterable<Drop, Map<String, Object>>() {

			@Override
			@SuppressWarnings("unchecked")
			protected Map<String, Map<String, Object>> initMap() throws IOException {
				final LinkedHashMap<String, Object> objectMap = objectMapper.readValue(file, new TypeReference<>() {});
				//Key = drop id, value = configuration map
				final LinkedHashMap<String, Map<String, Object>> configurationMap = new LinkedHashMap<>();

				objectMap.forEach((key, value) -> {
					if(value instanceof Map) {
						configurationMap.put(key, (Map<String, Object>) value);
					}
				});

				return configurationMap;
			}

			@Override
			protected Drop buildObject(String id, Map<String, Object> configurationMap) {
				final var drop = new Drop(id);

				configurationMap.forEach((itemId, configuration) -> {
					final DropItemDTO dropItemDTO = objectMapper.convertValue(configuration, DropItemDTO.class);
					final DropItem dropItem = DropItemMapper.getInstance().map(dropItemDTO);
					dropItem.setItemStack(ProfessionItemMapper.getInstance().getExternalItem(itemId));

					drop.addItem(dropItem);
				});

				return drop;
			}
		});
	}

	public static DropLoader getInstance() {
		return INSTANCE;
	}

}
