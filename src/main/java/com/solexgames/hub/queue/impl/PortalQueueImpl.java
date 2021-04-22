package com.solexgames.hub.queue.impl;

import com.solexgames.hub.queue.IQueue;
import me.joeleoli.portal.shared.queue.Queue;
import org.bukkit.entity.Player;

public class PortalQueueImpl implements IQueue {

    @Override
    public boolean isInQueue(Player player) {
        return Queue.getByPlayer(player.getUniqueId()) != null;
    }

    @Override
    public String getQueueName(Player player) {
        return (Queue.getByPlayer(player.getUniqueId()) != null ? Queue.getByPlayer(player.getUniqueId()).getName() : "None");
    }

    @Override
    public int getQueuePosition(Player player) {
        return (Queue.getByPlayer(player.getUniqueId()) != null ? Queue.getByPlayer(player.getUniqueId()).getPosition(player.getUniqueId()) : 0);
    }

    @Override
    public int getQueuePlayers(Player player) {
        return (Queue.getByPlayer(player.getUniqueId()) != null ? Queue.getByPlayer(player.getUniqueId()).getPlayers().size() : 0);
    }
}
