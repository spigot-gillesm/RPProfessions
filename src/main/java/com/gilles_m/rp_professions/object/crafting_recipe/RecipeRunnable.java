package com.gilles_m.rp_professions.object.crafting_recipe;

import com.gilles_m.rp_professions.menu.AbstractDynamicCraftingMenu;
import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Represent a crafting runnable.
 *
 * @param <R> the crafting recipe type
 * @param <M> the dynamic menu type
 */
public abstract class RecipeRunnable<R extends AbstractCraftingRecipe, M extends AbstractDynamicCraftingMenu> extends BukkitRunnable {

	protected final Player player;

	@Getter
	private final R recipe;

	@Getter
	private final M menu;

	protected int score = 0;

	protected int globalTimer = 0;

	protected RecipeRunnable(Player player, R recipe, M menu) {
		this.player = player;
		this.recipe = recipe;
		this.menu = menu;
	}

	@Override
	public void run() {
		//If the menu is no longer open or the crafting process is over, it stops
		if(!isMenuOpen() || execute()) {
			cancel();
		}
		globalTimer++;
	}

	private boolean isMenuOpen() {
		//Check if the player left the menu
		return SimpleMenu.getMenu(player).map(playerMenu -> playerMenu.equals(this.menu)).orElse(false);
	}

	/**
	 * Execute the runnable logic.
	 *
	 * @return true if the runnable should stop
	 */
	protected abstract boolean execute();

}
