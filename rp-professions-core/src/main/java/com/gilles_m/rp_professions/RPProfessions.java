package com.gilles_m.rp_professions;

import com.gilles_m.rp_professions.listener.BrewingStandListener;
import com.gilles_m.rp_professions.listener.MythicMobsListener;
import com.gilles_m.rp_professions.listener.PlayerListener;
import com.gilles_m.rp_professions.listener.WorldListener;
import com.gilles_m.rp_professions.loader.*;
import com.gilles_m.rp_professions.manager.DependencyManager;
import com.gilles_m.rp_professions.manager.PlayerEffectManager;
import com.gilles_m.rp_professions.object.StringHolder;
import com.gilles_m.rp_professions.version.ServerVersion;
import com.gilles_m.rp_professions.version.VersionWrapper;
import com.github.spigot_gillesm.command_lib.CommandLib;
import com.github.spigot_gillesm.file_utils.FileUtils;
import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.gui_lib.GuiLib;
import com.github.spigot_gillesm.item_lib.ItemLib;
import com.github.spigot_gillesm.player_lib.PlayerLib;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class RPProfessions extends JavaPlugin {

	@Getter
	private static RPProfessions instance;

	private ServerVersion serverVersion;

	@Getter
	private boolean isPaper = false;

	@Setter
	@Getter
	private StringHolder stringHolder;

	private static void initialize(RPProfessions plugin) {
		instance = plugin;
		Formatter.PREFIX = "&f[&2&lRP&9Professions&f]";
		FileUtils.PLUGIN_DATA_FOLDER_PATH = instance.getDataFolder().getPath();

		DependencyManager.getInstance().load();

		CommandLib.initialize(plugin);
		PlayerLib.initialize(plugin);
		ItemLib.initialize(plugin);
		GuiLib.initialize(plugin);
	}

	@Override
	public void onEnable() {
		Formatter.info("Loading RPProfessions...");

		initialize(this);
		loadObjects();
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new WorldListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new BrewingStandListener(), this);

		if(DependencyManager.getInstance().isMythicMobsPresent()) {
			Bukkit.getServer().getPluginManager().registerEvents(new MythicMobsListener(), this);
		}
		final String serverName = Bukkit.getServer().getName();

		if(serverName.contains("paper") || serverName.contains("Paper")) {
			this.isPaper = true;
		}
		//Load the effects of any online players (which might happen when using the /reload (server) command)
		for(final Player player : Bukkit.getServer().getOnlinePlayers()) {
			try {
				PlayerEffectLoader.getInstance().loadPlayerEffects(player);
			} catch (IOException | InvalidConfigurationException exception) {
				Formatter.error(String.format("Error loading %s player effects:", player.getName()));
				Formatter.error(exception.getMessage());
			}
		}
		Formatter.info("&aDone!");
	}

	public void loadObjects() {
		this.serverVersion = new ServerVersion();

		try {
			StringHolderLoader.getInstance().load();
		} catch (final IOException exception) {
			Formatter.error("Could not load strings from the language.yml file. Does the file exist in the plugin folder?");
			Formatter.warning("Using default (english) strings.");
			this.stringHolder = new StringHolder();
		}
		CategoryLoader.getInstance().load();
		WorkstationLoader.getInstance().load();
		ProfessionLoader.getInstance().load();
		EffectLoader.getInstance().load();
		ProfessionItemLoader.getInstance().load();
		CraftingRecipeLoader.getInstance().load();
		PatternLoader.getInstance().load();
		DropLoader.getInstance().load();
		VillagerTradeSetLoader.getInstance().load();
	}

	@Override
	public void onDisable() {
		try {
			PlayerLib.saveAllData();
		} catch (IOException | InvalidConfigurationException exception) {
			Formatter.error("Error saving players data");
			Formatter.error(exception.getMessage());
		}
		//Save player effects
		for(final Player player : Bukkit.getServer().getOnlinePlayers()) {
			PlayerEffectManager.getInstance().clearEffectsOnQuit(player);
		}
	}

	public VersionWrapper getVersionWrapper() {
		return serverVersion.getVersionWrapper();
	}

	public static RPProfessionsAPI getAPI() {
		return RPProfessionsAPI.getInstance();
	}

}
