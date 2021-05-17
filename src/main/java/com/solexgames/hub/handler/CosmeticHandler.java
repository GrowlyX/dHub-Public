package com.solexgames.hub.handler;

import com.solexgames.core.player.ranks.Rank;
import com.solexgames.hub.HubPlugin;
import com.solexgames.hub.cosmetic.Cosmetic;
import com.solexgames.hub.cosmetic.impl.ArmorCosmetic;
import com.solexgames.hub.cosmetic.impl.ParticleCosmetic;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.data.ParticleData;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class CosmeticHandler {

    private final Map<Player, BukkitRunnable> runnableHashMap = new HashMap<>();

    private final Map<Rank, ArmorCosmetic> armorCosmeticMap = new HashMap<>();
    private final Map<ParticleEffect, ParticleCosmetic> particleCosmeticMap = new HashMap<>();

    private final List<Cosmetic<?>> cosmeticList = new ArrayList<>();

    private final HubPlugin plugin;

    public void clearCosmetics() {
        this.cosmeticList.clear();
        this.armorCosmeticMap.clear();
    }

    public void loadArmorCosmetics() {
        Rank.getRanks().stream()
                .sorted(Comparator.comparingInt(Rank::getWeight).reversed())
                .collect(Collectors.toList())
                .forEach(rank -> {
                    final ArmorCosmetic cosmetic = new ArmorCosmetic(rank, rank.getName());

                    this.cosmeticList.add(cosmetic);
                    this.armorCosmeticMap.put(rank, cosmetic);
                });
    }

    public void loadParticleCosmetics() {
        Arrays.stream(ParticleEffect.values())
                .filter(particleEffect -> !particleEffect.getFieldName().equals("NONE"))
                .forEachOrdered(particleEffect -> this.particleCosmeticMap.put(particleEffect, new ParticleCosmetic(particleEffect, this.plugin)));
    }
}
