package com.gilles_m.rp_professions.object_mapper;

import com.gilles_m.rp_professions.dto.AbstractCraftingRecipeDTO;
import com.gilles_m.rp_professions.dto.CraftingRecipeMetaDTO;
import com.gilles_m.rp_professions.dto.crafting_recipe.BrewingStandCraftingRecipeDTO;
import com.gilles_m.rp_professions.dto.crafting_recipe.CraftingRecipeDTO;
import com.gilles_m.rp_professions.dto.crafting_recipe.dynamic_recipe.DynamicAnvilCraftingRecipeDTO;
import com.gilles_m.rp_professions.dto.crafting_recipe.dynamic_recipe.DynamicEnchantmentCraftingRecipeDTO;
import com.gilles_m.rp_professions.dto.crafting_recipe.dynamic_recipe.DynamicForgeCraftingRecipeDTO;
import com.gilles_m.rp_professions.manager.CategoryManager;
import com.gilles_m.rp_professions.manager.WorkstationManager;
import com.gilles_m.rp_professions.object.Category;
import com.gilles_m.rp_professions.object.crafting_recipe.AbstractCraftingRecipe;
import com.gilles_m.rp_professions.object.crafting_recipe.CraftingRecipeMeta;
import com.gilles_m.rp_professions.object.station.Workstation;
import com.gilles_m.rp_professions.object_mapper.recipe_mapper.BrewingStandRecipeMapper;
import com.gilles_m.rp_professions.object_mapper.recipe_mapper.CraftingRecipeMapper;
import com.gilles_m.rp_professions.object_mapper.recipe_mapper.dynamic_recipe_mapper.DynamicAnvilRecipeMapper;
import com.gilles_m.rp_professions.object_mapper.recipe_mapper.dynamic_recipe_mapper.DynamicEnchantmentRecipeMapper;
import com.gilles_m.rp_professions.object_mapper.recipe_mapper.dynamic_recipe_mapper.DynamicForgeRecipeMapper;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRecipeMapper<T extends AbstractCraftingRecipe> {

	protected static final Map<Class<? extends AbstractCraftingRecipeDTO>, AbstractRecipeMapper<?>> REGISTERED_MAPPERS = new HashMap<>();

	@Getter
	protected final ModelMapper modelMapper = new ModelMapper();

	protected final Class<T> dstClass;

	protected TypeMap<AbstractCraftingRecipeDTO, T> baseTypeMap;

	protected AbstractRecipeMapper(Class<T> dstClass) {

		this.dstClass = dstClass;

		configureMetadataMapper();
		configureMapper();
	}

	protected ItemStack[] asItemStackArray(Map<Integer, Object> recipeData) {
		final ItemStack[] pattern = new ItemStack[9];

		for(int i = 0; i < 9; i++) {
			final Object recipeElement = recipeData.get(i);

			if(recipeElement == null) {
				continue;
			}
			pattern[i] = ProfessionItemMapper.getInstance().asItemStack(recipeElement, "recipe element");
		}

		return pattern;
	}

	private void configureMapper() {
		this.baseTypeMap = modelMapper.createTypeMap(AbstractCraftingRecipeDTO.class, dstClass)
				.addMappings(new PropertyMap<>() {
					@Override
					protected void configure() {
						using((Converter<AbstractCraftingRecipeDTO, Category>) context ->
								CategoryManager.getInstance()
										.get(context.getSource().getCategory())
										.orElseThrow(() -> new IllegalArgumentException(
												String.format("Unknown category: %s", context.getSource().getCategory())
												)
										)
						).map(source).setCategory(null);
					}
				})
				.addMappings(new PropertyMap<>() {
					@Override
					protected void configure() {
						using((Converter<AbstractCraftingRecipeDTO, Workstation>) context ->
								WorkstationManager.getInstance()
										.get(context.getSource().getWorkstation())
										.orElseThrow(() -> new IllegalArgumentException(
														String.format("Unknown workstation: %s", context.getSource().getWorkstation())
												)
										)
						).map(source).setWorkstation(null);
					}
				})
				.addMappings(new PropertyMap<>() {
					@Override
					protected void configure() {
						using((Converter<AbstractCraftingRecipeDTO, ItemStack>) context ->
								ProfessionItemMapper.getInstance().asItemStack(context.getSource().getReagent(), "reagent")
						).map(source).setReagent(null);
					}
				});
	}

	private void configureMetadataMapper() {
		modelMapper.createTypeMap(CraftingRecipeMetaDTO.class, CraftingRecipeMeta.class);
	}

	protected abstract T map(AbstractCraftingRecipeDTO dto);

	public static AbstractCraftingRecipe mapExact(AbstractCraftingRecipeDTO dto) {
		if(REGISTERED_MAPPERS.isEmpty()) {
			REGISTERED_MAPPERS.put(DynamicAnvilCraftingRecipeDTO.class, DynamicAnvilRecipeMapper.getInstance());
			REGISTERED_MAPPERS.put(DynamicForgeCraftingRecipeDTO.class, DynamicForgeRecipeMapper.getInstance());
			REGISTERED_MAPPERS.put(DynamicEnchantmentCraftingRecipeDTO.class, DynamicEnchantmentRecipeMapper.getInstance());
			REGISTERED_MAPPERS.put(CraftingRecipeDTO.class, CraftingRecipeMapper.getDefaultInstance());
			REGISTERED_MAPPERS.put(BrewingStandCraftingRecipeDTO.class, BrewingStandRecipeMapper.getInstance());
		}

		return REGISTERED_MAPPERS.get(dto.getClass()).map(dto);
	}

}
