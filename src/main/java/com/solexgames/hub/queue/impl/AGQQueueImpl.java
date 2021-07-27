package com.solexgames.hub.queue.impl;

import com.solexgames.hub.queue.IQueue;
import com.solexgames.queue.QueueBukkit;
import com.solexgames.queue.commons.model.impl.CachedQueuePlayer;
import com.solexgames.queue.commons.queue.impl.ParentQueue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class AGQQueueImpl implements IQueue {

    @Override
    public boolean isInQueue(Player player) {
        return this.getParent(player) != null;
    }

    @Override
    public String getQueueName(Player player) {
        return this.getParent(player).getFancyName();
    }

    @Override
    public int getQueuePosition(Player player) {
        final CachedQueuePlayer cachedQueuePlayer = QueueBukkit.getInstance().getPlayerHandler().getByPlayer(player);

        return this.getParent(player).getChildQueue(cachedQueuePlayer).orElse(null).getPosition(cachedQueuePlayer);
    }

    @Override
    public int getQueuePlayers(Player player) {
        final CachedQueuePlayer cachedQueuePlayer = QueueBukkit.getInstance().getPlayerHandler().getByPlayer(player);

        return this.getParent(player).getChildQueue(cachedQueuePlayer).orElse(null).getAllQueued();
    }

    @Override
    public String getQueueLane(Player player) {
        final CachedQueuePlayer cachedQueuePlayer = QueueBukkit.getInstance().getPlayerHandler().getByPlayer(player);

        return this.getParent(player).getChildQueue(cachedQueuePlayer).orElse(null).getFancyName();
    }

    public ParentQueue getParent(Player player) {
        final CachedQueuePlayer cachedQueuePlayer = QueueBukkit.getInstance().getPlayerHandler().getByPlayer(player);

        if (cachedQueuePlayer != null) {
            final List<ParentQueue> parentQueueList = QueueBukkit.getInstance()
                    .getQueueHandler().fetchPlayerQueuedIn(cachedQueuePlayer);

            if (parentQueueList.size() > 0) {
                return parentQueueList.get(0);
            }
        }

        return null;
    }

    @Override
    public void joinQueue(Player player, String server) {
        Bukkit.dispatchCommand(player, "joinqueue " + server);
    }
}
