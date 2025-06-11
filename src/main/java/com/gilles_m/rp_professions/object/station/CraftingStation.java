package com.gilles_m.rp_professions.object.station;

import com.gilles_m.rp_professions.dto.AbstractCraftingRecipeDTO;
import com.gilles_m.rp_professions.dto.crafting_recipe.BrewingStandCraftingRecipeDTO;
import com.gilles_m.rp_professions.dto.crafting_recipe.CraftingRecipeDTO;
import com.gilles_m.rp_professions.dto.crafting_recipe.dynamic_recipe.DynamicAnvilCraftingRecipeDTO;
import com.gilles_m.rp_professions.dto.crafting_recipe.dynamic_recipe.DynamicEnchantmentCraftingRecipeDTO;
import com.gilles_m.rp_professions.dto.crafting_recipe.dynamic_recipe.DynamicForgeCraftingRecipeDTO;
import com.gilles_m.rp_professions.menu.AbstractCraftingMenu;
import com.gilles_m.rp_professions.menu.AbstractDynamicCraftingMenu;
import com.gilles_m.rp_professions.menu.crafting_station_menu_impl.*;
import com.gilles_m.rp_professions.menu.dynamic_crafting_menu_impl.DynamicAnvilMenu;
import com.gilles_m.rp_professions.menu.dynamic_crafting_menu_impl.DynamicEnchantmentMenu;
import com.gilles_m.rp_professions.menu.dynamic_crafting_menu_impl.DynamicForgeMenu;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public enum CraftingStation {

	WORKBENCH(CraftingRecipeDTO.class,
			null,
			null,
			null),

	ANVIL(CraftingRecipeDTO.class,
			DynamicAnvilCraftingRecipeDTO.class,
			AnvilMenu::new,
			DynamicAnvilMenu::new),

	//A pattern memory game as a dynamic craft? Using the (multi-level) partial DFS algorithm? Using a snake algorithm?
	CRAFTING_TABLE(CraftingRecipeDTO.class,
			null,
			CraftingMenu::new,
			null),

	ENCHANTING_TABLE(CraftingRecipeDTO.class,
			DynamicEnchantmentCraftingRecipeDTO.class,
			EnchantmentMenu::new,
			DynamicEnchantmentMenu::new),

	FORGE(CraftingRecipeDTO.class,
			DynamicForgeCraftingRecipeDTO.class,
			ForgeMenu::new,
			DynamicForgeMenu::new),

	//Summoning mobs when conjuring items as a dynamic craft?
	LECTERN(CraftingRecipeDTO.class,
			null,
			LecternMenu::new,
			null),

	BREWING_STAND(CraftingRecipeDTO.class,
			BrewingStandCraftingRecipeDTO.class,
			PotionMenu::new,
			null);

	@Getter(AccessLevel.PACKAGE)
	private final Class<? extends AbstractCraftingRecipeDTO> craftingDTOClass;

	@Getter(AccessLevel.PACKAGE)
	private final Class<? extends AbstractCraftingRecipeDTO> dynamicCraftingDTOClass;

	private final Supplier<? extends AbstractCraftingMenu> getCraftingMenuSupplier;

	private final Supplier<? extends AbstractDynamicCraftingMenu> getDynamicCraftingMenuSupplier;

	CraftingStation(Class<? extends AbstractCraftingRecipeDTO> craftingDTOClass,
					Class<? extends AbstractCraftingRecipeDTO> dynamicCraftingDTOClass,
					Supplier<? extends AbstractCraftingMenu> getCraftingMenuSupplier,
					Supplier<? extends AbstractDynamicCraftingMenu> getDynamicCraftingMenuSupplier) {

		this.getCraftingMenuSupplier = getCraftingMenuSupplier;
		this.craftingDTOClass = craftingDTOClass;
		this.dynamicCraftingDTOClass = dynamicCraftingDTOClass;
		this.getDynamicCraftingMenuSupplier = getDynamicCraftingMenuSupplier;
	}

	public AbstractCraftingMenu getCraftingMenu() {
		return getCraftingMenuSupplier.get();
	}

	public AbstractDynamicCraftingMenu getDynamicCraftingMenu() {
		return getDynamicCraftingMenuSupplier.get();
	}

	public static Optional<CraftingStation> getCraftingStation(@NotNull String string) {
		try {
			return Optional.of(valueOf(string.toUpperCase()));
		} catch (final IllegalArgumentException exception) {
			return Optional.empty();
		}
	}

}
