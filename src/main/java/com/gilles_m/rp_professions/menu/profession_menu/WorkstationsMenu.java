package com.gilles_m.rp_professions.menu.profession_menu;

import com.gilles_m.rp_professions.PluginUtils;
import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.manager.DependencyManager;
import com.gilles_m.rp_professions.object.Profession;
import com.gilles_m.rp_professions.object.station.Workstation;
import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.gui_lib.ListingMenu;
import com.github.spigot_gillesm.gui_lib.SimpleButton;
import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This menu lists all workstations the profession uses. This menu is accessible through a profession menu.
 */
public class WorkstationsMenu extends ListingMenu {

    private final Profession profession;

    WorkstationsMenu(SimpleMenu parent, Profession profession) {
        super(parent);

        this.profession = profession;
        setTitle(RPProfessions.getInstance().getStringHolder().workstationsMenuTitle);
        setSize(2*9);
        setCancelActions(true);
    }

    private ItemStack makeWorkstationIcon(String string) {
        if(string.startsWith("oraxen:")) {
            return DependencyManager.getInstance().getOraxenItem(string.split(":")[1]).orElse(null);
        }
        if(PluginUtils.isMaterial(string.toUpperCase())) {
            return new ItemStack(Material.valueOf(string.toUpperCase()));
        }

        return null;
    }

    private Optional<SimpleButton> makeWorkstationIcon(Workstation workstation, String blockId) {
        final ItemStack icon = makeWorkstationIcon(blockId);

        if(icon == null) {
            return Optional.empty();
        }
        final ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName(Formatter.colorize(workstation.getDisplayName()));
        icon.setItemMeta(meta);

        return Optional.of(new SimpleButton(icon) {

            @Override
            public boolean action(Player player, final ClickType clickType, ItemStack itemStack) {
                if(!workstation.isWorkbench()) {
                    new WorkstationMenu(WorkstationsMenu.this, workstation).display(player);
                }

                return false;
            }

        });
    }

    @Override
    protected List<SimpleButton> generateButtons() {
        final List<SimpleButton> buttons = new ArrayList<>();

        for(final Workstation workstation : profession.getWorkstations()) {
            makeWorkstationIcon(workstation, workstation.getMaterials().get(0)).ifPresent(buttons::add);
        }

        return buttons;
    }

}
