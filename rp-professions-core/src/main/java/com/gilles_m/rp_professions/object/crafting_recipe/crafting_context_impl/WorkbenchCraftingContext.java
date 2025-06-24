package com.gilles_m.rp_professions.object.crafting_recipe.crafting_context_impl;

import com.gilles_m.rp_professions.object.crafting_recipe.AbstractCraftingContext;
import com.gilles_m.rp_professions.object.crafting_recipe.AbstractCraftingRecipe;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class WorkbenchCraftingContext extends AbstractCraftingContext {

    private static final int RESULT_SLOT = 0;

    public WorkbenchCraftingContext(Player player, AbstractCraftingRecipe craftingRecipe, Inventory inventory) {
        super(player, craftingRecipe, inventory);
    }

    @Override
    protected void runCraft(final int amount) {
        final ItemStack result = craftingRecipe.getResult();
        result.setAmount(result.getAmount() * amount);
        inventory.setItem(RESULT_SLOT, result);
    }

    @Override
    protected int getCraftedAmount() {
        return 1;
    }

    @Override
    protected boolean canCraft() {
        return craftingRecipe.canCraft(player, false) && isMenuReady();
    }

    private boolean isMenuReady() {
        return inventory.getContents()[RESULT_SLOT] == null || inventory.getContents()[RESULT_SLOT].getType() == Material.AIR;
    }

}
