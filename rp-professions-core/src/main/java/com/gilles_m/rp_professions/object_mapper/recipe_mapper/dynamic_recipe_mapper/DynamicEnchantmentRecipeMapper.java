package com.gilles_m.rp_professions.object_mapper.recipe_mapper.dynamic_recipe_mapper;

import com.gilles_m.rp_professions.dto.AbstractCraftingRecipeDTO;
import com.gilles_m.rp_professions.dto.crafting_recipe.dynamic_recipe.DynamicEnchantmentCraftingRecipeDTO;
import com.gilles_m.rp_professions.object.crafting_recipe.dynamic_crafting_recipe_impl.DynamicEnchantmentCraftingRecipe;
import com.gilles_m.rp_professions.object_mapper.AbstractRecipeMapper;
import org.bukkit.inventory.ItemStack;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;

public class DynamicEnchantmentRecipeMapper extends AbstractRecipeMapper<DynamicEnchantmentCraftingRecipe> {

	private static final DynamicEnchantmentRecipeMapper INSTANCE = new DynamicEnchantmentRecipeMapper();

	private DynamicEnchantmentRecipeMapper() {
		super(DynamicEnchantmentCraftingRecipe.class);

		configureAnvilRecipeMapper();
	}

	private void configureAnvilRecipeMapper() {
		baseTypeMap.include(DynamicEnchantmentCraftingRecipeDTO.class, DynamicEnchantmentCraftingRecipe.class);
		modelMapper.typeMap(DynamicEnchantmentCraftingRecipeDTO.class, DynamicEnchantmentCraftingRecipe.class)
				.setProvider(request -> {
					final var craftingRecipe = new DynamicEnchantmentCraftingRecipe();
					modelMapper.map(request.getSource(), craftingRecipe);

					return craftingRecipe;
				})
				.addMappings(new PropertyMap<>() {
					@Override
					protected void configure() {
						using((Converter<DynamicEnchantmentCraftingRecipeDTO, ItemStack[]>) context ->
								asItemStackArray(context.getSource().getRecipe())
						).map(source).setPattern(new ItemStack[9]);
					}
				})
				.addMappings(mapper -> mapper.map(DynamicEnchantmentCraftingRecipeDTO::getTimeLaps, DynamicEnchantmentCraftingRecipe::setTimeLaps))
				.addMappings(mapper -> mapper.map(DynamicEnchantmentCraftingRecipeDTO::getMistakeCost, DynamicEnchantmentCraftingRecipe::setMistakeCost))
				.addMappings(mapper -> mapper.map(DynamicEnchantmentCraftingRecipeDTO::getTimeLaps, DynamicEnchantmentCraftingRecipe::setTimeLaps));
	}

	@Override
	protected DynamicEnchantmentCraftingRecipe map(AbstractCraftingRecipeDTO dto) {
		return modelMapper.map(dto, DynamicEnchantmentCraftingRecipe.class);
	}

	public static DynamicEnchantmentRecipeMapper getInstance() {
		return INSTANCE;
	}
	
}
