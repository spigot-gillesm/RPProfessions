package com.gilles_m.rp_professions.object_mapper.recipe_mapper.dynamic_recipe_mapper;

import com.gilles_m.rp_professions.dto.AbstractCraftingRecipeDTO;
import com.gilles_m.rp_professions.dto.crafting_recipe.dynamic_recipe.DynamicForgeCraftingRecipeDTO;
import com.gilles_m.rp_professions.object.crafting_recipe.dynamic_crafting_recipe_impl.DynamicForgeCraftingRecipe;
import com.gilles_m.rp_professions.object_mapper.AbstractRecipeMapper;
import org.bukkit.inventory.ItemStack;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;

public class DynamicForgeRecipeMapper extends AbstractRecipeMapper<DynamicForgeCraftingRecipe> {

	private static final DynamicForgeRecipeMapper INSTANCE = new DynamicForgeRecipeMapper();

	private DynamicForgeRecipeMapper() {
		super(DynamicForgeCraftingRecipe.class);

		configureForgeRecipeMapper();
	}

	private void configureForgeRecipeMapper() {
		baseTypeMap.include(DynamicForgeCraftingRecipeDTO.class, DynamicForgeCraftingRecipe.class);
		modelMapper.typeMap(DynamicForgeCraftingRecipeDTO.class, DynamicForgeCraftingRecipe.class)
				.setProvider(request -> {
					final var craftingRecipe = new DynamicForgeCraftingRecipe();
					modelMapper.map(request.getSource(), craftingRecipe);

					return craftingRecipe;
				})
				.addMappings(new PropertyMap<>() {
					@Override
					protected void configure() {
						using((Converter<DynamicForgeCraftingRecipeDTO, ItemStack[]>) context ->
								asItemStackArray(context.getSource().getRecipe())
						).map(source).setPattern(new ItemStack[9]);
					}
				})
				.addMappings(mapper -> mapper.map(DynamicForgeCraftingRecipeDTO::getRequiredHeat, DynamicForgeCraftingRecipe::setRequiredHeat))
				.addMappings(mapper -> mapper.map(DynamicForgeCraftingRecipeDTO::getTolerance, DynamicForgeCraftingRecipe::setTolerance))
				.addMappings(mapper -> mapper.map(DynamicForgeCraftingRecipeDTO::getTime, DynamicForgeCraftingRecipe::setTime))
				.addMappings(mapper -> mapper.map(DynamicForgeCraftingRecipeDTO::getChangeChance, DynamicForgeCraftingRecipe::setChangeChance));
	}

	@Override
	protected DynamicForgeCraftingRecipe map(AbstractCraftingRecipeDTO dto) {
		return modelMapper.map(dto, DynamicForgeCraftingRecipe.class);
	}

	public static DynamicForgeRecipeMapper getInstance() {
		return INSTANCE;
	}

}
