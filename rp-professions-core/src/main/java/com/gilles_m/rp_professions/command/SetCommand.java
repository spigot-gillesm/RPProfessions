package com.gilles_m.rp_professions.command;

import com.gilles_m.rp_professions.PluginUtils;
import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.manager.PlayerManager;
import com.gilles_m.rp_professions.manager.ProfessionManager;
import com.github.spigot_gillesm.command_lib.SimpleCommand;
import com.github.spigot_gillesm.format_lib.Formatter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SetCommand extends SimpleCommand {

    private static final String SPECIFY_PROFESSION_ERROR = "&cYou must specify the profession id";

    private static final String UNKNOWN_PROFESSION_ERROR = "&cUnknown profession id: %s";

    private static final String SPECIFY_PLAYER_ERROR = "&cYou must specify the player";

    private static final String UNKNOWN_PLAYER_ERROR = "&cUnknown or offline player: %s";

    private static final String SPECIFY_LEVEL_ERROR = "&cYou must specify the level";

    private static final String INVALID_NUMBER_ERROR = "&cThe amount must be a positive integer";

    SetCommand(SimpleCommand parentCommand) {
        super(parentCommand, "set");

        setAliases(List.of("s"));
        setPermission("rpprofessions.set");
        setPlayerCommand(false);
        setDescription("Set player profession data");

        new SetPlayerProfessionCommand(this);
        new SetPlayerProfessionLevelCommand(this);
    }

    @Override
    protected void run(CommandSender commandSender, String[] args) {
        if(args.length == 0) {
            displayHelp(commandSender);
        }
    }

    private static class SetPlayerProfessionCommand extends SimpleCommand {

        SetPlayerProfessionCommand(SimpleCommand parentCommand) {
            super(parentCommand, "profession");

            setPermission("rpprofessions.set.profession");
            setPlayerCommand(false);
            setDescription("Set the specified player's profession");
            addMandatoryArgument("player");
            addMandatoryArgument("profession");
        }

        @Override
        protected void run(CommandSender commandSender, String[] args) {
            if(args.length == 0) {
                Formatter.tell(commandSender, SPECIFY_PLAYER_ERROR);
                return;
            }
            if(args.length == 1) {
                Formatter.tell(commandSender, SPECIFY_PROFESSION_ERROR);
                return;
            }
            if(args.length > 2) {
                Formatter.tell(commandSender, "&cThis command takes at most 2 arguments");
                return;
            }
            final Player target = PluginUtils.getPlayer(args[0]);
            final String professionId = args[1];

            if(target == null) {
                Formatter.tell(commandSender, String.format(UNKNOWN_PLAYER_ERROR, args[0]));
                return;
            }
            final Player player = (Player) commandSender;

            ProfessionManager.getInstance()
                            .get(professionId)
                                    .ifPresentOrElse(profession -> {
                                        PlayerManager.getInstance().setProfession(target, profession);
                                        Formatter.tell(target,
                                                String.format(RPProfessions.getInstance().getStringHolder().setProfessionMessage,
                                                profession.getDisplayName())
                                        );
                                        Formatter.tell(player, String.format("&9%s &ais now a %s",
                                                target.getName(), profession.getDisplayName()));
                                    }, () -> Formatter.tell(player, String.format(UNKNOWN_PROFESSION_ERROR, professionId)));
        }

    }

    private static class SetPlayerProfessionLevelCommand extends SimpleCommand {

        SetPlayerProfessionLevelCommand(SimpleCommand parent) {
            super(parent, "level");
            setAliases(new ArrayList<>(Collections.singletonList("s")));
            setPlayerCommand(false);
            setDescription("Set the player's profession level");
            setPermission("professions.set.level");
            addMandatoryArgument("player");
            addMandatoryArgument("level");
        }

        @Override
        protected void run(CommandSender commandSender, String[] args) {
            if(args.length == 0) {
                Formatter.tell(commandSender, SPECIFY_PLAYER_ERROR);
                return;
            }
            if(args.length == 1) {
                Formatter.tell(commandSender, SPECIFY_LEVEL_ERROR);
                return;
            }
            if(args.length > 2) {
                Formatter.tell(commandSender, "&cThis command takes at most 2 arguments");
                return;
            }
            final Player target = PluginUtils.getPlayer(args[0]);

            if(target == null) {
                Formatter.tell(commandSender, String.format(UNKNOWN_PLAYER_ERROR, args[1]));
                return;
            }
            if(!PluginUtils.isInt(args[1])) {
                tell(commandSender, INVALID_NUMBER_ERROR);
                return;
            }
            final int level = Integer.parseInt(args[1]);

            if(level < 0) {
                tell(commandSender, "&cThe level must be an integer greater than 0");
                return;
            }
            PlayerManager.getInstance().setProfessionLevel(target, level);
            tell(commandSender, String.format("&9%s &ahas now its profession set to level &9%d&a!", target.getName(), level));
            tell(target, String.format(RPProfessions.getInstance().getStringHolder().setProfessionLevelMessage, level));
        }

    }

}
