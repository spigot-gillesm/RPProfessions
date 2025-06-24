package com.gilles_m.rp_professions;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class PluginUtils {

	public int clamp(final int value, final int min, final int max) {
		if(max < min) {
			throw new IllegalArgumentException("The minimum value cannot be greater than the maximum value.");
		}
		return Math.max(min, Math.min(max, value));
	}

	public String formatItemName(@NotNull ItemStack itemStack) {
		if(!itemStack.hasItemMeta()) {
			return formatMaterialName(itemStack.getType());
		}
		final ItemMeta meta = itemStack.getItemMeta();

		if(!meta.hasDisplayName()) {
			return formatMaterialName(itemStack.getType());
		}

		return meta.getDisplayName();
	}

	/**
	 * Reformat the material name nicely.
	 * Example: "GLASS_BOTTLE" to "Glass Bottle"
	 *
	 * @param material the material
	 * @return the reformatted string
	 */
	public String formatMaterialName(@NotNull final Material material) {
		final var formattedName = new StringBuilder();

		//Remove the "_" and recapitalize the words
		for(final String part : material.name().split("_")) {
			formattedName.append(part.substring(0, 1).toUpperCase())
					.append(part.substring(1).toLowerCase())
					.append(" ");
		}
		//Remove the last useless space
		formattedName.deleteCharAt(formattedName.length()-1);

		return formattedName.toString();
	}

	/**
	 * Make a brewing stand inventory.
	 *
	 * @param player the player holding the inventory
	 * @param ingredient the top slot
	 * @param fuel the left slot
	 * @param itemStacks the 3 bottom slots
	 *
	 * @return the brewing stand inventory
	 */
	public BrewerInventory makeBrewingInventory(@NotNull Player player, ItemStack ingredient, ItemStack fuel, ItemStack... itemStacks) {
		final var inventory = (BrewerInventory) Bukkit.getServer().createInventory(player, InventoryType.BREWING);
		inventory.setIngredient(ingredient);
		inventory.setFuel(fuel);

		if(itemStacks.length == 0) {
			return inventory;
		}
		for(int i = 0; i < clamp(itemStacks.length, 0, 3); i++) {
			inventory.setItem(i, itemStacks[i]);
		}

		return inventory;
	}

	/**
	 * Make a brewing stand inventory.
	 *
	 * @param player the player holding the inventory
	 * @param ingredient the top slot
	 * @param fuel the left slot
	 * @param itemStack the (same) item for the 3 bottom slots
	 *
	 * @return the brewing stand inventory
	 */
	public BrewerInventory makeBrewingInventory(@NotNull Player player, ItemStack ingredient, ItemStack fuel, ItemStack itemStack) {
		//Pass the same item 3 times
		return makeBrewingInventory(player, ingredient, fuel, itemStack, itemStack, itemStack);
	}

	/**
	 * Add the item to the player's inventory or drop it near the player.
	 *
	 * @param player the player
	 * @param itemStack the item
	 */
	public void addToInventoryOrDrop(@NotNull Player player, @NotNull ItemStack itemStack) {
		player.getInventory().addItem(itemStack)
				.forEach((amount, item) -> {
					if(item.getType() == Material.AIR) {
						return;
					}
					player.getWorld().dropItemNaturally(player.getLocation(), item);
				});
	}

	public boolean decreaseOrRemove(ItemStack itemStack, final int amount) {
		if(itemStack == null || itemStack.getType() == Material.AIR) {
			return false;
		}
		if(itemStack.getAmount() - amount <= 0) {
			return true;
		} else {
			itemStack.setAmount(itemStack.getAmount() - amount);
			return false;
		}
	}

	public Player getPlayer(@NotNull String name) {
		return Bukkit.getServer().getPlayer(name);
	}

	public boolean isInt(@NotNull String string) {
		try {
			Integer.parseInt(ChatColor.stripColor(string));

			return true;
		} catch(final NumberFormatException exception) {
			return false;
		}
	}

	public boolean isMaterial(@NotNull String material) {
		try {
			Material.valueOf(material.toUpperCase());

			return true;
		} catch (final IllegalArgumentException exception) {
			return false;
		}
	}

	public boolean isVillagerProfession(@NotNull String villagerProfession) {
		return RPProfessions.getInstance().getVersionWrapper().isVillagerProfession(villagerProfession);
	}

}
