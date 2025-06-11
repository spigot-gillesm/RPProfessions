package com.gilles_m.rp_professions.menu.profession_menu;

import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.manager.PlayerManager;
import com.gilles_m.rp_professions.object.Profession;
import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.gui_lib.SimpleButton;
import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * This menu gives details about a specific profession. This menu is accessible through a professions menu.
 */
public class ProfessionMenu extends SimpleMenu {

    private final SimpleButton selectProfessionButton;

    private final SimpleButton descriptionButton;

    private final SimpleButton workstationsButton;

    private final SimpleButton insightButton;

    ProfessionMenu(SimpleMenu parent, Profession profession) {
        super(parent);

        final PlayerManager playerManager = PlayerManager.getInstance();

        this.selectProfessionButton = new SimpleButton(SimpleItem.newBuilder()
                .material(Material.WRITABLE_BOOK)
                .displayName(RPProfessions.getInstance().getStringHolder().selectProfessionButton)
                .build().make().getItemStack()) {

            @Override
            public boolean action(Player player, final ClickType clickType, ItemStack itemStack) {
                if(playerManager.hasProfession(player)) {
                    Formatter.tell(player,
                            RPProfessions.getInstance().getStringHolder().alreadyHasProfessionMessage
                    );
                } else {
                    playerManager.setProfession(player, profession);
                    Formatter.tell(player,
                            String.format(RPProfessions.getInstance().getStringHolder().setProfessionMessage,
                                    profession.getDisplayName())
                    );
                }

                return false;
            }

        };
        this.workstationsButton = new SimpleButton(SimpleItem.newBuilder()
                .material(Material.CRAFTING_TABLE)
                .displayName(RPProfessions.getInstance().getStringHolder().workstationsButton)
                .build().make().getItemStack()) {

            @Override
            public boolean action(Player player, final ClickType clickType, ItemStack itemStack) {
                new WorkstationsMenu(ProfessionMenu.this, profession).display(player);

                return false;
            }

        };
        this.descriptionButton = new SimpleButton.DummyButton(SimpleItem.newBuilder()
                .material(Material.PAPER)
                .displayName(RPProfessions.getInstance().getStringHolder().infoButton)
                .lore(profession.getDescription())
                .build().make().getItemStack());
        this.insightButton = new SimpleButton(SimpleItem.newBuilder()
                .material(Material.ENDER_EYE)
                .displayName(RPProfessions.getInstance().getStringHolder().insightButton)
                .build()
                .make()
                .getItemStack()) {

            @Override
            public boolean action(Player player, final ClickType clickType, ItemStack itemStack) {
                new InsightMenu(ProfessionMenu.this, profession).display(player);

                return false;
            }

        };
        setSize(3*9);
        setTitle(profession.getDisplayName());
        setCancelActions(true);
        registerButtons(selectProfessionButton, descriptionButton, workstationsButton, insightButton, SimpleButton.DummyButton.FILLING_BUTTON);
    }

    @Override
    protected ItemStack getSlotItem(final int slot) {
        if(slot == 9 + 1) {
            return selectProfessionButton.getIcon();
        }
        if(slot == 9 + 3) {
            return descriptionButton.getIcon();
        }
        if(slot == 9 + 5) {
            return workstationsButton.getIcon();
        }
        if(slot == 9 + 7) {
            return insightButton.getIcon();
        }

        return SimpleButton.DummyButton.FILLING_BUTTON.getIcon();
    }

}
