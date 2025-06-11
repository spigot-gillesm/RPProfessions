package com.gilles_m.rp_professions;

import com.gilles_m.rp_professions.manager.VillagerTradeSetManager;
import com.gilles_m.rp_professions.object.randomized_object.VillagerTrade;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@UtilityClass
public class VillagerUtils {

    private void addRecipeToTrades(Villager villager, ItemStack result, ItemStack firstIngredient, ItemStack secondIngredient,
                                          final int maxUses, final boolean experienceReward, final int villagerExperience) {

        if(firstIngredient == null) {
            throw new IllegalArgumentException("The first ingredient cannot be null.");
        }
        final List<MerchantRecipe> mutableRecipes = new ArrayList<>(villager.getRecipes());
        final var recipe = new MerchantRecipe(result, 0, maxUses, experienceReward);
        recipe.setVillagerExperience(villagerExperience);
        recipe.addIngredient(firstIngredient);

        if(secondIngredient != null) {
            recipe.addIngredient(secondIngredient);
        }
        mutableRecipes.add(recipe);
        villager.setRecipes(mutableRecipes);
    }

    public void addRecipeToTrades(Villager villager, VillagerTrade villagerTrade) {
        addRecipeToTrades(
                villager,
                villagerTrade.getResult(),
                villagerTrade.getFirstIngredient(),
                villagerTrade.getSecondIngredient(),
                villagerTrade.getMaxUse(),
                villagerTrade.isExperienceReward(),
                villagerTrade.getVillagerExperienceReward()
        );
    }

    public void addRandomTradesToVillager(Villager villager) {
        final String world = villager.getWorld().getName();
        final Villager.Profession villagerProfession = villager.getProfession();

        //5 seconds cooldown to prevent the action to happen more than once per villager level-up
        //as vanilla villagers get 2 new recipes per level-up thus 2 events are fired at the same time
        CooldownHolder.getInstance()
                .checkAndStart(villager,
                        2,
                        () -> {
                            final Set<VillagerTrade> electedTrades = new HashSet<>();

                            VillagerTradeSetManager.getInstance()
                                    .getAll()
                                    .stream()
                                    //Find a matching world and profession
                                    .filter(villagerTradeSet -> villagerTradeSet.getWorld().equals(world)
                                            && villagerTradeSet.getProfession() == villagerProfession)
                                    .findFirst()
                                    .ifPresent(villagerTradeSet ->
                                        //Add each random trade to the villager's trades
                                        electedTrades.addAll(villagerTradeSet.getRandomTrades(villager.getVillagerLevel()))
                                    );
                            electedTrades.forEach(trade -> addRecipeToTrades(villager, trade));
                        });
    }

}
