package com.gilles_m.rp_professions.menu.crafting_station_menu_impl;

import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.menu.AbstractCraftingStationMenu;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public class ForgeMenu extends AbstractCraftingStationMenu {

	@Override
	protected ItemStack getCraftIcon() {
		return SimpleItem.newBuilder()
				.material(Material.GOLD_INGOT)
				.displayName(RPProfessions.getInstance().getStringHolder().forgeCraftButton)
				.build()
				.make()
				.getItemStack();
	}

	@Override
	protected String getTitle() {
		return RPProfessions.getInstance().getStringHolder().forgeMenuTitle;
	}

	@Override
	protected Sound getCraftSound() {
		return Sound.BLOCK_LAVA_EXTINGUISH;
	}

}
