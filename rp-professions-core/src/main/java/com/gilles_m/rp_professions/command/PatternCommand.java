package com.gilles_m.rp_professions.command;

import com.gilles_m.rp_professions.PluginUtils;
import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.manager.PatternManager;
import com.github.spigot_gillesm.command_lib.SimpleCommand;
import com.github.spigot_gillesm.format_lib.Formatter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PatternCommand extends SimpleCommand {

	private static final String UNKNOWN_PATTERN_ERROR = "&cUnknown pattern id: %s";

	private static final String SPECIFY_PATTERN_ERROR = "&cYou must specify the pattern id";

	private static final String SPECIFY_PLAYER_ERROR = "&cYou must specify the player";

	private static final String INVALID_NUMBER_ERROR = "&cThe amount must be a positive integer";

	private static final String UNKNOWN_PLAYER_ERROR = "&cUnknown or offline player: %s";

	PatternCommand(SimpleCommand parentCommand) {
		super(parentCommand, "pattern");

		setAliases(List.of("pat", "p"));
		setPermission("rpprofessions.pattern");
		setPlayerCommand(false);
		setDescription("Pattern related command");

		new PatternGetCommand(this);
		new PatternGiveCommand(this);
	}

	@Override
	protected void run(CommandSender commandSender, String[] args) {
		if(args.length == 0) {
			displayHelp(commandSender);
		}
	}

	/**
	 * Get a single copy of the specified pattern
	 */
	private static class PatternGetCommand extends SimpleCommand {

		private PatternGetCommand(SimpleCommand parentCommand) {
			super(parentCommand, "get");

			setPermission("rpprofessions.pattern.get");
			setPlayerCommand(true);
			setDescription("Get the specified pattern in your inventory");
			addMandatoryArgument("pattern id");
		}

		@Override
		protected void run(CommandSender commandSender, String[] args) {
			if(args.length == 0) {
				Formatter.tell(commandSender, SPECIFY_PATTERN_ERROR);
				return;
			}
			final String patternId = args[0];
			final Player player = (Player) commandSender;

			PatternManager.getInstance()
					.get(patternId)
					.ifPresentOrElse(
							item -> player.getInventory().addItem(item.getPhysicalPattern()),
							() -> Formatter.tell(player, String.format(UNKNOWN_PATTERN_ERROR, patternId))
					);
		}

	}

	/**
	 * Give to a player the specified pattern(s)
	 */
	private static class PatternGiveCommand extends SimpleCommand {

		private PatternGiveCommand(SimpleCommand parentCommand) {
			super(parentCommand, "give");

			setPermission("rpprofession.pattern.give");
			setPlayerCommand(true);
			setDescription("Give the specified pattern to the specified player");
			addMandatoryArgument("pattern id");
			addMandatoryArgument("player");
			addOptionalArgument("amount");
		}

		@Override
		protected void run(CommandSender commandSender, String[] args) {
			if(args.length == 0) {
				Formatter.tell(commandSender, SPECIFY_PATTERN_ERROR);
				return;
			}
			if(args.length == 1) {
				Formatter.tell(commandSender, SPECIFY_PLAYER_ERROR);
				return;
			}
			if(args.length > 3) {
				Formatter.tell(commandSender, "&cThis command takes at most 3 arguments");
				return;
			}
			final String patternId = args[0];
			final Player target = PluginUtils.getPlayer(args[1]);

			if(target == null) {
				Formatter.tell(commandSender, String.format(UNKNOWN_PLAYER_ERROR, args[1]));
				return;
			}
			final String amount = args.length == 3 ? args[2] : "1";

			if(!PluginUtils.isInt(amount)) {
				Formatter.tell(commandSender, INVALID_NUMBER_ERROR);
				return;
			}
			final Player player = (Player) commandSender;

			PatternManager.getInstance()
					.get(patternId)
					.ifPresentOrElse(pattern -> {
								final ItemStack finalItem = pattern.getPhysicalPattern();
								final String itemName = finalItem.getItemMeta().hasDisplayName() ? finalItem.getItemMeta().getDisplayName()
										: PluginUtils.formatMaterialName(finalItem.getType());
								finalItem.setAmount(Integer.parseInt(amount));
								target.getInventory().addItem(finalItem);

								Formatter.tell(
										target,
										String.format(RPProfessions.getInstance()
												.getStringHolder()
												.getItemMessage,
												itemName,
												Integer.parseInt(amount))
								);
								Formatter.tell(player, String.format("You've given %s to %s", patternId, target.getName()));
							},
							() -> Formatter.tell(player, String.format(UNKNOWN_PATTERN_ERROR, patternId)));
		}

	}

}
