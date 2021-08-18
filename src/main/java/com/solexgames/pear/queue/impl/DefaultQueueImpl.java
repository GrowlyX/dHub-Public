package com.solexgames.pear.queue.impl;

import com.solexgames.pear.PearSpigotConstants;
import com.solexgames.pear.queue.IQueue;
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
    public String getQueueLane(Player player) {
        return "Normal";
    }

    @Override
    public void joinQueue(Player player, String server) {
        player.sendMessage(PearSpigotConstants.CHAT_PREFIX + ChatColor.RED + "We couldn't add you to a queue, sorry.");
    }
}
