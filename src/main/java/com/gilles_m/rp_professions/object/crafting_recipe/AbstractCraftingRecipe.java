package com.gilles_m.rp_professions.object.crafting_recipe;

import com.gilles_m.rp_professions.Identifiable;
import com.gilles_m.rp_professions.manager.PlayerManager;
import com.gilles_m.rp_professions.menu.AbstractCraftingMenu;
import com.gilles_m.rp_professions.menu.AbstractDynamicCraftingMenu;
import com.gilles_m.rp_professions.object.Category;
import com.gilles_m.rp_professions.object.Profession;
import com.gilles_m.rp_professions.object.station.Workstation;
import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Represents anything that can be crafted.
 */
@Setter
public abstract class AbstractCraftingRecipe implements Identifiable {

	@Getter
	protected String id;

	@Getter
	protected Category category;

	@Getter
	protected Profession profession;

	@Getter
	protected ItemStack reagent;

	@Getter
	protected Workstation workstation;

	@Getter
	protected CraftingRecipeMeta metaData;

	protected ItemStack result;

	protected AbstractCraftingRecipe() { }

	protected AbstractCraftingRecipe(Category category, Workstation workstation) {
		this.category = category;
		this.workstation = workstation;
	}

	protected AbstractCraftingMenu getCraftingMenu() {
		return workstation.getCraftingMenu();
	}

	public AbstractDynamicCraftingMenu getDynamicCraftingMenu() {
		return workstation.getDynamicCraftingMenu();
	}

	/**
	 * Check if the given items match the pattern.
	 *
	 * @param items the items
	 * @return true if the items match the pattern
	 */
	public abstract boolean isPatternMatching(ItemStack[] items);

	public boolean canCraft(Player player, final boolean checkMenu) {
		return PlayerManager.getInstance()
				.getProfession(player)
				.map(playerProfession -> {
					if(!profession.equals(playerProfession)) {
						return false;
					}
					//Check that the player has the required level
					if(PlayerManager.getInstance().getProfessionLevel(player) < metaData.getRequiredLevel()) {
						return false;
					}
					//Check that the player knows the recipe
					if(!PlayerManager.getInstance().knowRecipe(player, this)) {
						return false;
					}
					if(checkMenu) {
						//Check that the player is in the correct menu
						return SimpleMenu.getMenu(player)
								.map(menu -> menu.getClass().equals(getCraftingMenu().getClass()))
								.orElse(false);
					} else {
						return true;
					}
				})
				.orElse(false);
	}

	public ItemStack getResult() {
		return result.clone();
	}

	public int getAmount() {
		return result.getAmount();
	}

	public int getReagentAmount() {
		return reagent != null ? reagent.getAmount() : 0;
	}

	public boolean allowMultiple() {
		return metaData.isAllowMultiple() && !workstation.isWorkbench();
	}

	public boolean isDynamic() {
		return this instanceof DynamicCraftingRecipe;
	}

}
