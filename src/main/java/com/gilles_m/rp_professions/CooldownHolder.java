package com.gilles_m.rp_professions;

import org.bukkit.entity.Entity;

import java.util.HashSet;
import java.util.Set;

public class CooldownHolder {

    private static final CooldownHolder INSTANCE = new CooldownHolder();

    private final Set<Integer> cooldowns = new HashSet<>();

    private CooldownHolder() { }

    public void checkAndStart(Entity entity, final int cooldown, Runnable runnable) {
        if(!isInCooldown(entity)) {
            runnable.run();
            startCooldown(entity, cooldown);
        }
    }

    public void startCooldown(Entity entity, final int cooldown) {
        cooldowns.add(entity.getEntityId());
        RPProfessions.getInstance()
                .getServer()
                .getScheduler()
                .runTaskLater(
                        RPProfessions.getInstance(),
                        () -> cooldowns.remove(entity.getEntityId()),
                        cooldown * 20L
                );
    }

    public boolean isInCooldown(Entity entity) {
        return cooldowns.contains(entity.getEntityId());
    }

    public static CooldownHolder getInstance() {
        return INSTANCE;
    }

}
