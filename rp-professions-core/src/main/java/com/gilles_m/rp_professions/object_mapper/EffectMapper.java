package com.gilles_m.rp_professions.object_mapper;

import com.gilles_m.rp_professions.dto.EffectDTO;
import com.gilles_m.rp_professions.object.Effect;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

public abstract class EffectMapper<T extends Effect> {

	protected final ModelMapper modelMapper = new ModelMapper();

	protected final Class<T> dstClass;

	protected TypeMap<EffectDTO, T> baseTypeMap;

	protected EffectMapper(Class<T> dstClass) {
		this.dstClass = dstClass;

		configureMapper();
	}

	private void configureMapper() {
		this.baseTypeMap = modelMapper.createTypeMap(EffectDTO.class, dstClass)
				.addMappings(mapper -> mapper.map(EffectDTO::getDuration, Effect::setDuration))
				.addMappings(mapper -> mapper.map(EffectDTO::isSavedOnQuit, Effect::setSavedOnQuit))
				.addMappings(mapper -> mapper.map(EffectDTO::isPersistentThroughDeath, Effect::setPersistentThroughDeath));
	}

	public T map(EffectDTO dto) {
		return modelMapper.map(dto, dstClass);
	}

}
