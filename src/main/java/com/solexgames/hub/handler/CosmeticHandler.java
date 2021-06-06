package com.solexgames.hub.handler;

import com.solexgames.core.CorePlugin;
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
        CorePlugin.getInstance().getRankManager().getRanks().stream()
                .sorted(Comparator.comparingInt(Rank::getWeight).reversed())
                .collect(Collectors.toList())
                .forEach(rank -> {
                    final ArmorCosmetic cosmetic = new ArmorCosmetic(rank, rank.getName());

                    this.cosmeticList.add(cosmetic);
                    this.armorCosmeticMap.put(rank, cosmetic);
                });
    }

    public void loadParticleCosmetics() {
        this.particleCosmeticMap.put(ParticleEffect.HEART, new ParticleCosmetic(ParticleEffect.HEART, this.plugin));
        this.particleCosmeticMap.put(ParticleEffect.NOTE, new ParticleCosmetic(ParticleEffect.NOTE, this.plugin));
        this.particleCosmeticMap.put(ParticleEffect.FIREWORKS_SPARK, new ParticleCosmetic(ParticleEffect.FIREWORKS_SPARK, this.plugin));
        this.particleCosmeticMap.put(ParticleEffect.SLIME, new ParticleCosmetic(ParticleEffect.SLIME, this.plugin));
        this.particleCosmeticMap.put(ParticleEffect.WATER_SPLASH, new ParticleCosmetic(ParticleEffect.WATER_SPLASH, this.plugin));
        this.particleCosmeticMap.put(ParticleEffect.FLAME, new ParticleCosmetic(ParticleEffect.FLAME, this.plugin));
        this.particleCosmeticMap.put(ParticleEffect.CLOUD, new ParticleCosmetic(ParticleEffect.CLOUD, this.plugin));
        this.particleCosmeticMap.put(ParticleEffect.SMOKE_NORMAL, new ParticleCosmetic(ParticleEffect.SMOKE_NORMAL, this.plugin));
    }
}
