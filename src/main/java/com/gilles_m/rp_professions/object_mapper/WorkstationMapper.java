package com.gilles_m.rp_professions.object_mapper;

import com.gilles_m.rp_professions.PluginUtils;
import com.gilles_m.rp_professions.dto.WorkstationDTO;
import com.gilles_m.rp_professions.object.station.CraftingStation;
import com.gilles_m.rp_professions.object.station.Workstation;
import org.bukkit.Material;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.Map;
import java.util.stream.Collectors;

public class WorkstationMapper {

	private static final WorkstationMapper INSTANCE = new WorkstationMapper();

	private final ModelMapper modelMapper = new ModelMapper();

	private WorkstationMapper() {
		configureMapper();
	}

	private void configureMapper() {
		modelMapper.addMappings(new PropertyMap<WorkstationDTO, Workstation>() {
			@Override
			protected void configure() {
				using((Converter<WorkstationDTO, Material>) context -> {
					final String fuel = context.getSource().getFuel();

					if(fuel == null) {
						return null;
					}
					if(!PluginUtils.isMaterial(fuel)) {
						throw new IllegalArgumentException(String.format("Invalid material for fuel: %s", fuel));
					}

					return Material.valueOf(fuel.toUpperCase());
				}).map(source).setFuel(null);
			}
		});
		modelMapper.addMappings(new PropertyMap<WorkstationDTO, Workstation>() {
			@Override
			protected void configure() {
				using((Converter<WorkstationDTO, CraftingStation>) context -> {
					final String craftingStation = context.getSource().getCraftingStation();

					return CraftingStation.getCraftingStation(craftingStation)
							.orElseThrow(() -> new IllegalArgumentException(String.format("Invalid crafting station: %s", craftingStation)));
				}).map(source).setCraftingStation(CraftingStation.CRAFTING_TABLE);
			}
		});
		modelMapper.typeMap(WorkstationDTO.class, Workstation.class)
				.addMappings(mapper -> mapper.map(WorkstationDTO::getDisplayName, Workstation::setDisplayName))
				.addMappings(mapper -> mapper.map(WorkstationDTO::getMaterials, Workstation::setMaterials))
				.addMappings(mapper -> mapper.map(WorkstationDTO::isRestricted, Workstation::setRestricted))
				.addMappings(mapper -> mapper.map(WorkstationDTO::getTool, Workstation::setTool));
	}

	public Workstation map(WorkstationDTO dto) {
		return modelMapper.map(dto, Workstation.class);
	}

	public Map<String, Workstation> map(Map<String, WorkstationDTO> workstationDTOMap) {
		return workstationDTOMap
				.entrySet()
				.stream()
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						entry -> modelMapper.map(entry.getValue(), Workstation.class)
				));
	}

	public static WorkstationMapper getInstance() {
		return INSTANCE;
	}

}
