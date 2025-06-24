package com.gilles_m.rp_professions.command;

import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.manager.PlayerManager;
import com.gilles_m.rp_professions.menu.recipe_menu.CategoriesMenu;
import com.github.spigot_gillesm.command_lib.MainCommand;
import com.github.spigot_gillesm.command_lib.SimpleCommand;
import com.github.spigot_gillesm.format_lib.Formatter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@MainCommand
public class RecipesCommand extends SimpleCommand {

	public RecipesCommand() {
		super("recipes");

		setAliases(List.of("r"));
		setPlayerCommand(true);
		setDescription("Open the recipes menu");
		setPermission("rpprofessions.recipes");
	}

	@Override
	protected void run(CommandSender commandSender, String[] args) {
		if(args.length != 0) {
			Formatter.tell(commandSender, "&cThis command takes no arguments");
		}
		final Player player = (Player) commandSender;

		if(!PlayerManager.getInstance().hasProfession(player)) {
			Formatter.tell(commandSender, RPProfessions.getInstance().getStringHolder().noRecipesMessage);
			return;
		}
		new CategoriesMenu(player).display(player);
	}

}
