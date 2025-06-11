package com.gilles_m.rp_professions.object;

import com.gilles_m.rp_professions.Identifiable;
import com.gilles_m.rp_professions.object.randomized_object.RandomizedObject;
import com.gilles_m.rp_professions.object.randomized_object.RangeInteger;
import com.gilles_m.rp_professions.object.randomized_object.VillagerTrade;
import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Villager;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
public class VillagerTradeSet implements Identifiable {

    private String id;

    private String world;

    private Villager.Profession profession;

    private RangeInteger tradesPerLevel = new RangeInteger(1);

    private final Set<VillagerTrade> villagerTrades = new HashSet<>();

    protected VillagerTradeSet() { }

    public void addTrade(VillagerTrade villagerTrade) {
        this.villagerTrades.add(villagerTrade);
    }

    /**
     * Generate a random set of trades matching the given villager level.
     *
     * @param villagerLevel the villager level
     * @return a set of villager trades
     */
    public Set<VillagerTrade> getRandomTrades(final int villagerLevel) {
        //Get the trades matching the given level
        final Set<VillagerTrade> validTrades = villagerTrades.stream()
                .filter(trade -> trade.getLevel() == villagerLevel)
                .collect(Collectors.toSet());
        final Set<VillagerTrade> electedTrades = RandomizedObject.randomRelative(validTrades,
                tradesPerLevel.getLow(), tradesPerLevel.getHigh());
        //Add the additional trades
        electedTrades.addAll(RandomizedObject.randomAbsolute(validTrades));

        return electedTrades;
    }

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this)
                .add("world", world)
                .add("profession", profession != null ? profession.toString() : "null")
                .add("tradesPerLevel", tradesPerLevel.toString())
                .add("villagerTrades", villagerTrades.toString())
                .toString();
    }

    @Override
    public final int hashCode() {
        return Objects.hash(world, profession, tradesPerLevel, villagerTrades);
    }

    @Override
    public final boolean equals(Object other) {
        if(other == null) {
            return false;
        }
        if(this == other) {
            return true;
        }
        if(!(other instanceof VillagerTradeSet otherTrade)) {
            return false;
        }

		return Objects.equals(otherTrade.world, this.world)
                && otherTrade.profession == this.profession
                //&& otherTrade.maxTrades == this.maxTrades
                && Objects.equals(otherTrade.tradesPerLevel, this.tradesPerLevel)
                && Objects.equals(otherTrade.villagerTrades, this.villagerTrades);
    }

}
