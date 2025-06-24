package com.gilles_m.rp_professions.listener;

import com.gilles_m.rp_professions.VillagerUtils;
import com.gilles_m.rp_professions.manager.DropManager;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;

public class WorldListener implements Listener {

	@EventHandler
	protected void onVillagerGetNewTrades(VillagerAcquireTradeEvent event) {
		if(!(event.getEntity() instanceof Villager villager)) {
			return;
		}
		VillagerUtils.addRandomTradesToVillager(villager);

		if(villager.isTrading()) {
			final var trader = villager.getTrader();

			if(trader instanceof Player player) {
				player.updateInventory();
			}
		}
	}

	@EventHandler
	protected void onEntityDeathEvent(EntityDeathEvent event) {
		DropManager.getInstance()
				.get(event.getEntity().getType().toString())
				.ifPresent(drop -> drop.drop(event.getEntity().getLocation()));
	}

}
