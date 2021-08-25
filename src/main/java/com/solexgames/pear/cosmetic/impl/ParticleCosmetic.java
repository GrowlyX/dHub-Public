package com.solexgames.pear.cosmetic.impl;

import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.pear.PearSpigotPlugin;
import com.solexgames.pear.cosmetic.Cosmetic;
import com.solexgames.pear.cosmetic.type.CosmeticType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;

/**
 * @author GrowlyX
 * @since 5/16/2021
 */

@Getter
@RequiredArgsConstructor
public class ParticleCosmetic extends Cosmetic<ParticleEffect> {

    private final ParticleEffect particleEffect;
    private final Material displayMaterial;
    private final PearSpigotPlugin hubPlugin;

    @Override
    public String getName() {
        return StringUtils.capitalize(this.particleEffect.name().replace("_", " ").toLowerCase());
    }

    @Override
    public String getPermission() {
        return "pear.cosmetic.trail." + this.particleEffect.name().replace("_", "-").toLowerCase();
    }

    @Override
    public CosmeticType getCosmeticType() {
        return CosmeticType.PARTICLE;
    }

    @Override
    public void applyTo(Player player, ParticleEffect particleEffect) {
        final BukkitRunnable bukkitRunnable = new ParticleUpdaterRunnable(player, particleEffect);
        bukkitRunnable.runTaskTimer(this.hubPlugin, 2L, 2L);

        this.hubPlugin.getCosmeticHandler().getRunnableHashMap().put(player, bukkitRunnable);
    }

    public ItemBuilder getMenuItemBuilder() {
        return new ItemBuilder(this.displayMaterial)
                .setDurability(this.displayMaterial == Material.INK_SACK ? 15 : 0)
                .setDisplayName(ChatColor.GREEN + this.getName() + " Trail");
    }

    @Getter
    @RequiredArgsConstructor
    private static class ParticleUpdaterRunnable extends BukkitRunnable {

        private final Player player;
        private final ParticleEffect particleEffect;

        @Override
        public void run() {
            final Vector vector = this.player.getEyeLocation().toVector()
                    .normalize().multiply(-1);
            final Location location = this.player.getLocation().clone()
                    .add(0.0D, 1.5D, 0.0D).add(vector);

            this.particleEffect.display(location);
        }
    }
}
