package com.gilles_m.rp_professions.listener;

import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.event.CompleteCraftEvent;
import com.gilles_m.rp_professions.event.InteractWithPatternEvent;
import com.gilles_m.rp_professions.event.PlayerEffectClearEvent;
import com.gilles_m.rp_professions.event.PlayerEffectGetEvent;
import com.gilles_m.rp_professions.loader.PlayerEffectLoader;
import com.gilles_m.rp_professions.manager.*;
import com.gilles_m.rp_professions.object.RecipePattern;
import com.gilles_m.rp_professions.object.crafting_recipe.crafting_context_impl.WorkbenchCraftingContext;
import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import com.github.spigot_gillesm.gui_lib.SimpleMenuInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class PlayerListener implements Listener {

	/*
	 * MENU RELATED EVENTS
	 */

	@EventHandler
	protected void onPlayerInteract(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		final Block block = event.getClickedBlock();

		//Do not proceed if a menu is opened
		if(SimpleMenu.getMenu(player).isPresent()) {
			event.setCancelled(true);
			return;
		}
		if(block == null || event.getHand() == EquipmentSlot.OFF_HAND) {
			return;
		}
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK && (PlayerManager.getInstance().displayWorkstation(player, block))) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	protected void onPlayerClickEvent(InventoryClickEvent event) {
		final Inventory inventory = event.getClickedInventory();

		if(inventory == null || inventory.getType() != InventoryType.WORKBENCH) {
			return;
		}
		if(!(event.getWhoClicked() instanceof Player player)) {
			return;
		}
		final ItemStack[] craftingGridContent = new ItemStack[9];

		//Delay of 1 tick to allow the server to acknowledge the modified item
		Bukkit.getServer().getScheduler().runTaskLater(RPProfessions.getInstance(),
				() -> {
					//Get the content of the crafting grid. slot 0 = result slot
					for(int i = 1; i < inventory.getSize(); i++) {
						craftingGridContent[i-1] = inventory.getItem(i);
					}
					//Find any matching recipe and run the craft
					CraftingRecipeManager.getInstance()
							.getRecipeMatchingPattern(craftingGridContent)
							.ifPresent(recipe -> new WorkbenchCraftingContext(player, recipe, inventory).runCraft());
				}, 1);
	}

	@EventHandler
	protected void onItemDrag(InventoryDragEvent event) {
		final Inventory inventory = event.getInventory();

		if(inventory.getType() != InventoryType.WORKBENCH) {
			return;
		}
		if(!(event.getWhoClicked() instanceof Player player)) {
			return;
		}
		final ItemStack[] craftingGridContent = new ItemStack[9];

		//Delay of 1 tick to allow the server to acknowledge the modified item
		Bukkit.getServer().getScheduler().runTaskLater(RPProfessions.getInstance(),
				() -> {
					//Get the content of the crafting grid. slot 0 = result slot
					for(int i = 1; i < inventory.getSize(); i++) {
						craftingGridContent[i-1] = inventory.getItem(i);
					}
					//Find any matching recipe and run the craft
					CraftingRecipeManager.getInstance().getRecipeMatchingPattern(craftingGridContent)
							.ifPresent(recipe -> new WorkbenchCraftingContext(player, recipe, inventory).runCraft());
				}, 1);
	}

	@EventHandler
	protected void onPlayerCompleteWorkbenchCraft(InventoryClickEvent event) {
		final Inventory inventory = event.getClickedInventory();

		if(inventory == null || inventory.getType() != InventoryType.WORKBENCH) {
			return;
		}
		if(!(event.getWhoClicked() instanceof Player player)) {
			return;
		}
		if(event.getSlotType() != InventoryType.SlotType.RESULT) {
			return;
		}
		if(event.getCurrentItem() == null) {
			return;
		}
		if(event.isShiftClick() && player.getInventory().firstEmpty() < 0) {
			return;
		}
		CraftingRecipeManager.getInstance()
				.get(event.getCurrentItem())
				.ifPresent(recipe -> PlayerManager.getInstance()
						.getProfession(player)
						.ifPresent(profession ->
								//Run the complete craft event
								Bukkit.getServer()
										.getPluginManager()
										.callEvent(new CompleteCraftEvent(player, recipe, profession, 1))
						)
				);
	}

	/*
	 * ITEM INTERACTION EVENTS
	 */

	@EventHandler
	private void onPlayerClickItem(PlayerInteractEvent event) {
		//Check if player is interacting with a pattern
		if(event.getAction().name().contains("RIGHT")) {
			PatternManager.getInstance()
					.getPattern(event.getItem())
					.ifPresent(pattern -> Bukkit.getServer()
							.getPluginManager()
							.callEvent(new InteractWithPatternEvent(event.getPlayer(), pattern))
					);
		}
	}

	@EventHandler
	private void onInteractWithPattern(InteractWithPatternEvent event) {
		final RecipePattern pattern = event.getRecipePattern();
		final Player player = event.getPlayer();

		if(pattern.teach(player) && pattern.isConsumedOnUse()) {
			Bukkit.getServer()
					.getScheduler()
					.runTaskLater(RPProfessions.getInstance(),
							() -> player.getInventory().setItemInMainHand(new ItemStack(Material.AIR)),
							2L);
		}
	}

	@EventHandler
	private void onPlayerConsumeItem(PlayerItemConsumeEvent event) {
		ItemManager.getInstance()
				.get(event.getItem()).ifPresent(item -> {
					if(item.hasEffects()) {
						item.getEffects().forEach(effect -> effect.apply(event.getPlayer()));
					}
				});
	}

	/*
	 * PROFESSION CRAFTING RELATED EVENTS
	 */

	@EventHandler
	protected void onCompleteCraft(CompleteCraftEvent event) {
		PlayerManager.getInstance().updateProfessionLevel(
				event.getPlayer(),
				event.getPlayerProfession(),
				event.getCraftingRecipe(),
				event.getCraftedAmount()
		);
	}

	/*
	 * EFFECT RELATED EVENTS
	 */

	@EventHandler
	protected void onPlayerJoin(PlayerJoinEvent event) {
		try {
			PlayerEffectLoader.getInstance().loadPlayerEffects(event.getPlayer());
		} catch (IOException | InvalidConfigurationException exception) {
			Formatter.error(String.format("Error loading %s player effects:", event.getPlayer().getName()));
			Formatter.error(exception.getMessage());
		}
	}

	@EventHandler
	protected void onPlayerGetEffect(PlayerEffectGetEvent event) {
		//Nothing yet
	}

	@EventHandler
	protected void onPlayerClearEffect(PlayerEffectClearEvent event) {
		//Nothing yet
	}

	@EventHandler
	protected void onPlayerDeath(PlayerDeathEvent event) {
		//Clear effects that should disappear when the player dies
		PlayerEffectManager.getInstance().clearEffectsOnDeath(event.getEntity());
	}

	@EventHandler
	protected void onPlayerQuit(PlayerQuitEvent event) {
		//Clear and save the effects that should be saved when the player dc
		PlayerEffectManager.getInstance().clearEffectsOnQuit(event.getPlayer());
	}

}
