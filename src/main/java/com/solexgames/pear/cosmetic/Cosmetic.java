package com.solexgames.pear.cosmetic;

import org.bukkit.entity.Player;

/**
 * @author GrowlyX
 * @since 5/16/2021
 *
 * @param <K> Type parameter for the external cosmetic option
 */

public abstract class Cosmetic<K> {

    public abstract String getName();
    public abstract String getPermission();

    public abstract CosmeticType getCosmeticType();

    public abstract void applyTo(Player player, K boundParam);

}
