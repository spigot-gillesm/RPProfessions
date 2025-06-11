package com.gilles_m.rp_professions.command;

import com.gilles_m.rp_professions.menu.profession_menu.ProfessionsMenu;
import com.github.spigot_gillesm.command_lib.MainCommand;
import com.github.spigot_gillesm.command_lib.SimpleCommand;
import com.github.spigot_gillesm.format_lib.Formatter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@MainCommand
public class ProfessionsCommand extends SimpleCommand {

	public ProfessionsCommand() {
		super("professions");

		setAliases(List.of("profess", "prof", "p"));
		setPlayerCommand(true);
		setDescription("Open the profession selection menu");
		setPermission("rpprofessions.professions");
	}

	@Override
	protected void run(CommandSender commandSender, String[] args) {
		if(args.length != 0) {
			Formatter.tell(commandSender, "&cThis command takes no arguments");
		}
		new ProfessionsMenu().display((Player) commandSender);
	}

}
