package com.gilles_m.rp_professions.menu.profession_menu;

import com.gilles_m.rp_professions.PluginUtils;
import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.manager.DependencyManager;
import com.gilles_m.rp_professions.manager.ItemManager;
import com.gilles_m.rp_professions.object.station.Workstation;
import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.gui_lib.SimpleButton;
import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * This menu shows information related to a specific workstation. This menu is accessible through a workstations menu.
 */
public class WorkstationMenu extends SimpleMenu {

    private final SimpleButton materialsButton;

    private final SimpleButton fuelButton;

    private final SimpleButton toolButton;

    public WorkstationMenu(SimpleMenu parent, Workstation workstation) {
        super(parent);

        this.materialsButton = new SimpleButton.DummyButton(makeBlocksIcon(workstation));
        this.fuelButton = new SimpleButton.DummyButton(makeFuelButtonIcon(workstation));
        this.toolButton = new SimpleButton.DummyButton(makeToolButton(workstation));
        setTitle(workstation.getDisplayName());
        setSize(3*9);
        setCancelActions(true);
        registerButtons(materialsButton, fuelButton, toolButton, SimpleButton.DummyButton.FILLING_BUTTON);
    }

    private ItemStack makeBlockIcon(String string) {
        if(string.startsWith("oraxen:")) {
            return DependencyManager.getInstance().getOraxenItem(string.split(":")[1]).orElse(null);
        }
        if(PluginUtils.isMaterial(string.toUpperCase())) {
            return new ItemStack(Material.valueOf(string.toUpperCase()));
        }
        
        return null;
    }

    private ItemStack makeBlocksIcon(Workstation workstation) {
        final List<String> lore = new ArrayList<>(List.of(""));
        ItemStack icon = null;

        for(final String blockId : workstation.getMaterials()) {
            final ItemStack block = makeBlockIcon(blockId);

            if(icon == null) {
                icon = block;
            }
            if(block != null) {
                lore.add(Formatter.colorize(String.format(RPProfessions.getInstance().getStringHolder().workstationBlockEntry,
                        PluginUtils.formatItemName(block))));
            }
        }
        final ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName(Formatter.colorize(RPProfessions.getInstance().getStringHolder().workstationBlockButton));
        meta.setLore(lore);
        icon.setItemMeta(meta);

        return icon;
    }

    private ItemStack makeFuelButtonIcon(Workstation workstation) {
        if(workstation.getFuel() == null) {
            return SimpleItem.newBuilder()
                    .material(Material.BUCKET)
                    .displayName(RPProfessions.getInstance().getStringHolder().workstationFuelButton)
                    .lore(RPProfessions.getInstance().getStringHolder().workstationNoFuelLore)
                    .build().make().getItemStack();
        } else {
            final List<String> lore = new ArrayList<>();

            for(final String line : RPProfessions.getInstance().getStringHolder().workstationFuelLore) {
                lore.add(String.format(line, PluginUtils.formatMaterialName(workstation.getFuel())));
            }

            return SimpleItem.newBuilder()
                    .material(Material.LAVA_BUCKET)
                    .displayName(RPProfessions.getInstance().getStringHolder().workstationFuelButton)
                    .lore(lore)
                    .build().make().getItemStack();
        }
    }

    private ItemStack makeToolButton(Workstation workstation) {
        final String toolId = workstation.getTool();
        final ItemStack noTool = SimpleItem.newBuilder()
                .material(Material.STICK)
                .displayName(RPProfessions.getInstance().getStringHolder().workstationToolButton)
                .lore(RPProfessions.getInstance().getStringHolder().workstationNoToolLore)
                .build().make().getItemStack();

        if(toolId == null || toolId.isBlank()) {
            return noTool;
        } else {
            return ItemManager.getInstance().get(toolId)
                    .map(item -> {
                        final ItemStack itemStack = item.getItemStack();
                        final ItemMeta meta = itemStack.getItemMeta();
                        final List<String> toolLore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                        final List<String> finalLore = new ArrayList<>();
                        RPProfessions.getInstance().getStringHolder().workstationToolLore.forEach(line -> finalLore.add(String.format(line, meta.getDisplayName())));

                        for(int i = RPProfessions.getInstance().getStringHolder().workstationToolLore.size() - 1; i >= 0; i--) {
                            toolLore.add(0, Formatter.colorize(finalLore.get(i)));
                        }
                        meta.setDisplayName(Formatter.colorize(RPProfessions.getInstance().getStringHolder().workstationToolButton));
                        meta.setLore(toolLore);
                        itemStack.setItemMeta(meta);

                        return itemStack;

                    }).orElse(noTool);
        }
    }

    @Override
    protected ItemStack getSlotItem(final int slot) {
        if(slot == 9 + 2) {
            return materialsButton.getIcon();
        }
        if(slot == 9 + 4) {
            return fuelButton.getIcon();
        }
        if(slot == 9 + 6) {
            return toolButton.getIcon();
        }

        return SimpleButton.DummyButton.FILLING_BUTTON.getIcon();
    }

}
