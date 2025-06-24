package com.gilles_m.rp_professions.version;

import com.github.spigot_gillesm.format_lib.Formatter;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public enum WrapperProvider {

    V_1_21("VersionWrapper_1_21_R1", 1, 21),

    V_1_18("VersionWrapper_1_18_R1", 1, 18);

    private static final String CLASS_PATH = "com.gilles_m.rp_professions.version.";

    private final String wrapperClass;

    private final int[] bukkitVersion;

    WrapperProvider(String wrapperClass, final int... bukkitVersion) {
        this.wrapperClass = wrapperClass;
        this.bukkitVersion = bukkitVersion;
    }

    /**
     * Returns true if the given bukkit version is at least this instance's version. e.g., given [1, 19, 1],
     * V_1_21_4.isVersionAbove returns false but V_1_17.isVersionAbove returns true.
     *
     * @param version the bukkit server version to compare
     * @return a boolean
     */
    private boolean isVersionAbove(final int... version) {
        final int maxLength = Math.max(this.bukkitVersion.length, version.length);

        for(int i = 0; i < maxLength; i++) {
            //If the version array is too small, default to 0
            final int a = (i < this.bukkitVersion.length) ? this.bukkitVersion[i] : 0;
            final int b = (i < version.length) ? version[i] : 0;

            //As soon as the value from the same index is greater for b, then the given version is higher
            if(b > a) {
                return true;
            }
            //Otherwise, it is lower
            if(b < a) {
                return false;
            }
        }

        //Or equal
        return true;
    }

    /**
     * Finds the most recent version wrapper such that it is compatible.
     *
     * @param bukkitVersion the current bukkit server version
     * @return an implementation of VersionWrapper
     */
    public static VersionWrapper fetchWrapper(final int[] bukkitVersion) {
        String path = null;

        for(final WrapperProvider provider : WrapperProvider.values()) {
            if(provider.isVersionAbove(bukkitVersion)) {
                path = CLASS_PATH + provider.wrapperClass;
                break;
            }
        }
        try {
            //Default to the oldest version wrapper available
            if(path == null) {
                final String wrapperClass = WrapperProvider.values()[WrapperProvider.values().length-1].wrapperClass;
                path = CLASS_PATH + wrapperClass;
                Formatter.warning(String.format("No version wrapper for version %s. Defaulting to %s",
                        Arrays.toString(bukkitVersion), wrapperClass));
            }

            return (VersionWrapper) Class.forName(path).getDeclaredConstructor().newInstance();
        } catch(ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException
                | NoSuchMethodException exception) {

            Formatter.error(String.format("Unable to instantiate version wrapper for version %s", Arrays.toString(bukkitVersion)));
            Formatter.error(exception.getMessage());
        }

        return null;
    }

}
