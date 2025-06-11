package com.gilles_m.rp_professions.menu.crafting_station_menu_impl;

import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.menu.AbstractCraftingStationMenu;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class EnchantmentMenu extends AbstractCraftingStationMenu {

	@Override
	protected ItemStack getCraftIcon() {
		return SimpleItem.newBuilder()
				.material(Material.ENDER_EYE)
				.displayName(RPProfessions.getInstance().getStringHolder().enchantmentCraftButton)
				.build()
				.make()
				.getItemStack();
	}

	@Override
	protected String getTitle() {
		return RPProfessions.getInstance().getStringHolder().enchantmentMenuTitle;
	}

}
