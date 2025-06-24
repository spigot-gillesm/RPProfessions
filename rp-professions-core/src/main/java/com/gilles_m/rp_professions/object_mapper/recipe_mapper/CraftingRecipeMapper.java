package com.gilles_m.rp_professions.object_mapper.recipe_mapper;

import com.gilles_m.rp_professions.dto.AbstractCraftingRecipeDTO;
import com.gilles_m.rp_professions.dto.crafting_recipe.CraftingRecipeDTO;
import com.gilles_m.rp_professions.object.crafting_recipe.crafting_recipe_impl.CraftingRecipe;
import com.gilles_m.rp_professions.object_mapper.AbstractRecipeMapper;
import org.bukkit.inventory.ItemStack;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;

public class CraftingRecipeMapper extends AbstractRecipeMapper<CraftingRecipe> {

	private static final CraftingRecipeMapper INSTANCE = new CraftingRecipeMapper();

	protected CraftingRecipeMapper() {
		super(CraftingRecipe.class);

		configureCraftingRecipeMapper();
	}

	private void configureCraftingRecipeMapper() {
		baseTypeMap.include(CraftingRecipeDTO.class, CraftingRecipe.class);
		modelMapper.typeMap(CraftingRecipeDTO.class, CraftingRecipe.class)
				.setProvider(request -> {
					final var craftingRecipe = new CraftingRecipe();
					modelMapper.map(request.getSource(), craftingRecipe);

					return craftingRecipe;
				})
				.addMappings(new PropertyMap<>() {
					@Override
					protected void configure() {
						using((Converter<CraftingRecipeDTO, ItemStack[]>) context ->
								asItemStackArray(context.getSource().getRecipe())
						).map(source).setPattern(new ItemStack[9]);
					}
				});
	}

	@Override
	protected CraftingRecipe map(AbstractCraftingRecipeDTO dto) {
		return modelMapper.map(dto, CraftingRecipe.class);
	}

	public static CraftingRecipeMapper getDefaultInstance() {
		return INSTANCE;
	}

}
