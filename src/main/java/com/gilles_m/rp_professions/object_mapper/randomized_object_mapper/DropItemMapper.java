package com.gilles_m.rp_professions.object_mapper.randomized_object_mapper;

import com.gilles_m.rp_professions.dto.randomized_object.DropItemDTO;
import com.gilles_m.rp_professions.object.randomized_object.DropItem;
import com.gilles_m.rp_professions.object.randomized_object.RangeInteger;
import com.gilles_m.rp_professions.object_mapper.RandomizedObjectMapper;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;

public class DropItemMapper extends RandomizedObjectMapper<DropItem> {

	private static final DropItemMapper INSTANCE = new DropItemMapper();

	public DropItemMapper() {
		super(DropItem.class);

		configureDropItemMapper();
	}

	private void configureDropItemMapper() {
		baseTypeMap.include(DropItemDTO.class, DropItem.class);
		modelMapper.typeMap(DropItemDTO.class, DropItem.class)
				.setProvider(request -> {
					final var dropItem = new DropItem();
					modelMapper.map(request.getSource(), dropItem);

					return dropItem;
				})
				.addMappings(new PropertyMap<>() {
					@Override
					protected void configure() {
						using((Converter<DropItemDTO, RangeInteger>) context ->
								RangeInteger.fromString(context.getSource().getAmount(), 1)
						).map(source).setAmount(new RangeInteger(1));
					}
				});
	}

	public DropItem map(DropItemDTO dto) {
		return modelMapper.map(dto, DropItem.class);
	}

	public static DropItemMapper getInstance() {
		return INSTANCE;
	}

}
