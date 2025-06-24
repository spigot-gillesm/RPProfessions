package com.gilles_m.rp_professions.object_mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.gilles_m.rp_professions.PluginUtils;
import com.gilles_m.rp_professions.dto.ProfessionItemDTO;
import com.gilles_m.rp_professions.loader.ProfessionItemLoader;
import com.gilles_m.rp_professions.manager.ItemManager;
import com.github.spigot_gillesm.item_lib.ItemMapper;
import com.github.spigot_gillesm.item_lib.configuration.ItemConfiguration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Collectors;

public class ProfessionItemMapper extends ItemMapper {

	private final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

	private static final ProfessionItemMapper INSTANCE = new ProfessionItemMapper();

	private ProfessionItemMapper() {
		super();

		baseTypeMap.include(ProfessionItemDTO.class, ItemConfiguration.class);
		modelMapper.typeMap(ProfessionItemDTO.class, ItemConfiguration.class)
				.setProvider(request -> {
					final var itemConfiguration = new ItemConfiguration();
					modelMapper.map(request.getSource(), itemConfiguration);

					return itemConfiguration;
				});
	}

	/**
	 * Map the object to a new item stack. The given object must be an instance of Map< String, Object> or String.
	 *
	 * @param object the object to map
	 * @param objectName the object's name
	 *
	 * @return a new instance of ItemStack
	 */
	public ItemStack asItemStack(Object object, String objectName) {
		if(object == null) {
			return null;
		}
		//Check if the reagent is a full item configuration -> use the ItemMapper
		if(object instanceof Map) {
			final ProfessionItemDTO itemConfigurationDTO = objectMapper.convertValue(object, ProfessionItemDTO.class);

			return ProfessionItemMapper.getInstance().asItemStack(itemConfigurationDTO);
		//Otherwise, treat the reagent as a single entry (i.e. a single (amount = 1) item)
		} else if(object instanceof String itemId) {
			if(PluginUtils.isMaterial(itemId)) {
				return new ItemStack(Material.valueOf(itemId.toUpperCase()), 1);
			} else {
				return ProfessionItemMapper.getInstance().getExternalItem(itemId);
			}
		} else {
			throw new IllegalArgumentException(String.format("The %s must be either an item configuration section or a string", objectName));
		}
	}

	public ItemStack getExternalItem(@NotNull String item) {
		if(item.contains(":")) {
			final String sourceId = item.split(":")[0];
			final String itemId = item.split(":")[1];

			return ProfessionItemLoader.ItemSource.getSource(sourceId)
					.map(source -> source
							.retrieveItem(itemId)
							.orElseThrow(() -> new IllegalArgumentException(
									String.format("Error loading item. Unknown %s item: %s", source.getSourceName(),itemId)
							))
					).orElseThrow(() -> new IllegalArgumentException(
							String.format("Error loading item. Unknown item source: %s", sourceId)
					));
		} else {
			return ItemManager.getInstance()
					.get(item)
					.map(professionItem -> {
						final ItemStack itemStack = professionItem.getItemStack();
						itemStack.setAmount(1);

						return itemStack;
					})
					.orElseThrow(() -> new IllegalArgumentException(String.format("Error loading item. Invalid item id: %s", item)));
		}
	}

	/**
	 * Map the given ProfessionItemDTO to an item stack.
	 *
	 * @param professionItemDTO the DTO
	 *
	 * @return a new instance of ItemStack
	 */
	public ItemStack asItemStack(ProfessionItemDTO professionItemDTO) {
		final String item = professionItemDTO.getItem();

		//Check if the item comes from a dependency
		if(item != null && !item.isBlank()) {
			final int amount = professionItemDTO.getAmount();
			final ItemStack externalItem = getExternalItem(item).clone();

			if(amount > 0) {
				externalItem.setAmount(amount);
			}

			return externalItem;
		}

		//Otherwise, treat it as a simple item configuration
		return modelMapper.map(professionItemDTO, ItemConfiguration.class).toItemStack();
	}

	public Map<String, ItemStack> map(Map<String, ProfessionItemDTO> professionItemDTOMap) {
		return professionItemDTOMap
				.entrySet()
				.stream()
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						entry -> asItemStack(entry.getValue())
				));
	}

	public static ProfessionItemMapper getInstance() {
		return INSTANCE;
	}

}
