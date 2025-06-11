package com.gilles_m.rp_professions.menu.crafting_station_menu_impl;

import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.menu.AbstractCraftingStationMenu;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class CraftingMenu extends AbstractCraftingStationMenu {

	@Override
	protected ItemStack getCraftIcon() {
		return SimpleItem.newBuilder()
				.material(Material.LEATHER)
				.displayName(RPProfessions.getInstance().getStringHolder().craftingCraftButton)
				.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
				.build()
				.make()
				.getItemStack();
	}

	@Override
	protected String getTitle() {
		return RPProfessions.getInstance().getStringHolder().craftingMenuTitle;
	}

	@Override
	protected Sound getCraftSound() {
		return Sound.UI_LOOM_TAKE_RESULT;
	}

}
