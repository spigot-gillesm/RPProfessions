package com.gilles_m.rp_professions.listener;

import com.gilles_m.rp_professions.manager.DropManager;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MythicMobsListener implements Listener {

    @EventHandler
    protected void onMythicMobDeath(MythicMobDeathEvent event) {
        DropManager.getInstance()
                .get(event.getMob().getMobType())
                .ifPresent(drop -> drop.drop(event.getEntity().getLocation()));
    }

}
