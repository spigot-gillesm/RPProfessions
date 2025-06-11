package com.gilles_m.rp_professions.object_mapper.effect;

import com.gilles_m.rp_professions.dto.effect.MMOItemEffectDTO;
import com.gilles_m.rp_professions.object.effect.MMOItemEffect;
import com.gilles_m.rp_professions.object_mapper.EffectMapper;
import io.lumine.mythic.lib.api.stat.modifier.StatModifier;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;

import java.util.ArrayList;
import java.util.List;

public class MMOItemEffectMapper extends EffectMapper<MMOItemEffect> {

	private static final MMOItemEffectMapper INSTANCE = new MMOItemEffectMapper();

	private MMOItemEffectMapper() {
		super(MMOItemEffect.class);

		configureMMOEffectMapper();
	}

	private void configureMMOEffectMapper() {
		baseTypeMap.include(MMOItemEffectDTO.class, MMOItemEffect.class);
		modelMapper.typeMap(MMOItemEffectDTO.class, MMOItemEffect.class)
				.setProvider(request -> {
					final var mmoItemEffect = new MMOItemEffect();
					modelMapper.map(request.getSource(), mmoItemEffect);

					return mmoItemEffect;
				})
				.addMappings(new PropertyMap<>() {
					@Override
					protected void configure() {
						//Convert the map of string to double to actual mmo item effects
						using((Converter<MMOItemEffectDTO, List<StatModifier>>) context -> {
							final String mmoId = context.getSource().getMmoId();

							return context.getSource()
									.getStatModifiers()
									.entrySet()
									.stream()
									.map(entry -> new StatModifier(mmoId, entry.getKey(), entry.getValue()))
									.toList();
						}).map(source).setStatModifiers(new ArrayList<>());
					}
				})
				.addMappings(mapper -> mapper.map(MMOItemEffectDTO::getMmoId, MMOItemEffect::setMmoId));
	}

	public static MMOItemEffectMapper getInstance() {
		return INSTANCE;
	}

}
