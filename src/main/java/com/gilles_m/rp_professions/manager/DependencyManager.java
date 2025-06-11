package com.gilles_m.rp_professions.manager;

import com.gilles_m.rp_professions.papi.PAPIExpansion;
import com.github.spigot_gillesm.format_lib.Formatter;
import dev.lone.itemsadder.api.CustomStack;
import io.lumine.mythic.api.MythicPlugin;
import io.lumine.mythic.api.MythicProvider;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.th0rgal.oraxen.api.OraxenBlocks;
import io.th0rgal.oraxen.api.OraxenFurniture;
import io.th0rgal.oraxen.api.OraxenItems;
import lombok.Getter;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public class DependencyManager {

	private static final DependencyManager INSTANCE = new DependencyManager();

	private MMOItems mmoItems;

	@Getter
	private boolean mmoItemsPresent = false;

	private MythicPlugin mythicMobs;

	@Getter
	private boolean mythicMobsPresent = false;

	private boolean itemsAdderPresent = false;

	private boolean oraxenPresent = false;

	public void load() {
		Formatter.info("Loading dependencies...");
		loadMmoItems();
		loadMythicMobs();
		loadItemsAdder();
		loadOraxen();
		loadPAPI();
		Formatter.info("&aDone!");
	}

	//Method used to avoid null checks everywhere in the class
	private <T> Optional<T> retrieveFromAPI(Supplier<Optional<T>> supplier) {
		try {
			return supplier.get();
		} catch (final NullPointerException exception) {
			return Optional.empty();
		}
	}

	public Optional<MMOPlayerData> getPlayerMMOStats(@NotNull Player player) {
		return retrieveFromAPI(() -> Optional.of(MMOPlayerData.get(player.getUniqueId())));
	}

	public Optional<ItemStack> getMmoItem(@NotNull String id) {
		if(!mmoItemsPresent) {
			return Optional.empty();
		}
		return retrieveFromAPI(() -> {
			for(final Type type : mmoItems.getTypes().getAll()) {
				final MMOItem mmoItem = mmoItems.getMMOItem(type, id);

					if(mmoItem != null) {
						final ItemStack itemStack = mmoItem.newBuilder().build();

					if(itemStack != null) {
						return Optional.of(itemStack);
					}
				}
			}

			return Optional.empty();
		});
	}

	public Optional<ItemStack> getMythicMobsItem(@NotNull String id) {
		if(!mythicMobsPresent) {
			return Optional.empty();
		}

		return retrieveFromAPI(() ->
				mythicMobs.getItemManager()
						.getItem(id)
						.map(mythicItem -> BukkitAdapter.adapt(mythicItem.generateItemStack(1)))
		);
	}

	public Optional<ItemStack> getItemsAdderItem(@NotNull String id) {
		if(!itemsAdderPresent) {
			return Optional.empty();
		}
		return retrieveFromAPI(() -> {
			if (!CustomStack.isInRegistry(id)) {
				return Optional.empty();
			}

			return Optional.of(CustomStack.getInstance(id).getItemStack());
		});
	}

	public Optional<ItemStack> getOraxenItem(@NotNull String id) {
		if(!oraxenPresent) {
			return Optional.empty();
		}
		return retrieveFromAPI(() -> {
			if (!OraxenItems.exists(id)) {
				return Optional.empty();
			}

			return Optional.of(OraxenItems.getItemById(id).build());
		});
	}

	public Optional<String> getOraxenIdFromBlock(@NotNull Block block) {
		if(!oraxenPresent) {
			return Optional.empty();
		}
		return retrieveFromAPI(() -> {
			if(OraxenBlocks.isOraxenBlock(block)) {
				return Optional.of(OraxenBlocks.getOraxenBlock(block.getLocation()).getItemID());
			}
			if(OraxenFurniture.isFurniture(block)) {
				return Optional.of(OraxenFurniture.getFurnitureMechanic(block).getItemID());
			}

			return Optional.empty();
		});
	}

	private void loadMmoItems() {
		if(Bukkit.getServer().getPluginManager().getPlugin("MMOItems") != null) {
			mmoItems = MMOItems.plugin;
			Formatter.info("Hooked onto MMOItems.");
			mmoItemsPresent = true;
		} else {
			Formatter.info("MMOItems not found.");
		}
	}

	private void loadMythicMobs() {
		if(Bukkit.getServer().getPluginManager().isPluginEnabled("MythicMobs")) {
			this.mythicMobs = MythicProvider.get();
			Formatter.info("Hooked onto MythicMobs.");
			mythicMobsPresent = true;
		} else {
			Formatter.info("MythicMobs not found.");
		}
	}

	private void loadItemsAdder() {
		if(Bukkit.getServer().getPluginManager().isPluginEnabled("ItemsAdder")) {
			Formatter.info("Hooked onto ItemsAdder.");
			itemsAdderPresent = true;
		} else {
			Formatter.info("ItemsAdder not found.");
		}
	}

	private void loadOraxen() {
		if(Bukkit.getServer().getPluginManager().isPluginEnabled("Oraxen")) {
			Formatter.info("Hooked onto Oraxen.");
			oraxenPresent = true;
		} else {
			Formatter.info("Oraxen not found.");
		}
	}

	private void loadPAPI() {
		if(Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
			new PAPIExpansion().register();
			Formatter.info("Hooked onto PlaceholderAPI.");
		} else {
			Formatter.info("PlaceholderAPI not found.");
		}
	}

	public static DependencyManager getInstance() {
		return INSTANCE;
	}

}
