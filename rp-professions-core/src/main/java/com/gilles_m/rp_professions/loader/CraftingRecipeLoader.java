package com.gilles_m.rp_professions.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gilles_m.rp_professions.dto.AbstractCraftingRecipeDTO;
import com.gilles_m.rp_professions.dto.CraftingRecipeMetaDTO;
import com.gilles_m.rp_professions.manager.CraftingRecipeManager;
import com.gilles_m.rp_professions.manager.ItemManager;
import com.gilles_m.rp_professions.manager.ProfessionManager;
import com.gilles_m.rp_professions.manager.WorkstationManager;
import com.gilles_m.rp_professions.object.Profession;
import com.gilles_m.rp_professions.object.ProfessionItem;
import com.gilles_m.rp_professions.object.crafting_recipe.AbstractCraftingRecipe;
import com.gilles_m.rp_professions.object.crafting_recipe.CraftingRecipeMeta;
import com.gilles_m.rp_professions.object.station.Workstation;
import com.gilles_m.rp_professions.object_mapper.AbstractRecipeMapper;
import com.gilles_m.rp_professions.object_mapper.recipe_mapper.CraftingRecipeMapper;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class CraftingRecipeLoader extends ObjectLoader<AbstractCraftingRecipe> {

	private static final CraftingRecipeLoader INSTANCE = new CraftingRecipeLoader();

	private static final String FILE_PATH = "items/";

	private CraftingRecipeLoader() {
		super(CraftingRecipeManager.getInstance(), FILE_PATH, "recipe.s");

		setLoaderIterable(new LoaderIterable.DoubleMapIterable<AbstractCraftingRecipe, AbstractCraftingRecipeDTO, CraftingRecipeMetaDTO>() {

			@Override
			@SuppressWarnings("unchecked")
			protected Map<String, AbstractCraftingRecipeDTO> initMapA() throws IOException {
				final LinkedHashMap<String, Object> objectMap = objectMapper.readValue(file, new TypeReference<>() {});
				final Map<String, AbstractCraftingRecipeDTO> craftingRecipeDTOMap = new LinkedHashMap<>();

				//Only convert maps
				objectMap.forEach((key, value) -> {
					if(value instanceof Map) {
						//Convert values to class-specific DTOs
						buildRecipeDTO((Map<String, Object>) value).ifPresent(dto -> craftingRecipeDTOMap.put(key, dto));
					}
				});


				return craftingRecipeDTOMap;
			}

			@Override
			protected Map<String, CraftingRecipeMetaDTO> initMapB() throws IOException {
				final LinkedHashMap<String, Object> objectMap = objectMapper.readValue(file, new TypeReference<>() {});
				final Map<String, CraftingRecipeMetaDTO> craftingRecipeMetaDTOMap = new LinkedHashMap<>();

				//Only convert maps
				objectMap.forEach((key, value) -> {
					if(value instanceof Map) {
						//Convert values to the non-specific, abstract DTO class to hold the universal values
						craftingRecipeMetaDTOMap.put(key, objectMapper.convertValue(value, CraftingRecipeMetaDTO.class));
					}
				});


				return craftingRecipeMetaDTOMap;
			}

			@Override
			protected AbstractCraftingRecipe buildObject(String id, AbstractCraftingRecipeDTO recipeDTO, CraftingRecipeMetaDTO metaDTO) {
				//Check if the item has no recipe linked to it
				final String professionId = file.getName().split("\\.")[0];
				final Optional<Profession> profession = ProfessionManager.getInstance().get(professionId);

				if(profession.isEmpty()) {
					throw new IllegalArgumentException(String.format("Unknown profession: %s", professionId));
				}
				final String workstationId = recipeDTO.getWorkstation();

				//Check if the item has a recipe (= the workstation is defined)
				if(workstationId == null) {
					return null;
				}

				return WorkstationManager.getInstance()
						.get(workstationId)
						.map(workstation -> {
							//Map the DTO to the exact recipe implementation
							//At that point, the recipe DTO is already "class-exact"
							final AbstractCraftingRecipe craftingRecipe = AbstractRecipeMapper.mapExact(recipeDTO);
							//Then add the missing data
							final CraftingRecipeMeta metadata = CraftingRecipeMapper.getDefaultInstance()
									.getModelMapper()
									.map(metaDTO, CraftingRecipeMeta.class);
							craftingRecipe.setId(id);
							craftingRecipe.setMetaData(metadata);
							craftingRecipe.setProfession(profession.get());
							craftingRecipe.setResult(ItemManager.getInstance()
									.get(id)
									.map(ProfessionItem::getItemStack)
									.orElseThrow(() -> new IllegalArgumentException("Cannot set recipe's result. Did the recipe's item fail loading?")));

							return craftingRecipe;

						})
						//At that point, the workstation is valid (previously checked in buildRecipeDTO)
						.orElseThrow(() -> new IllegalArgumentException(String.format("Unknown workstation: %s", workstationId)));
			}
		});
	}

	private Optional<AbstractCraftingRecipeDTO> buildRecipeDTO(Map<String, Object> map) {
		final AbstractCraftingRecipeDTO recipeDTO = objectMapper.convertValue(map, AbstractCraftingRecipeDTO.class);
		final String workstationId = recipeDTO.getWorkstation();

		//Check if the item has a recipe (= the workstation is defined)
		if(workstationId == null) {
			return Optional.empty();
		}
		final Optional<Workstation> workstation = WorkstationManager.getInstance().get(workstationId);

		//If no workstation matches, return a non-specific dto
		if(workstation.isEmpty()) {
			return Optional.of(recipeDTO);
		} else {
			//Get the exact dto class in order to convert the map to the right DTO
			final Class<? extends AbstractCraftingRecipeDTO> dtoClass = workstation.get().getCraftRecipeDTOClass(recipeDTO.isDynamic());

			return Optional.of(objectMapper.convertValue(map, dtoClass));
		}
	}

	public static CraftingRecipeLoader getInstance() {
		return INSTANCE;
	}

}
