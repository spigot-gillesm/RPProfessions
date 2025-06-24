package com.gilles_m.rp_professions.object;

import com.gilles_m.rp_professions.Identifiable;
import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.manager.CraftingRecipeManager;
import com.gilles_m.rp_professions.manager.PlayerManager;
import com.gilles_m.rp_professions.object.crafting_recipe.AbstractCraftingRecipe;
import com.github.spigot_gillesm.format_lib.Formatter;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RecipePattern implements Identifiable {

	@Getter
	private final String id;

	@Getter
	private final boolean consumedOnUse;

	private final ItemStack physicalPattern;

	/**
	 * @param id the recipe id this pattern refers to
	 * @param consumedOnUse whether the pattern is consumed when used
	 * @param physicalPattern the in-game pattern
	 */
	public RecipePattern(String id, final boolean consumedOnUse, ItemStack physicalPattern) {
		this.id = id;
		this.consumedOnUse = consumedOnUse;
		this.physicalPattern = physicalPattern;
	}

	/**
	 * Teach its recipe to the player if the player meets the requirements.
	 *
	 * @param player the player
	 * @return true if the recipe has been taught
	 */
	public boolean teach(Player player) {
		return CraftingRecipeManager.getInstance().get(id).map(craftingRecipe -> {
			if(canLearn(player, craftingRecipe)) {
				PlayerManager.getInstance().teachRecipe(player, craftingRecipe);
				Formatter.tell(player, String.format(RPProfessions.getInstance().getStringHolder().learnNewRecipeMessage,
						craftingRecipe.getResult().getItemMeta().getDisplayName()));
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

				return true;
			} else {
				return false;
			}
		}).orElse(false);
	}

	private boolean canLearn(Player player, AbstractCraftingRecipe craftingRecipe) {
		if(!PlayerManager.getInstance().matchProfession(player, craftingRecipe.getProfession())) {
			Formatter.tell(player, RPProfessions.getInstance().getStringHolder().invalidProfessionMessage);

			return false;
		}
		if(PlayerManager.getInstance().knowRecipe(player, craftingRecipe)) {
			Formatter.tell(player, RPProfessions.getInstance().getStringHolder().alreadyTaughtMessage);

			return false;
		}
		if(PlayerManager.getInstance().getProfessionLevel(player) < craftingRecipe.getMetaData().getRequiredLevel()) {
			Formatter.tell(player, RPProfessions.getInstance().getStringHolder().invalidProfessionLevelMessage);

			return false;
		}

		return true;
	}

	public ItemStack getPhysicalPattern() {
		return physicalPattern.clone();
	}

}
