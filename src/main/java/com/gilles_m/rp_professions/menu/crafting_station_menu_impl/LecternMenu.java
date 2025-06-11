package com.gilles_m.rp_professions.menu.crafting_station_menu_impl;

import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.menu.AbstractCraftingStationMenu;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public class LecternMenu extends AbstractCraftingStationMenu {

	@Override
	protected ItemStack getCraftIcon() {
		return SimpleItem.newBuilder()
				.material(Material.ENCHANTED_BOOK)
				.displayName(RPProfessions.getInstance().getStringHolder().lecternCraftButton)
				.build()
				.make()
				.getItemStack();
	}

	@Override
	protected String getTitle() {
		return RPProfessions.getInstance().getStringHolder().lecternMenuTitle;
	}

	@Override
	protected Sound getCraftSound() {
		return Sound.ENTITY_ILLUSIONER_MIRROR_MOVE;
	}

}
