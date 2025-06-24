package com.gilles_m.rp_professions.papi;

import com.gilles_m.rp_professions.RPProfessionsAPI;
import com.gilles_m.rp_professions.manager.PlayerManager;
import com.gilles_m.rp_professions.object.Profession;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

public enum PAPIKey {

	PLAYER_PROFESSION("player_profession", player -> RPProfessionsAPI.getInstance()
			.getPlayerProfession(player)
			.map(Profession::getDisplayName)
			.orElse("None")),

	PLAYER_PROFESSION_ID("player_profession_id", player -> RPProfessionsAPI.getInstance()
			.getPlayerProfession(player)
			.map(Profession::getId)
			.orElse("NULL")),

	PLAYER_PROFESSION_LEVEL("player_profession_level", player -> {
		if(!PlayerManager.getInstance().hasProfession(player)) {
			return "0";
		}

		return String.valueOf(PlayerManager.getInstance().getProfessionLevel(player));
	});

	private final String key;

	@Getter
	private final Function<Player, String> function;

	PAPIKey(String key, Function<Player, String> function) {
		this.key = key;
		this.function = function;
	}

	public static Optional<PAPIKey> getPAPIByKey(@NotNull String key) {
		return Arrays.stream(values())
				.filter(papiKey -> papiKey.key.equals(key))
				.findFirst();
	}

}
