package com.gilles_m.rp_professions.object_mapper.randomized_object_mapper;

import com.gilles_m.rp_professions.dto.randomized_object.VillagerTradeDTO;
import com.gilles_m.rp_professions.object.randomized_object.VillagerTrade;
import com.gilles_m.rp_professions.object_mapper.ProfessionItemMapper;
import com.gilles_m.rp_professions.object_mapper.RandomizedObjectMapper;
import org.bukkit.inventory.ItemStack;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;

public class VillagerTradeMapper extends RandomizedObjectMapper<VillagerTrade> {

	private static final VillagerTradeMapper INSTANCE = new VillagerTradeMapper();

	public VillagerTradeMapper() {
		super(VillagerTrade.class);

		configureTraderMapper();
	}

	private void configureTraderMapper() {
		baseTypeMap.include(VillagerTradeDTO.class, VillagerTrade.class);
		modelMapper.typeMap(VillagerTradeDTO.class, VillagerTrade.class)
				.setProvider(request -> {
					final var villagerTrade = new VillagerTrade();
					modelMapper.map(request.getSource(), villagerTrade);

					return villagerTrade;
				})
				.addMappings(new PropertyMap<>() {
					@Override
					protected void configure() {
						using((Converter<VillagerTradeDTO, ItemStack>) context ->
								ProfessionItemMapper.getInstance().asItemStack(context.getSource().getResult(), "result")
						).map(source).setResult(null);
					}
				})
				.addMappings(new PropertyMap<>() {
					@Override
					protected void configure() {
						using((Converter<VillagerTradeDTO, ItemStack>) context ->
								ProfessionItemMapper.getInstance().asItemStack(context.getSource().getFirstIngredient(), "first ingredient")
						).map(source).setFirstIngredient(null);
					}
				})
				.addMappings(new PropertyMap<>() {
					@Override
					protected void configure() {
						using((Converter<VillagerTradeDTO, ItemStack>) context ->
								ProfessionItemMapper.getInstance().asItemStack(context.getSource().getSecondIngredient(), "second ingredient")
						).map(source).setSecondIngredient(null);
					}
				})
				.addMappings(mapper -> mapper.map(VillagerTradeDTO::getLevel, VillagerTrade::setLevel))
				.addMappings(mapper -> mapper.map(VillagerTradeDTO::getMaxUse, VillagerTrade::setMaxUse))
				.addMappings(mapper -> mapper.map(VillagerTradeDTO::getVillagerExperienceReward, VillagerTrade::setVillagerExperienceReward))
				.addMappings(mapper -> mapper.map(VillagerTradeDTO::isExperienceReward, VillagerTrade::setExperienceReward));
	}

	public VillagerTrade map(VillagerTradeDTO dto) {
		return modelMapper.map(dto, VillagerTrade.class);
	}

	public static VillagerTradeMapper getInstance() {
		return INSTANCE;
	}

}
