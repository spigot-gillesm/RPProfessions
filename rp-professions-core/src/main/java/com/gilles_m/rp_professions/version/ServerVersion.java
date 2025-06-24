package com.gilles_m.rp_professions.version;

import lombok.Getter;
import org.bukkit.Bukkit;

/**
 * This class handles server version related operations and gives access to the appropriate version wrapper, avoiding
 * Bukkit API version issues.
 * <p>
 * Inspired from the class of the same name from the MythicLib repository.
 */
public class ServerVersion {

    //There are at most 3 values defining a Bukkit server version
    private static final int MAXIMUM_INDEX = 3;

    @Getter
    private final VersionWrapper versionWrapper;

    public ServerVersion() throws IllegalStateException {
        //Get the version numbers
        final String[] bukkitSplit = Bukkit.getServer().getBukkitVersion().split("-")[0].split("\\."); // ["1", "20", "4"]
        //Stores the integer representing the running bukkit version
        final int[] bukkitVersion = new int[Math.min(MAXIMUM_INDEX, bukkitSplit.length)];

        for(int i = 0; i < bukkitVersion.length; i++) {
            bukkitVersion[i] = Integer.parseInt(bukkitSplit[i]);
        }
        this.versionWrapper = WrapperProvider.fetchWrapper(bukkitVersion);

        if(versionWrapper == null) {
            throw new IllegalStateException("Null version wrapper");
        }
    }

}
