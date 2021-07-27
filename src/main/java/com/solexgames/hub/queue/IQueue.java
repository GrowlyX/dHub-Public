package com.solexgames.hub.queue;

import org.bukkit.entity.Player;

/**
 * @author GrowlyX
 * @since 4/3/2021
 */

public interface IQueue {

    boolean isInQueue(Player player);

    String getQueueName(Player player);

    String getQueueLane(Player player);

    int getQueuePosition(Player player);

    int getQueuePlayers(Player player);

    void joinQueue(Player player, String server);

}
