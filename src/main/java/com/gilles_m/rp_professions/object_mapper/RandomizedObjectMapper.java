package com.gilles_m.rp_professions.object_mapper;

import com.gilles_m.rp_professions.dto.RandomizedObjectDTO;
import com.gilles_m.rp_professions.object.randomized_object.RandomizedObject;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;

public class RandomizedObjectMapper<T extends RandomizedObject> {

	private static final String ERROR_MESSAGE = "Chance must be either 'ALWAYS' or a strictly positive number";

	protected final ModelMapper modelMapper = new ModelMapper();

	protected final Class<T> dstClass;

	protected TypeMap<RandomizedObjectDTO, T> baseTypeMap;

	protected RandomizedObjectMapper(Class<T> dstClass) {
		this.dstClass = dstClass;

		configureMapper();
	}

	private double getValue(String chance) {
		if("ALWAYS".equals(chance)) {
			return 1.0;
		}
		try {
			final double value = Double.parseDouble(chance);

			if(value <= 0) {
				throw new IllegalArgumentException(ERROR_MESSAGE);
			}

			return value;
		} catch (final IllegalArgumentException exception) {
			throw new IllegalArgumentException(ERROR_MESSAGE);
		}
	}

	private void configureMapper() {
		this.baseTypeMap = modelMapper.createTypeMap(RandomizedObjectDTO.class, dstClass)
				.addMappings(new PropertyMap<>() {

					@Override
					protected void configure() {
						using((Converter<RandomizedObjectDTO, Double>) context ->
								getValue(context.getSource().getChance())).map(source).setChance(1);
					}

				})
				.addMappings(new PropertyMap<>() {

					@Override
					protected void configure() {
						using((Converter<RandomizedObjectDTO, RandomizedObject.Type>) context -> {
							final String chance = context.getSource().getChance();

							if("ALWAYS".equals(chance)) {
								return RandomizedObject.Type.ABSOLUTE;
							}
							final double value = getValue(chance);

							if(value <= 0) {
								throw new IllegalArgumentException(ERROR_MESSAGE);
							}
							//Check if the value is a proportion
							if(value < 1) {
								return RandomizedObject.Type.ABSOLUTE;
							//Or a weight
							} else {
								return RandomizedObject.Type.RELATIVE;
							}
						}).map(source).setType(RandomizedObject.Type.RELATIVE);
					}

				});
	}

}
