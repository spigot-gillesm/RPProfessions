package com.gilles_m.rp_professions.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gilles_m.rp_professions.dto.ProfessionItemDTO;
import com.gilles_m.rp_professions.manager.DependencyManager;
import com.gilles_m.rp_professions.manager.ItemManager;
import com.gilles_m.rp_professions.manager.PatternManager;
import com.gilles_m.rp_professions.object.ProfessionItem;
import com.gilles_m.rp_professions.object.RecipePattern;
import com.gilles_m.rp_professions.object_mapper.ProfessionItemMapper;
import com.gilles_m.rp_professions.updater.ProfessionItemUpdater;
import com.github.spigot_gillesm.item_lib.ItemUtil;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class ProfessionItemLoader extends ObjectLoader<ProfessionItem> {

	private static final ProfessionItemLoader INSTANCE = new ProfessionItemLoader();

	private static final String FILE_PATH = "items/";

	private ProfessionItemLoader() {
		super(ItemManager.getInstance(), FILE_PATH, ProfessionItemUpdater.getInstance(), "item.s");

		setLoaderIterable(new LoaderIterable<ProfessionItem, ProfessionItemDTO>() {

			@Override
			protected Map<String, ProfessionItemDTO> initMap() throws IOException {
				final LinkedHashMap<String, Object> objectMap = objectMapper.readValue(file, new TypeReference<>() {});
				final Map<String, ProfessionItemDTO> dtoMap = new LinkedHashMap<>();

				objectMap.forEach((key, value) -> {
					if(value instanceof Map) {
						dtoMap.put(key, objectMapper.convertValue(value, ProfessionItemDTO.class));
					}
				});

				return dtoMap;
			}

			@Override
			protected ProfessionItem buildObject(final String id, ProfessionItemDTO dto) {
				final ItemStack itemStack = ProfessionItemMapper.getInstance().asItemStack(dto);
				ItemUtil.setPersistentString(itemStack, ItemManager.KEY_ID, id);

				return new ProfessionItem(id, itemStack);
			}

		});
	}

	public static ProfessionItemLoader getInstance() {
		return INSTANCE;
	}

	public enum ItemSource {

		PATTERN("pattern", "RPProfessions pattern",
				id -> PatternManager.getInstance().get(id).map(RecipePattern::getPhysicalPattern)),

		MMO_ITEMS("mi", "MMOItems", id -> DependencyManager.getInstance().getMmoItem(id)),

		MYTHIC_MOBS("mm", "MythicMobs", id -> DependencyManager.getInstance().getMythicMobsItem(id)),

		ITEMS_ADDER("ia", "ItemsAdder", id -> DependencyManager.getInstance().getItemsAdderItem(id)),

		ORAXEN("oraxen", "Oraxen", id -> DependencyManager.getInstance().getOraxenItem(id));

		private final String sourceId;

		@Getter
		private final String sourceName;

		private final Function<String, Optional<ItemStack>> retriever;

		ItemSource(String sourceId, String sourceName, Function<String, Optional<ItemStack>> retriever) {
			this.sourceId = sourceId;
			this.sourceName = sourceName;
			this.retriever = retriever;
		}

		public Optional<ItemStack> retrieveItem(String itemId) {
			return retriever.apply(itemId)
					.map(itemStack -> {
						//Do not alter the item
						final ItemStack clone = itemStack.clone();
						clone.setAmount(1);

						return clone;
					});
		}

		/**
		 * Get the ItemSource matching the given identifier.
		 *
		 * @param sourceId the source id
		 * @return the ItemSource or an empty optional
		 */
		public static Optional<ItemSource> getSource(String sourceId) {
			return Arrays.stream(ItemSource.values())
					.filter(itemSource -> itemSource.sourceId.equals(sourceId))
					.findFirst();
		}

	}

}
