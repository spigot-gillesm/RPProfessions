package com.gilles_m.rp_professions.menu.recipe_menu;

import com.gilles_m.rp_professions.PluginUtils;
import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.manager.DependencyManager;
import com.gilles_m.rp_professions.menu.profession_menu.WorkstationMenu;
import com.gilles_m.rp_professions.object.crafting_recipe.AbstractCraftingRecipe;
import com.gilles_m.rp_professions.object.crafting_recipe.crafting_recipe_impl.CraftingRecipe;
import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.gui_lib.SimpleButton;
import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * This menu shows the crafting recipe of a specific crafting recipe. This is the third accessible menu when using the
 * recipes command.
 */
public class RecipeMenu extends SimpleMenu {

	private final CraftingRecipe craftingRecipe;

	private final SimpleButton infoMenuButton;

	public RecipeMenu(SimpleMenu parentMenu, CraftingRecipe craftingRecipe) {
		super(parentMenu);

		this.craftingRecipe = craftingRecipe;
		this.infoMenuButton = new SimpleButton(SimpleItem.newBuilder()
				.material(Material.PAPER)
				.displayName(RPProfessions.getInstance().getStringHolder().infoButton)
				.build().make().getItemStack()) {

			@Override
			public boolean action(Player player, final ClickType clickType, ItemStack itemStack) {
				final var menu = new RecipeInfoMenu(RecipeMenu.this, craftingRecipe);
				menu.display(player);

				return false;
			}

		};
		setSize(6*9);
		setTitle(RPProfessions.getInstance().getStringHolder().recipeMenuTitle);
		setCancelActions(true);
		registerButtons(infoMenuButton, SimpleButton.DummyButton.FILLING_BUTTON);
	}

	@Override
	protected ItemStack getSlotItem(final int slot) {
		if(slot == 12) {
			return craftingRecipe.getPattern()[0];
		}
		else if(slot == 13) {
			return craftingRecipe.getPattern()[1];
		}
		else if(slot == 14) {
			return craftingRecipe.getPattern()[2];
		}
		else if(slot == 19) {
			return infoMenuButton.getIcon();
		}
		else if(slot == 21) {
			return craftingRecipe.getPattern()[3];
		}
		else if(slot == 22) {
			return craftingRecipe.getPattern()[4];
		}
		else if(slot == 23) {
			return craftingRecipe.getPattern()[5];
		}
		else if(slot == 25) {
			return craftingRecipe.getResult();
		}
		else if(slot == 30) {
			return craftingRecipe.getPattern()[6];
		}
		else if(slot == 31) {
			return craftingRecipe.getPattern()[7];
		}
		else if(slot == 32) {
			return craftingRecipe.getPattern()[8];
		}
		else if(slot == 49) {
			return craftingRecipe.getReagent();
		}
		else {
			//Fill the rest
			return SimpleButton.DummyButton.FILLING_BUTTON.getIcon();
		}
	}

	private static class RecipeInfoMenu extends SimpleMenu {

		private final SimpleButton workstationButton;

		private final SimpleButton isAdvancedButton;

		private final SimpleButton requiredLevelButton;

		private final SimpleButton levelGainButton;

		private final SimpleButton levelCapButton;

		private final SimpleButton allowMultipleButton;

		private RecipeInfoMenu(RecipeMenu parent, AbstractCraftingRecipe craftingRecipe) {
			super(parent);

			this.workstationButton = makeWorkstationIcon(craftingRecipe,
					craftingRecipe.getWorkstation().getMaterials().stream().findAny()
					.orElse("CRAFTING_TABLE"));
			this.isAdvancedButton = new SimpleButton.DummyButton(
					SimpleItem.newBuilder()
							.material(craftingRecipe.isDynamic() ? Material.GOLDEN_PICKAXE : Material.IRON_PICKAXE)
							.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
							.displayName(String.format(RPProfessions.getInstance().getStringHolder().craftingHelpType,
									(craftingRecipe.isDynamic() ? RPProfessions.getInstance().getStringHolder().craftingHelpAdvanced
											: RPProfessions.getInstance().getStringHolder().craftingHelpSimple)))
							.build().make().getItemStack()
			);
			this.requiredLevelButton = new SimpleButton.DummyButton(
					SimpleItem.newBuilder()
							.material(Material.EXPERIENCE_BOTTLE)
							.displayName(String.format(RPProfessions.getInstance().getStringHolder().craftingHelpRequiredLevel,
									craftingRecipe.getMetaData().getRequiredLevel()))
							.build().make().getItemStack()
			);
			this.levelGainButton = new SimpleButton.DummyButton(
					SimpleItem.newBuilder()
							.material(Material.EXPERIENCE_BOTTLE)
							.displayName(String.format(RPProfessions.getInstance().getStringHolder().craftingHelpLevelGain,
									craftingRecipe.getMetaData().getLevelGain()))
							.build().make().getItemStack()
			);
			this.levelCapButton = new SimpleButton.DummyButton(
					SimpleItem.newBuilder()
							.material(Material.EXPERIENCE_BOTTLE)
							.displayName(String.format(RPProfessions.getInstance().getStringHolder().craftingHelpLevelCap,
									craftingRecipe.getMetaData().getLevelCap()))
							.build().make().getItemStack()
			);
			this.allowMultipleButton = new SimpleButton.DummyButton(
					SimpleItem.newBuilder()
							.material(craftingRecipe.allowMultiple() ? Material.BOOK : Material.PAPER)
							.displayName(String.format(RPProfessions.getInstance().getStringHolder().allowMultiple,
									craftingRecipe.allowMultiple() ? RPProfessions.getInstance().getStringHolder().allowMultipleYes
											: RPProfessions.getInstance().getStringHolder().allowMultipleNo))
							.build().make().getItemStack()
			);
			setTitle(RPProfessions.getInstance().getStringHolder().infoMenuTitle);
			setSize(4*9);
			registerButtons(workstationButton, isAdvancedButton, requiredLevelButton, levelGainButton, levelCapButton,
					allowMultipleButton, SimpleButton.DummyButton.FILLING_BUTTON);
		}

		private ItemStack makeWorkstationIcon(String string) {
			if(string.startsWith("oraxen:")) {
				return DependencyManager.getInstance().getOraxenItem(string.split(":")[1]).orElse(null);
			}
			if(PluginUtils.isMaterial(string.toUpperCase())) {
				return new ItemStack(Material.valueOf(string.toUpperCase()));
			}

			return null;
		}

		//TODO: Improve this. Find a better way to get ItemStacks from the string.
		private SimpleButton makeWorkstationIcon(AbstractCraftingRecipe craftingRecipe, String blockId) {
			final ItemStack icon = makeWorkstationIcon(blockId);

			if(icon == null) {
				return null;
			}
			final ItemMeta meta = icon.getItemMeta();
			meta.setDisplayName(
					Formatter.colorize(
							String.format(
									RPProfessions.getInstance().getStringHolder().craftingHelpWorkstation,
									craftingRecipe.getWorkstation().getDisplayName()
							)
					)
			);
			icon.setItemMeta(meta);

			return new SimpleButton(icon) {

				@Override
				public boolean action(Player player, final ClickType clickType, ItemStack itemStack) {
					if(!craftingRecipe.getWorkstation().isWorkbench()) {
						new WorkstationMenu(RecipeInfoMenu.this, craftingRecipe.getWorkstation()).display(player);
					}

					return false;
				}

			};
		}

		@Override
		protected ItemStack getSlotItem(final int slot) {
			if(slot == 3+9) {
				return workstationButton.getIcon();
			}
			if(slot == 4+9) {
				return isAdvancedButton.getIcon();
			}
			if(slot == 5+9) {
				return allowMultipleButton.getIcon();
			}
			if(slot == 3+18) {
				return requiredLevelButton.getIcon();
			}
			if(slot == 4+18) {
				return levelGainButton.getIcon();
			}
			if(slot == 5+18) {
				return levelCapButton.getIcon();
			}

			return SimpleButton.DummyButton.FILLING_BUTTON.getIcon();
		}
	}

}
