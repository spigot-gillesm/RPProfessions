package com.gilles_m.rp_professions.menu.crafting_station_menu_impl;

import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.menu.AbstractCraftingStationMenu;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class AnvilMenu extends AbstractCraftingStationMenu {

	@Override
	protected ItemStack getCraftIcon() {
		return SimpleItem.newBuilder()
				.material(Material.IRON_SHOVEL)
				.displayName(RPProfessions.getInstance().getStringHolder().anvilCraftButton)
				.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
				.build()
				.make()
				.getItemStack();
	}

	@Override
	protected String getTitle() {
		return RPProfessions.getInstance().getStringHolder().anvilMenuTitle;
	}

	@Override
	protected Sound getCraftSound() {
		return Sound.BLOCK_ANVIL_LAND;
	}

}
