package com.solexgames.hub.module.impl;

import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author GrowlyX
 * @since 8/5/2021
 */

public interface HubModuleScoreboardAdapter {

    String getNewTitle();

    List<String> getNewLines(Player player);

}
