package com.solexgames.queue.impl;

import com.solexgames.queue.IQueue;
import org.bukkit.entity.Player;

import java.util.List;

public class EZQueueImpl implements IQueue {

    @Override
    public boolean isInQueue(Player player) {
        return false;
    }

    @Override
    public String getQueueName(Player player) {
        return null;
    }

    @Override
    public int getQueuePosition(Player player) {
        return 0;
    }

    @Override
    public List<Player> getQueuePlayers(Player player) {
        return null;
    }
}
