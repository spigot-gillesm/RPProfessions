package com.gilles_m.rp_professions.object_mapper;

import com.gilles_m.rp_professions.PluginUtils;
import com.gilles_m.rp_professions.dto.ProfessionDTO;
import com.gilles_m.rp_professions.manager.WorkstationManager;
import com.gilles_m.rp_professions.object.Profession;
import com.gilles_m.rp_professions.object.station.Workstation;
import org.bukkit.Material;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProfessionMapper {

	private static final ProfessionMapper INSTANCE = new ProfessionMapper();

	private final ModelMapper modelMapper = new ModelMapper();

	private ProfessionMapper() {
		configureMapper();
	}

	private void configureMapper() {
		modelMapper.addMappings(new PropertyMap<ProfessionDTO, Profession>() {
			@Override
			protected void configure() {
				using((Converter<ProfessionDTO, List<Workstation>>) context ->
						context.getSource()
								.getWorkstations()
								.stream()
								.map(workstation -> WorkstationManager.getInstance()
										.get(workstation)
										.orElseThrow(() -> new IllegalArgumentException(
												String.format("Unknown workstation: %s", workstation)
										)))
								.toList()
				).map(source).setWorkstations(new ArrayList<>());
			}
		});
		modelMapper.addMappings(new PropertyMap<ProfessionDTO, Profession>() {
			@Override
			protected void configure() {
				using((Converter<ProfessionDTO, Material>) context -> {
					final String fuel = context.getSource().getIcon();

					if(fuel == null) {
						return null;
					}
					if(!PluginUtils.isMaterial(fuel)) {
						throw new IllegalArgumentException(String.format("Invalid material for icon: %s", fuel));
					}

					return Material.valueOf(fuel.toUpperCase());
				}).map(source).setIcon(Material.STICK);
			}
		});
		modelMapper.typeMap(ProfessionDTO.class, Profession.class)
				.addMappings(mapper -> mapper.map(ProfessionDTO::getDisplayName, Profession::setDisplayName))
				.addMappings(mapper -> mapper.map(ProfessionDTO::getDescription, Profession::setDescription))
				.addMappings(mapper -> mapper.map(ProfessionDTO::getExampleCraftId, Profession::setExampleCraftId));
	}

	public Profession map(ProfessionDTO dto) {
		return modelMapper.map(dto, Profession.class);
	}

	public Map<String, Profession> map(Map<String, ProfessionDTO> professionDTOMap) {
		return professionDTOMap
				.entrySet()
				.stream()
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						entry -> modelMapper.map(entry.getValue(), Profession.class),
						(key, value) -> value,
						LinkedHashMap::new
				));
	}

	public static ProfessionMapper getInstance() {
		return INSTANCE;
	}

}
