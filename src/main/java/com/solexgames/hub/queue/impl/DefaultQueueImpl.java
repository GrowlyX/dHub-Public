package com.solexgames.hub.queue.impl;

import com.solexgames.hub.queue.IQueue;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DefaultQueueImpl implements IQueue {

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

    @Override
    public void joinQueue(Player player, String server) {
        player.sendMessage(ChatColor.RED + "Something went terribly wrong while trying to add you to the " + ChatColor.YELLOW + server + ChatColor.RED + " queue.");
    }
}
