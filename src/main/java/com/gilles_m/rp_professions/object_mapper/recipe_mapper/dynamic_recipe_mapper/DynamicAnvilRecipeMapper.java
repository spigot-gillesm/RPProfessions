package com.gilles_m.rp_professions.object_mapper.recipe_mapper.dynamic_recipe_mapper;

import com.gilles_m.rp_professions.dto.AbstractCraftingRecipeDTO;
import com.gilles_m.rp_professions.dto.crafting_recipe.dynamic_recipe.DynamicAnvilCraftingRecipeDTO;
import com.gilles_m.rp_professions.object.crafting_recipe.dynamic_crafting_recipe_impl.DynamicAnvilCraftingRecipe;
import com.gilles_m.rp_professions.object_mapper.AbstractRecipeMapper;
import org.bukkit.inventory.ItemStack;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;

public class DynamicAnvilRecipeMapper extends AbstractRecipeMapper<DynamicAnvilCraftingRecipe> {

	private static final DynamicAnvilRecipeMapper INSTANCE = new DynamicAnvilRecipeMapper();

	private DynamicAnvilRecipeMapper() {
		super(DynamicAnvilCraftingRecipe.class);

		configureAnvilRecipeMapper();
	}

	private void configureAnvilRecipeMapper() {
		baseTypeMap.include(DynamicAnvilCraftingRecipeDTO.class, DynamicAnvilCraftingRecipe.class);
		modelMapper.typeMap(DynamicAnvilCraftingRecipeDTO.class, DynamicAnvilCraftingRecipe.class)
				.setProvider(request -> {
					final var craftingRecipe = new DynamicAnvilCraftingRecipe();
					modelMapper.map(request.getSource(), craftingRecipe);

					return craftingRecipe;
				})
				.addMappings(new PropertyMap<>() {
					@Override
					protected void configure() {
						using((Converter<DynamicAnvilCraftingRecipeDTO, ItemStack[]>) context ->
								asItemStackArray(context.getSource().getRecipe())
						).map(source).setPattern(new ItemStack[9]);
					}
				})
				.addMappings(mapper -> mapper.map(DynamicAnvilCraftingRecipeDTO::getRequiredHammering, DynamicAnvilCraftingRecipe::setRequiredHammering))
				.addMappings(mapper -> mapper.map(DynamicAnvilCraftingRecipeDTO::getMistakeCost, DynamicAnvilCraftingRecipe::setMistakeCost))
				.addMappings(mapper -> mapper.map(DynamicAnvilCraftingRecipeDTO::getTimeLaps, DynamicAnvilCraftingRecipe::setTimeLaps));
	}

	@Override
	protected DynamicAnvilCraftingRecipe map(AbstractCraftingRecipeDTO dto) {
		return modelMapper.map(dto, DynamicAnvilCraftingRecipe.class);
	}

	public static DynamicAnvilRecipeMapper getInstance() {
		return INSTANCE;
	}

}
