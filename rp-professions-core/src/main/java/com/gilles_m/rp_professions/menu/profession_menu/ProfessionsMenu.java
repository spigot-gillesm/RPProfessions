package com.gilles_m.rp_professions.menu.profession_menu;

import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.manager.PlayerManager;
import com.gilles_m.rp_professions.manager.ProfessionManager;
import com.gilles_m.rp_professions.object.Profession;
import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.gui_lib.ListingMenu;
import com.github.spigot_gillesm.gui_lib.SimpleButton;
import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This menu shows all available professions. This is the first accessible menu when using the professions command.
 */
public class ProfessionsMenu extends ListingMenu {

	public ProfessionsMenu(SimpleMenu parent) {
		super(parent);

		setTitle(RPProfessions.getInstance().getStringHolder().selectProfessionMenuTitle);
		setSize(3*9);
		setCancelActions(true);
	}

	public ProfessionsMenu() {
		super();

		setTitle(RPProfessions.getInstance().getStringHolder().selectProfessionMenuTitle);
		setSize(3*9);
		setCancelActions(true);
	}

	@Override
	protected List<SimpleButton> generateButtons() {
		final List<SimpleButton> buttons = new ArrayList<>();
		final PlayerManager playerManager = PlayerManager.getInstance();

		//Generate each profession menu button
		for(final Profession profession : ProfessionManager.getInstance().getAll()) {
			buttons.add(new SimpleButton(SimpleItem.newBuilder()
					.material(profession.getIcon())
					.displayName(profession.getDisplayName())
					.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
					.build().make().getItemStack()) {

				@Override
				public boolean action(Player player, final ClickType click, ItemStack draggedItem) {
					new ProfessionMenu(ProfessionsMenu.this, profession).display(player);

					return false;
				}

			});
		}
		//Generate the forget profession button
		buttons.add(new SimpleButton(SimpleItem.newBuilder()
				.material(Material.WRITABLE_BOOK)
				.displayName(RPProfessions.getInstance().getStringHolder().forgetProfessionButton)
				.lore(RPProfessions.getInstance().getStringHolder().forgetProfessionButtonLore)
				.build()
				.make()
				.getItemStack()) {

			@Override
			public boolean action(Player player, final ClickType click, ItemStack draggedItem) {
				final Optional<Profession> profession = playerManager.getProfession(player);

				if(profession.isPresent() && click == ClickType.SHIFT_RIGHT) {
					Formatter.tell(player,
							String.format(RPProfessions.getInstance().getStringHolder().forgetProfessionMessage,
									profession.get().getDisplayName())
					);
					playerManager.removeProfession(player);
				}

				return false;
			}

		});

		return buttons;
	}

}
