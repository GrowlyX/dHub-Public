package com.solexgames.hub.queue.impl;

import com.solexgames.hub.queue.IQueue;
import org.bukkit.entity.Player;

public class EZQueueImpl implements IQueue {

    @Override
    public boolean isInQueue(Player player) {
        return false;
    }

    @Override
    public String getQueueName(Player player) {
        return "";
    }

    @Override
    public int getQueuePosition(Player player) {
        return 0;
    }

    @Override
    public int getQueuePlayers(Player player) {
        return 0;
    }
}
