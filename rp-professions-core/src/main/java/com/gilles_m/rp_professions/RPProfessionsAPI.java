package com.gilles_m.rp_professions;

import com.gilles_m.rp_professions.manager.ItemManager;
import com.gilles_m.rp_professions.manager.PatternManager;
import com.gilles_m.rp_professions.manager.PlayerManager;
import com.gilles_m.rp_professions.menu.profession_menu.ProfessionsMenu;
import com.gilles_m.rp_professions.menu.recipe_menu.CategoriesMenu;
import com.gilles_m.rp_professions.object.Profession;
import com.gilles_m.rp_professions.object.ProfessionItem;
import com.gilles_m.rp_professions.object.RecipePattern;
import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class RPProfessionsAPI {

	private static final RPProfessionsAPI INSTANCE = new RPProfessionsAPI();

	protected RPProfessionsAPI() { }

	/**
	 * Get the ProfessionItem instance matching the given id if one exists.
	 *
	 * @param id the RPProfessions item id
	 * @return an optional of an instance of ProfessionItem
	 */
	public Optional<ProfessionItem> getProfessionItem(@NotNull String id) {
		return ItemManager.getInstance().get(id);
	}

	/**
	 * Get the item stack matching the given RPProfessions item id if one exists.
	 *
	 * @param id the RPProfessions item id
	 * @return an optional of an instance of ItemStack
	 */
	public Optional<ItemStack> getItemStack(@NotNull String id) {
		return getProfessionItem(id).map(ProfessionItem::getItemStack);
	}

	/**
	 * Get the RecipePattern instance matching the given RPProfessions item id if that item has a pattern.
	 *
	 * @param id the RPProfessions item id to get the pattern from
	 * @return an optional of an instance of RecipePattern
	 */
	public Optional<RecipePattern> getPattern(@NotNull String id) {
		return PatternManager.getInstance().get(id);
	}

	/**
	 * Get the pattern's item stack matching the given RPProfessions item id if that item has a pattern.
	 *
	 * @param id the RPProfessions item id to get the pattern from
	 * @return an optional of an instance of ItemStack
	 */
	public Optional<ItemStack> getPatternItemStack(@NotNull String id) {
		return getPattern(id).map(RecipePattern::getPhysicalPattern);
	}

	/**
	 * Get the player's profession if it has one.
	 *
	 * @param player the player to get the profession from
	 * @return an instance of Profession
	 */
	public Optional<Profession> getPlayerProfession(@NotNull Player player) {
		return PlayerManager.getInstance().getProfession(player);
	}

	/**
	 * Get the player's profession level or -1 if the player has no profession.
	 *
	 * @param player the player to get the profession level from
	 * @return the player's profession level as an integer
	 */
	public int getPlayerProfessionLevel(@NotNull Player player) {
		if(!PlayerManager.getInstance().hasProfession(player)) {
			return -1;
		}

		return PlayerManager.getInstance().getProfessionLevel(player);
	}

	/**
	 * Display the profession selection menu to the specified player.
	 *
	 * @param player the player
	 */
	public void displayProfessionsMenu(@NotNull Player player) {
		new ProfessionsMenu().display(player);
	}

	/**
	 * Display the profession selection menu to the specified player.
	 *
	 * @param parent the parent menu
	 * @param player the player
	 */
	public void displayProfessionsMenu(@NotNull SimpleMenu parent, @NotNull Player player) {
		new ProfessionsMenu(parent).display(player);
	}

	/**
	 * Display the crafting recipes menu to the specified player.
	 *
	 * @param player the player
	 */
	public void displayRecipesMenu(@NotNull Player player) {
		new CategoriesMenu(player).display(player);
	}

	/**
	 * Display the crafting recipes menu to the specified player.
	 *
	 * @param parent the parent menu
	 * @param player the player
	 */
	public void displayRecipesMenu(@NotNull SimpleMenu parent, @NotNull Player player) {
		new CategoriesMenu(parent, player).display(player);
	}

	public static RPProfessionsAPI getInstance() {
		return INSTANCE;
	}

}
