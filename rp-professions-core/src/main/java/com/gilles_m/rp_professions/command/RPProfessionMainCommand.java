package com.gilles_m.rp_professions.command;

import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.manager.ProfessionManager;
import com.gilles_m.rp_professions.object.Profession;
import com.github.spigot_gillesm.command_lib.MainCommand;
import com.github.spigot_gillesm.command_lib.SimpleCommand;
import com.github.spigot_gillesm.format_lib.Formatter;
import org.bukkit.command.CommandSender;

import java.util.List;

@MainCommand
public class RPProfessionMainCommand extends SimpleCommand {

	public RPProfessionMainCommand() {
		super("rpprofession");

		setAliases(List.of("rpprofess", "rpprofes", "rpprof", "rpp"));
		setPlayerCommand(false);
		setDescription("RPProfession main command");
		setPermission("rpprofessions");

		new ReloadCommand(this);
		new ListProfessionCommand(this);
		new ItemCommand(this);
		new PatternCommand(this);
		new SetCommand(this);
	}

	@Override
	protected void run(CommandSender commandSender, String[] args) {
		if(args.length == 0) {
			displayHelp(commandSender);
		}
	}

	private static class ReloadCommand extends SimpleCommand {

		private ReloadCommand(SimpleCommand parentCommand) {
			super(parentCommand, "reload");

			setAliases(List.of("rld", "r"));
			setPermission("rpprofessions.reload");
			setPlayerCommand(false);
			setDescription("Reload the plugin");
		}

		@Override
		protected void run(CommandSender commandSender, String[] args) {
			if(args.length > 0) {
				Formatter.tell(commandSender, "&cThis command takes no arguments");
				return;
			}
			Formatter.tell(commandSender, "Reloading plugin...");
			RPProfessions.getInstance().loadObjects();
			Formatter.tell(commandSender, "&aDone!");
		}

	}

	private static class ListProfessionCommand extends SimpleCommand {

		private ListProfessionCommand(SimpleCommand parentCommand) {
			super(parentCommand, "list");

			setAliases(List.of("ls"));
			setPermission("rpprofessions.list");
			setPlayerCommand(false);
			setDescription("List the available professions");
		}

		@Override
		protected void run(CommandSender commandSender, String[] args) {
			if(args.length > 0) {
				Formatter.tell(commandSender, "&cThis command takes no arguments");
				return;
			}
			for(final Profession profession : ProfessionManager.getInstance().getAll()) {
				Formatter.tell(commandSender, String.format("&7* %s (%s&7)", profession.getId(), profession.getDisplayName()));
			}
		}

	}

}
