package com.gilles_m.rp_professions.object.crafting_recipe.crafting_context_impl;

import com.gilles_m.rp_professions.menu.AbstractCraftingStationMenu;
import com.gilles_m.rp_professions.menu.AbstractDynamicCraftingMenu;
import com.gilles_m.rp_professions.object.crafting_recipe.AbstractCraftingContext;
import com.gilles_m.rp_professions.object.crafting_recipe.AbstractCraftingRecipe;
import com.gilles_m.rp_professions.object.crafting_recipe.DynamicCraftingRecipe;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AdvancedCraftingContext extends AbstractCraftingContext {

    private final AbstractCraftingStationMenu craftingStationMenu;

    private final ItemStack[] craftingGridContent;

    private final ItemStack reagent;

    public AdvancedCraftingContext(Player player, AbstractCraftingRecipe craftingRecipe, AbstractCraftingStationMenu craftingStationMenu,
                                   Inventory inventory, ItemStack[] craftingGridContent) {

        super(player, craftingRecipe, inventory);

        this.craftingStationMenu = craftingStationMenu;
        this.craftingGridContent = craftingGridContent;
        this.reagent = inventory.getContents()[AbstractCraftingStationMenu.REAGENTS_SLOT];
    }

    @Override
    protected void runCraft(final int amount) {
        craftingStationMenu.clearGrid(inventory, amount, craftingRecipe.getReagentAmount());

        if(craftingRecipe.isDynamic()) {
            runDynamicCraft(player, craftingRecipe, amount);
        } else {
            craftingStationMenu.completeCraft(player, inventory, craftingRecipe, amount);
        }
    }

    @SuppressWarnings("unchecked")
    private void runDynamicCraft(Player player, AbstractCraftingRecipe craftingRecipe, final int craftedAmount) {
        final AbstractDynamicCraftingMenu menu = craftingRecipe.getDynamicCraftingMenu();
        //Start a new dynamic crafting recipe runnable and attach it to the menu
        menu.setRecipeRunnable(((DynamicCraftingRecipe) craftingRecipe).start(player, menu));

        //Set the result of the menu
        final ItemStack result = craftingRecipe.getResult();
        result.setAmount(result.getAmount() * craftedAmount);
        menu.setResult(result);
        menu.display(player);
    }

    @Override
    protected int getCraftedAmount() {
        int amount = 64;

        for(final var ingredient: craftingGridContent) {
            if(ingredient != null && ingredient.getAmount() < amount) {
                amount = ingredient.getAmount();
            }
        }
        final int reagentAmount = craftingRecipe.getReagentAmount();
        
        //Divide by the reagent amount to get the actual amount of craftable items at once
        if(reagent != null && (reagent.getAmount() / reagentAmount) < amount) {
            amount = reagent.getAmount() / reagentAmount;
        }
        //For crafting recipes outputing more than one item per craft, check and remove excess
        if(amount * craftingRecipe.getAmount() > 64) {
            //The excess of outputed items
            final int excess = amount * craftingRecipe.getAmount() - 64;
            //How many crafts match that excess
            final int craftedExcess = excess / craftingRecipe.getAmount();
            //Remove the excess
            amount -= craftedExcess;
        }

        return amount;
    }

    @Override
    protected boolean canCraft() {
        return craftingRecipe.canCraft(player, true) && isMenuReady() && hasReagents();
    }

    private boolean hasReagents() {
        if(craftingRecipe.getReagent() == null) {
            return true;
        }
        final ItemStack usedReagent = inventory.getContents()[AbstractCraftingStationMenu.REAGENTS_SLOT];

        if(usedReagent == null) {
            return false;
        }

        return craftingRecipe.getReagent().isSimilar(usedReagent) && usedReagent.getAmount() >= craftingRecipe.getReagentAmount();
    }

    private boolean isMenuReady() {
        final ItemStack[] contents = inventory.getContents();

        return contents[AbstractCraftingStationMenu.RESULT_SLOT] == null
                || contents[AbstractCraftingStationMenu.RESULT_SLOT].equals(new ItemStack(Material.AIR));
    }

}
