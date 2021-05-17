package com.solexgames.hub.cosmetic.impl;

import com.solexgames.core.util.Color;
import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.hub.HubPlugin;
import com.solexgames.hub.cosmetic.Cosmetic;
import com.solexgames.hub.cosmetic.CosmeticType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xenondevs.particle.ParticleEffect;

/**
 * @author GrowlyX
 * @since 5/16/2021
 */

@Getter
@RequiredArgsConstructor
public class ParticleCosmetic extends Cosmetic<ParticleEffect> {

    private final ParticleEffect particleEffect;
    private final HubPlugin hubPlugin;

    @Override
    public String getName() {
        return StringUtils.capitalize(this.particleEffect.name().replace("_", " ").toLowerCase());
    }

    @Override
    public String getPermission() {
        return "neon.cosmetic.particle." + this.particleEffect.name().replace("_", "-").toLowerCase();
    }

    @Override
    public CosmeticType getCosmeticType() {
        return CosmeticType.PARTICLE;
    }

    @Override
    public void applyTo(Player player, ParticleEffect particleEffect) {
        final BukkitRunnable bukkitRunnable = new ParticleUpdaterRunnable(player, particleEffect);
        bukkitRunnable.runTaskTimer(this.hubPlugin, 5L, 5L);

        this.hubPlugin.getCosmeticHandler().getRunnableHashMap().put(player, bukkitRunnable);
    }

    @Override
    public void removeFrom(Player player) {
        // not used
    }

    public ItemBuilder getMenuItemBuilder() {
        return new ItemBuilder(Material.BLAZE_ROD)
                .setDisplayName(Color.MAIN_COLOR + this.getName() + " Cosmetic");
    }

    @Getter
    @RequiredArgsConstructor
    private static class ParticleUpdaterRunnable extends BukkitRunnable {

        private final Player player;
        private final ParticleEffect particleEffect;

        @Override
        public void run() {
            this.particleEffect.display(this.player.getLocation().add(0.0D, 2.2D, 0.0D));
        }
    }
}
