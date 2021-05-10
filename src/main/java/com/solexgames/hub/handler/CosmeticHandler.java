package com.solexgames.hub.handler;

import com.solexgames.core.player.ranks.Rank;
import com.solexgames.hub.cosmetic.Cosmetic;
import com.solexgames.hub.cosmetic.impl.ArmorCosmetic;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class CosmeticHandler {

    private final Map<Rank, ArmorCosmetic> armorCosmeticMap = new HashMap<>();
    private final List<Cosmetic<?>> cosmeticList = new ArrayList<>();

    public void loadArmorCosmetics() {
        Rank.getRanks().stream()
                .sorted(Comparator.comparingInt(Rank::getWeight).reversed())
                .collect(Collectors.toList())
                .forEach(rank -> {
                    final ArmorCosmetic cosmetic = new ArmorCosmetic(rank, rank.getName(), "neon.cosmetic." + rank.getName().toLowerCase());

                    this.cosmeticList.add(cosmetic);
                    this.armorCosmeticMap.put(rank, cosmetic);
                });
    }
}
