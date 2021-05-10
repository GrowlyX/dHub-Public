package com.solexgames.hub.cosmetic;

import org.bukkit.entity.Player;

public abstract class Cosmetic<K> {

    public abstract String getName();
    public abstract String getPermission();

    public abstract CosmeticType getCosmeticType();

    public abstract void applyTo(Player player, K boundParam);
    public abstract void removeFrom(Player player);

}
