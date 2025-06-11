package com.gilles_m.rp_professions.object.crafting_recipe;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public abstract class AbstractCraftingContext {

    protected final Player player;

    protected final AbstractCraftingRecipe craftingRecipe;

    protected final Inventory inventory;

    protected AbstractCraftingContext(Player player, AbstractCraftingRecipe craftingRecipe, Inventory inventory) {
        this.player = player;
        this.craftingRecipe = craftingRecipe;
        this.inventory = inventory;
    }

    public void runCraft() {
        if(!canCraft()) {
            return;
        }
        runCraft(craftingRecipe.allowMultiple() ? getCraftedAmount() : 1);
    }

    protected abstract void runCraft(int amount);

    protected abstract int getCraftedAmount();

    protected abstract boolean canCraft();

}
