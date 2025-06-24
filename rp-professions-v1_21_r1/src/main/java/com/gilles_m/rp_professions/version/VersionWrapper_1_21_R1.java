package com.gilles_m.rp_professions.version;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;

public class VersionWrapper_1_21_R1 implements VersionWrapper {

    @Override
    public Villager.Profession getVillagerProfession(@NotNull String name) {
        return Registry.VILLAGER_PROFESSION.get(NamespacedKey.minecraft(name.toLowerCase()));
    }

    @Override
    public boolean isVillagerProfession(@NotNull String name) {
        return Registry.VILLAGER_PROFESSION.get(NamespacedKey.minecraft(name.toLowerCase())) != null;
    }

}
