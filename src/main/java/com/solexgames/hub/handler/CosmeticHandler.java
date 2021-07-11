package com.solexgames.hub.handler;

import com.cryptomorin.xseries.XMaterial;
import com.solexgames.core.CorePlugin;
import com.solexgames.core.player.ranks.Rank;
import com.solexgames.hub.HubPlugin;
import com.solexgames.hub.cosmetic.Cosmetic;
import com.solexgames.hub.cosmetic.impl.ArmorCosmetic;
import com.solexgames.hub.cosmetic.impl.ParticleCosmetic;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class CosmeticHandler {

    private final Map<Player, BukkitRunnable> runnableHashMap = new HashMap<>();

    private final Map<Rank, ArmorCosmetic> armorCosmeticMap = new HashMap<>();
    private final Map<Integer, Color> colorHashMap = new HashMap<>();
    private final Map<ParticleEffect, ParticleCosmetic> particleCosmeticMap = new HashMap<>();

    private final List<Cosmetic<?>> cosmeticList = new ArrayList<>();

    private ArmorCosmetic chromaCosmetic;

    private final HubPlugin plugin;

    public void loadArmorCosmetics() {
        CorePlugin.getInstance().getRankManager().getRanks()
                .forEach(rank -> {
                    final ArmorCosmetic cosmetic = new ArmorCosmetic(rank, rank.getName());

                    this.cosmeticList.add(cosmetic);
                    this.armorCosmeticMap.put(rank, cosmetic);
                });

        this.chromaCosmetic = new ArmorCosmetic(null, "Chroma");

        final double range = 255D;
        final double f = (6.48D / range);

        for (int i = 0; i < range; ++i) {
            final double r = Math.sin(f * i + 0.0D) * 127.0D + 128.0D;
            final double g = Math.sin(f * i + (2 * Math.PI / 3)) * 127.0D + 128.0D;
            final double b = Math.sin(f * i + (4 * Math.PI / 3)) * 127.0D + 128.0D;

            this.colorHashMap.put(i, Color.fromRGB((int) r, (int) g, (int) b));
        }
    }

    public void loadParticleCosmetics() {
        this.particleCosmeticMap.put(ParticleEffect.HEART, new ParticleCosmetic(ParticleEffect.HEART, Material.REDSTONE, this.plugin));
        this.particleCosmeticMap.put(ParticleEffect.NOTE, new ParticleCosmetic(ParticleEffect.NOTE, Material.NOTE_BLOCK, this.plugin));
        this.particleCosmeticMap.put(ParticleEffect.FIREWORKS_SPARK, new ParticleCosmetic(ParticleEffect.FIREWORKS_SPARK, Material.FIREWORK, this.plugin));
        this.particleCosmeticMap.put(ParticleEffect.SLIME, new ParticleCosmetic(ParticleEffect.SLIME, Material.SLIME_BALL, this.plugin));
        this.particleCosmeticMap.put(ParticleEffect.WATER_SPLASH, new ParticleCosmetic(ParticleEffect.WATER_SPLASH, Material.WATER_BUCKET, this.plugin));
        this.particleCosmeticMap.put(ParticleEffect.FLAME, new ParticleCosmetic(ParticleEffect.FLAME, Material.BLAZE_POWDER, this.plugin));
        this.particleCosmeticMap.put(ParticleEffect.CLOUD, new ParticleCosmetic(ParticleEffect.CLOUD, Material.INK_SACK, this.plugin));
        this.particleCosmeticMap.put(ParticleEffect.SMOKE_NORMAL, new ParticleCosmetic(ParticleEffect.SMOKE_NORMAL, XMaterial.GUNPOWDER.parseMaterial(), this.plugin));
    }
}
