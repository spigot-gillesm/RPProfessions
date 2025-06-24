package com.gilles_m.rp_professions.object_mapper;

import com.gilles_m.rp_professions.dto.VillagerTradeSetDTO;
import com.gilles_m.rp_professions.object.randomized_object.RangeInteger;
import com.gilles_m.rp_professions.object.VillagerTradeSet;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

public class VillagerTradeSetMapper {

	private static final VillagerTradeSetMapper INSTANCE = new VillagerTradeSetMapper();

	protected final ModelMapper modelMapper = new ModelMapper();

	private VillagerTradeSetMapper() {
		configureMapper();
	}

	private void configureMapper() {
		modelMapper.addMappings(new PropertyMap<VillagerTradeSetDTO, VillagerTradeSet>() {
			@Override
			protected void configure() {
				using((Converter<VillagerTradeSetDTO, RangeInteger>) context ->
						RangeInteger.fromString(context.getSource().getTradesPerLevel(), 1)
				).map(source).setTradesPerLevel(new RangeInteger(1));
			}
		});
	}

	public VillagerTradeSet map(VillagerTradeSetDTO dto) {
		return modelMapper.map(dto, VillagerTradeSet.class);
	}

	public static VillagerTradeSetMapper getInstance() {
		return INSTANCE;
	}

}
