package com.gilles_m.rp_professions.object;

import com.gilles_m.rp_professions.Identifiable;
import com.gilles_m.rp_professions.manager.DependencyManager;
import com.gilles_m.rp_professions.manager.ItemManager;
import com.gilles_m.rp_professions.object.station.Workstation;
import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.*;

/**
 * Represent a (customizable) player's profession.
 */
@Setter
public class Profession implements Identifiable {

	@Getter
	private String id;

	@Getter
	private List<Workstation> workstations;

	@Getter
	private String displayName;

	@Getter
	private Material icon;

	@Getter
	private List<String> description = new ArrayList<>();

	private String exampleCraftId;

	private final Set<ProfessionItem> items = new HashSet<>();

	protected Profession() { }

	public Set<ProfessionItem> getItems() {
		return Collections.unmodifiableSet(items);
	}

	/**
	 * Get the workstation matching the given block.
	 *
	 * @param block the block
	 * @return an optional containing the matching workstation or an empty optional
	 */
	public Optional<Workstation> getWorkstation(Block block) {
		final Optional<String> oraxenBlockId = DependencyManager.getInstance().getOraxenIdFromBlock(block);

		return oraxenBlockId.map(oraxenId -> workstations.stream()
				.filter(workstation -> workstation.getMaterials().contains("oraxen:" + oraxenId))
				.findFirst())
				.orElseGet(() -> workstations.stream()
				.filter(workstation -> workstation.getMaterials().contains(block.getType().toString()))
				.findFirst());

	}

	public Optional<ProfessionItem> getExampleItem() {
		if(exampleCraftId == null || exampleCraftId.isBlank()) {
			return Optional.empty();
		}

		return ItemManager.getInstance().get(exampleCraftId);
	}

	@Override
	public final String toString() {
		return MoreObjects.toStringHelper(this)
				.add("id", id)
				.add("workstations", workstations.toString())
				.add("displayName", displayName)
				.add("icon", icon.toString())
				.toString();
	}

}
