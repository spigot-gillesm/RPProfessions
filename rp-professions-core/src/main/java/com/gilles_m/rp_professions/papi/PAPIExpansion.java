package com.gilles_m.rp_professions.papi;

import com.github.spigot_gillesm.format_lib.Formatter;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PAPIExpansion extends PlaceholderExpansion {

	@Override
	public @NotNull String getIdentifier() {
		return "rpprofessions";
	}

	@Override
	public @NotNull String getAuthor() {
		return "gilles_m";
	}

	@Override
	public @NotNull String getVersion() {
		return "1.0.0";
	}

	@Override
	public String onRequest(@NotNull OfflinePlayer player, @NotNull String key) {
		if(!player.isOnline()) {
			return null;
		}

		//Find the right key and return the result of its function, or null if no key exist with that value.
		return PAPIKey.getPAPIByKey(key)
				.map(papiKey -> Formatter.colorize(papiKey.getFunction().apply((Player) player)))
				.orElse(null);
	}

}
