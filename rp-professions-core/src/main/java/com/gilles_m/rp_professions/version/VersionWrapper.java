package com.gilles_m.rp_professions.version;

import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;

/**
 * An interface designed to abstract version specific API calls.
 */
public interface VersionWrapper {

    /**
     * Get the villager profession class from its name.
     *
     * @param name the villager profession's name
     * @return the matching villager profession class
     */
    Villager.Profession getVillagerProfession(@NotNull String name);

    /**
     * Check whether the given string is a valid villager profession.
     *
     * @param name the villager profession's name
     * @return true if the name is a villager profession
     */
    boolean isVillagerProfession(@NotNull String name);

}
