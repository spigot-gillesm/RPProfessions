package com.gilles_m.rp_professions.object_mapper.recipe_mapper;

import com.gilles_m.rp_professions.dto.AbstractCraftingRecipeDTO;
import com.gilles_m.rp_professions.dto.crafting_recipe.BrewingStandCraftingRecipeDTO;
import com.gilles_m.rp_professions.object.crafting_recipe.crafting_recipe_impl.BrewingStandCraftingRecipe;
import com.gilles_m.rp_professions.object_mapper.AbstractRecipeMapper;
import com.gilles_m.rp_professions.object_mapper.ProfessionItemMapper;
import org.bukkit.inventory.ItemStack;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;

public class BrewingStandRecipeMapper extends AbstractRecipeMapper<BrewingStandCraftingRecipe> {

	private static final BrewingStandRecipeMapper INSTANCE = new BrewingStandRecipeMapper();

	private BrewingStandRecipeMapper() {
		super(BrewingStandCraftingRecipe.class);

		configureBrewingRecipeMapper();
	}

	private void configureBrewingRecipeMapper() {
		baseTypeMap.include(BrewingStandCraftingRecipeDTO.class, BrewingStandCraftingRecipe.class);
		modelMapper.typeMap(BrewingStandCraftingRecipeDTO.class, BrewingStandCraftingRecipe.class)
				.setProvider(request -> {
					final var brewingStandCraftingRecipe = new BrewingStandCraftingRecipe();
					modelMapper.map(request.getSource(), brewingStandCraftingRecipe);

					return brewingStandCraftingRecipe;
				})
				.addMappings(new PropertyMap<>() {
					@Override
					protected void configure() {
						using((Converter<BrewingStandCraftingRecipeDTO, ItemStack>) context ->
								ProfessionItemMapper.getInstance().asItemStack(context.getSource().getFuel(), "fuel")
						).map(source).setFuel(null);
					}
				})
				.addMappings(new PropertyMap<>() {
					@Override
					protected void configure() {
						using((Converter<BrewingStandCraftingRecipeDTO, ItemStack>) context ->
								ProfessionItemMapper.getInstance().asItemStack(context.getSource().getReceptacle(), "receptacle")
						).map(source).setReceptacle(null);
					}
				})
				.addMappings(mapper -> mapper.map(BrewingStandCraftingRecipeDTO::isFuelConsumed, BrewingStandCraftingRecipe::setFuelConsumed));
	}

	@Override
	protected BrewingStandCraftingRecipe map(AbstractCraftingRecipeDTO dto) {
		return modelMapper.map(dto, BrewingStandCraftingRecipe.class);
	}

	public static BrewingStandRecipeMapper getInstance() {
		return INSTANCE;
	}

}
