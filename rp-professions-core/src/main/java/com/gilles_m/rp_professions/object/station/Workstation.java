package com.gilles_m.rp_professions.object.station;

import com.gilles_m.rp_professions.Identifiable;
import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.dto.AbstractCraftingRecipeDTO;
import com.gilles_m.rp_professions.menu.AbstractCraftingMenu;
import com.gilles_m.rp_professions.menu.AbstractDynamicCraftingMenu;
import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

@Setter
public class Workstation implements Identifiable {

	//Workstation serving as vanilla crafting table
	public static final Workstation WORKBENCH = new Workstation();

	static {
		//Set up the vanilla workstation
		WORKBENCH.id = "WORKBENCH";
		WORKBENCH.displayName = RPProfessions.getInstance().getStringHolder().workbenchDisplayName;
		WORKBENCH.materials = new ArrayList<>(List.of("CRAFTING_TABLE"));
		WORKBENCH.craftingStation = CraftingStation.WORKBENCH;
	}

	@Getter
	private String id;

	private CraftingStation craftingStation;

	@Getter
	private String displayName;

	@Getter
	private List<String> materials;

	@Getter
	private boolean restricted;

	@Getter
	private Material fuel;

	@Getter
	private String tool;

	/**
	 * Get the CraftingRecipeDTO class linked to this workstation.
	 *
	 * @param isDynamic whether the crafting recipe is dynamic
	 *
	 * @return the CraftEntity class
	 */
	public Class<? extends AbstractCraftingRecipeDTO> getCraftRecipeDTOClass(final boolean isDynamic) {
		if(isDynamic) {
			return craftingStation.getDynamicCraftingDTOClass();
		} else {
			return craftingStation.getCraftingDTOClass();
		}
	}

	public AbstractCraftingMenu getCraftingMenu() {
		return craftingStation.getCraftingMenu();
	}

	public AbstractDynamicCraftingMenu getDynamicCraftingMenu() {
		return craftingStation.getDynamicCraftingMenu();
	}

	public boolean isFueled(Block block) {
		if(fuel == null) {
			return true;
		}
		boolean isFueled = false;

		//Check if a block of fuel material is next to the workstation
		for(int x = -1; x <= 1; x++) {
			for(int z = -1; z <= 1; z++) {
				if(block.getLocation().add(x, 0, z).getBlock().getType() == fuel) {
					isFueled = true;
				}
			}
		}

		return isFueled;
	}

	public boolean isWorkbench() {
		return craftingStation == CraftingStation.WORKBENCH;
	}

	@Override
	public final String toString() {
		return MoreObjects.toStringHelper(this)
				.add("id", id)
				.add("displayName", displayName)
				.add("materials", materials.toString())
				.add("restricted", restricted)
				.add("fuel", fuel != null ? fuel.toString() : "null")
				.add("craftingStation", craftingStation.name())
				.toString();
	}

}
