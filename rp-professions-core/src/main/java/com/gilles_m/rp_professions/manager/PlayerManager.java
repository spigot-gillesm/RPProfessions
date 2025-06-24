package com.gilles_m.rp_professions.manager;

import com.gilles_m.rp_professions.PluginUtils;
import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.object.Profession;
import com.gilles_m.rp_professions.object.crafting_recipe.AbstractCraftingRecipe;
import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.player_lib.DataManager;
import com.github.spigot_gillesm.player_lib.PlayerData;
import com.github.spigot_gillesm.player_lib.PlayerTag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class PlayerManager {

	private static final PlayerManager INSTANCE = new PlayerManager();

	private PlayerManager() { }

	public boolean displayWorkstation(Player player, Block block) {
		//Immediately return false if the player has no profession
		return getProfession(player).map(profession ->
			//Return true if the player has effectively open a workstation menu
			profession.getWorkstation(block).map(workstation -> {
				//Check if the workstation is fueled
				if(!workstation.isFueled(block)) {
					Formatter.tell(player,
							"&cThe " + workstation.getDisplayName() + " &cmust be fueled with " +
									PluginUtils.formatMaterialName(workstation.getFuel()));
					return false;
				}
				//Check if the workstation requires a specific tool
				if(workstation.getTool() != null) {
					final ItemStack heldItem = player.getInventory().getItemInMainHand();

					if(!ItemManager.getInstance().doItemMatchId(heldItem, workstation.getTool())) {
						return false;
					}
				//Otherwise player must be sneaking
				} else {
					if(!player.isSneaking()) {
						return false;
					}
				}
				workstation.getCraftingMenu().display(player);

				return true;
			}).orElse(false)
		).orElse(false);
	}

	public void teachRecipe(Player player, AbstractCraftingRecipe craftingRecipe) {
		if(!craftingRecipe.getMetaData().isKnownByDefault()) {
			DataManager.getData(player).addTagValue(PlayerTag.RECIPES, craftingRecipe.getId());
		}
	}

	/**
	 * Update the player's profession for the given amount of times by calling the analogous method for that
	 * many times.
	 *
	 * @param player the player
	 * @param profession the profession
	 * @param craftingRecipe the crafting recipes
	 * @param amount the amount of times to update the level
	 */
	public void updateProfessionLevel(Player player, Profession profession, AbstractCraftingRecipe craftingRecipe, final int amount) {
		for(int i = 0; i < amount; i++) {
			updateProfessionLevel(player, profession, craftingRecipe);
		}
	}

	/**
	 * Update the player's profession and warn them about new recipes.
	 *
	 * @param player the player
	 * @param profession the player's profession
	 * @param craftingRecipe the crafting recipe
	 */
	public void updateProfessionLevel(Player player, Profession profession, AbstractCraftingRecipe craftingRecipe) {
		final int playerLevel = getProfessionLevel(player);
		final int levelCap = craftingRecipe.getMetaData().getLevelCap();

		if(craftingRecipe.getMetaData().getLevelGain() <= 0 || playerLevel >= levelCap) {
			return;
		}
		final int newLevel = Math.min(playerLevel + craftingRecipe.getMetaData().getLevelGain(), levelCap);

		setProfessionLevel(player, newLevel);
		Formatter.tell(player, String.format(RPProfessions.getInstance().getStringHolder().professionLevelUpMessage,
				newLevel, profession.getDisplayName()));

		for(final AbstractCraftingRecipe learnedRecipe : CraftingRecipeManager.getInstance()
				.getRecipesWithRequiredLevel(profession, playerLevel, newLevel)) {

			//Do not warn the player about recipes which are not known by default
			if(!learnedRecipe.getMetaData().isKnownByDefault() && !PlayerManager.getInstance().knowRecipe(player, learnedRecipe)) {
				continue;
			}
			final ItemStack recipeResult = learnedRecipe.getResult();

			if(recipeResult.hasItemMeta() && recipeResult.getItemMeta().hasDisplayName()) {
				Formatter.tell(player, String.format(RPProfessions.getInstance().getStringHolder().learnNewRecipeMessage,
						recipeResult.getItemMeta().getDisplayName()));
			} else {
				Formatter.tell(player, RPProfessions.getInstance().getStringHolder().learnNewRecipeMessageGeneric);
			}
		}
	}

	public void removeProfession(Player player) {
		DataManager.getData(player).removeValue(PlayerTag.PROFESSION);
		DataManager.getData(player).removeValue(PlayerTag.PROFESSION_LEVEL);
	}

	public void setProfession(Player player, Profession profession) {
		DataManager.getData(player).setTagValue(PlayerTag.PROFESSION, profession.getId());
	}

	public void setProfessionLevel(Player player, final int level) {
		DataManager.getData(player).setTagValue(PlayerTag.PROFESSION_LEVEL, level);
	}

	public Optional<Profession> getProfession(Player player) {
		if(player == null) {
			return Optional.empty();
		}
		final Object id = DataManager.getData(player).getTagValue(PlayerTag.PROFESSION);
		return id != null ? ProfessionManager.getInstance().get(id.toString()) : Optional.empty();
	}

	public int getProfessionLevel(Player player) {
		final PlayerData playerData = DataManager.getData(player);
		return playerData.getTagValue(PlayerTag.PROFESSION_LEVEL, Integer.class, 1);
	}

	public boolean hasProfession(Player player) {
		return getProfession(player).isPresent();
	}

	/**
	 * Check if the player has the specified profession.
	 *
	 * @param player the player
	 * @param profession the profession
	 * @return true if the player match the profession
	 */
	public boolean matchProfession(Player player, Profession profession) {
		return getProfession(player)
				.map(playerProfession -> playerProfession.equals(profession))
				.orElse(false);
	}

	public boolean knowRecipe(Player player, AbstractCraftingRecipe craftingRecipe) {
		if(craftingRecipe.getMetaData().isKnownByDefault()) {
			return true;
		} else {
			return DataManager.getData(player).getTagList(PlayerTag.RECIPES, String.class).contains(craftingRecipe.getId());
		}
	}

	public static PlayerManager getInstance() {
		return INSTANCE;
	}

}
