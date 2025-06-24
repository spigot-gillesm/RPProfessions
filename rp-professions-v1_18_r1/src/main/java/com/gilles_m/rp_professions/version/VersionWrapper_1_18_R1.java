package com.gilles_m.rp_professions.version;

import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;

public class VersionWrapper_1_18_R1 implements VersionWrapper {

    @Override
    public Villager.Profession getVillagerProfession(@NotNull String name) {
        return Villager.Profession.valueOf(name.toUpperCase());
    }

    @Override
    public boolean isVillagerProfession(@NotNull String name) {
        try {
            Villager.Profession.valueOf(name.toUpperCase());

            return true;
        } catch (final IllegalArgumentException exception) {
            return false;
        }
    }

}
