package com.gilles_m.rp_professions.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gilles_m.rp_professions.PluginUtils;
import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.dto.VillagerTradeSetDTO;
import com.gilles_m.rp_professions.dto.randomized_object.VillagerTradeDTO;
import com.gilles_m.rp_professions.manager.VillagerTradeSetManager;
import com.gilles_m.rp_professions.object.VillagerTradeSet;
import com.gilles_m.rp_professions.object.randomized_object.VillagerTrade;
import com.gilles_m.rp_professions.object_mapper.VillagerTradeSetMapper;
import com.gilles_m.rp_professions.object_mapper.randomized_object_mapper.VillagerTradeMapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Villager;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class VillagerTradeSetLoader extends ObjectLoader<VillagerTradeSet> {

	/*
	 * Currently, the architecture of the villager trades configuration file is poorly designed. It should be changed
	 * to allow a better handling of the file by DTO classes and object/model mappers.
	 * In the meantime, these loading methods will do (even tho the nested maps make them hard to read)
	 */

	private static final VillagerTradeSetLoader INSTANCE = new VillagerTradeSetLoader();

	private static final String FILE_PATH = "trades/";

	protected VillagerTradeSetLoader() {
		super(VillagerTradeSetManager.getInstance(), FILE_PATH, "villager trade set.s");

		setLoaderIterable(new LoaderIterable<VillagerTradeSet, Map<String, Object>>() {

			@Override
			@SuppressWarnings("unchecked")
			protected Map<String, Map<String, Object>> initMap() throws IOException {
				final LinkedHashMap<String, Object> objectMap = objectMapper.readValue(file, new TypeReference<>() {});
				final LinkedHashMap<String, Map<String, Object>> configurationMap = new LinkedHashMap<>();

				//key = profession name, value = villager trade set configuration
				objectMap.forEach((key, value) -> {
					if(value instanceof Map) {
						configurationMap.put(key, (Map<String, Object>) value);
					}
				});

				return configurationMap;
			}

			@Override
			@SuppressWarnings("unchecked")
			//file name = world, id = villager profession id
			protected VillagerTradeSet buildObject(String villagerProfession, Map<String, Object> configurationMap) {
				final String worldName = file.getName().split("\\.")[0];

				//Check the world exists
				if(Bukkit.getServer().getWorld(worldName) == null) {
					throw new IllegalArgumentException(String.format("Unknown world in villager trade set folder: %s", worldName));
				}
				if(!PluginUtils.isVillagerProfession(villagerProfession)) {
					throw new IllegalArgumentException(String.format("Unknown villager profession: %s", villagerProfession));
				}
				final VillagerTradeSetDTO dto = objectMapper.convertValue(configurationMap, VillagerTradeSetDTO.class);
				final VillagerTradeSet villagerTradeSet = VillagerTradeSetMapper.getInstance().map(dto);

				//Get all the trade configurations for each profession
				configurationMap.forEach((key, value) -> {
					//Link each configuration key to its value. If a value is a Map, then the value is the configuration of a trade
					//Load and map each individual trade
					if(value instanceof Map) {
						final VillagerTrade villagerTrade = loadTrade((Map<String, Object>) value);
						villagerTrade.setId(key);
						villagerTradeSet.addTrade(villagerTrade);
					}
				});
				//Set the missing data
				villagerTradeSet.setId(worldName + "." + villagerProfession);
				villagerTradeSet.setWorld(worldName);
				villagerTradeSet.setProfession(RPProfessions.getInstance().getVersionWrapper().getVillagerProfession(villagerProfession));

				return villagerTradeSet;
			}

		});
	}

	private VillagerTrade loadTrade(Map<String, Object> tradeMap) {
		final VillagerTradeDTO dto = objectMapper.convertValue(tradeMap, VillagerTradeDTO.class);

		return VillagerTradeMapper.getInstance().map(dto);
	}

	public static VillagerTradeSetLoader getInstance() {
		return INSTANCE;
	}

}
