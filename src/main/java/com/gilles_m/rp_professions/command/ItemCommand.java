package com.gilles_m.rp_professions.command;

import com.gilles_m.rp_professions.PluginUtils;
import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.manager.ItemManager;
import com.github.spigot_gillesm.command_lib.SimpleCommand;
import com.github.spigot_gillesm.format_lib.Formatter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemCommand extends SimpleCommand {

	private static final String UNKNOWN_ITEM_ERROR = "&cUnknown item id: %s";

	private static final String SPECIFY_ITEM_ERROR = "&cYou must specify the item id";

	private static final String SPECIFY_PLAYER_ERROR = "&cYou must specify the player";

	private static final String INVALID_NUMBER_ERROR = "&cThe amount must be a positive integer";

	private static final String UNKNOWN_PLAYER_ERROR = "&cUnknown or offline player: %s";

	ItemCommand(SimpleCommand parentCommand) {
		super(parentCommand, "item");

		setAliases(List.of("i"));
		setPermission("rpprofessions.item");
		setPlayerCommand(false);
		setDescription("Item related command");

		new ItemGetCommand(this);
		new ItemGiveCommand(this);
	}

	@Override
	protected void run(CommandSender commandSender, String[] args) {
		if(args.length == 0) {
			displayHelp(commandSender);
		}
	}

	/**
	 * Get a single copy of the specified key
	 */
	private static class ItemGetCommand extends SimpleCommand {

		private ItemGetCommand(SimpleCommand parentCommand) {
			super(parentCommand, "get");

			setPermission("rpprofessions.item.get");
			setPlayerCommand(true);
			setDescription("Get the specified item in your inventory");
			addMandatoryArgument("item id");
		}

		@Override
		protected void run(CommandSender commandSender, String[] args) {
			if(args.length == 0) {
				Formatter.tell(commandSender, SPECIFY_ITEM_ERROR);
				return;
			}
			final String itemId = args[0];
			final Player player = (Player) commandSender;

			ItemManager.getInstance()
					.get(itemId)
					.ifPresentOrElse(item -> {
							final ItemStack itemStack = item.getItemStack();
							itemStack.setAmount(1);
							player.getInventory().addItem(itemStack);
						},
							() -> Formatter.tell(player, String.format(UNKNOWN_ITEM_ERROR, itemId)));
		}

	}

	/**
	 * Give to a player the specified item(s)
	 */
	private static class ItemGiveCommand extends SimpleCommand {

		private ItemGiveCommand(SimpleCommand parentCommand) {
			super(parentCommand, "give");

			setPermission("rpprofession.item.give");
			setPlayerCommand(true);
			setDescription("Give the specified item to the specified player");
			addMandatoryArgument("item id");
			addMandatoryArgument("player");
			addOptionalArgument("amount");
		}

		@Override
		protected void run(CommandSender commandSender, String[] args) {
			if(args.length == 0) {
				Formatter.tell(commandSender, SPECIFY_ITEM_ERROR);
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
			final String itemId = args[0];
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

			ItemManager.getInstance()
					.get(itemId)
					.ifPresentOrElse(item -> {
							final ItemStack finalItem = item.getItemStack().clone();
							final String itemName = finalItem.getItemMeta().hasDisplayName() ? finalItem.getItemMeta().getDisplayName()
									: PluginUtils.formatMaterialName(finalItem.getType());
							finalItem.setAmount(Integer.parseInt(amount));
							target.getInventory().addItem(finalItem);

							Formatter.tell(target,
									String.format(RPProfessions.getInstance().getStringHolder().getItemMessage,
											itemName, Integer.parseInt(amount)));
							Formatter.tell(player, String.format("You've given %s to %s", itemId, target.getName()));
						},
							() -> Formatter.tell(player, String.format(UNKNOWN_ITEM_ERROR, itemId)));
		}

	}

}
