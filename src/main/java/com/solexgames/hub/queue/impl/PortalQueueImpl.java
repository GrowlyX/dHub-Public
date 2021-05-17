package com.solexgames.hub.queue.impl;

import com.google.gson.JsonObject;
import com.solexgames.hub.queue.IQueue;
import me.joeleoli.portal.bukkit.Portal;
import me.joeleoli.portal.shared.jedis.JedisAction;
import me.joeleoli.portal.shared.jedis.JedisChannel;
import me.joeleoli.portal.shared.queue.Queue;
import me.joeleoli.portal.shared.queue.QueueRank;
import org.bukkit.ChatColor;
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

    @Override
    public void joinQueue(Player player, String server) {
        Queue queue = Queue.getByPlayer(player.getUniqueId());

        if (queue != null) {
            player.sendMessage(ChatColor.RED + "Error: You are already in the " + ChatColor.YELLOW + queue.getName() + ChatColor.RED + " queue.");
            return;
        }

        queue = Queue.getByName(server);

        if (queue == null) {
            player.sendMessage(ChatColor.RED + "Error: The queue with the name " + ChatColor.YELLOW + server + ChatColor.RED + " does not exist.");
            return;
        }

        if (queue.getServerData() == null || !queue.getServerData().isOnline()) {
            player.sendMessage(ChatColor.RED + "Error: The queue with the name " + ChatColor.YELLOW + queue.getName() + ChatColor.RED + " is offline.");
            return;
        }

        if (player.hasPermission("queue.priority.bypass")) {
            final JsonObject data = new JsonObject();

            data.addProperty("uuid", player.getUniqueId().toString());
            data.addProperty("server", queue.getName());

            Portal.getInstance().getPublisher().write(JedisChannel.BUKKIT, JedisAction.SEND_PLAYER_SERVER, data);
            return;
        }

        final QueueRank queueRank = Portal.getInstance().getPriorityProvider().getPriority(player);

        final JsonObject rank = new JsonObject();
        rank.addProperty("name", queueRank.getName());
        rank.addProperty("priority", queueRank.getPriority());

        final JsonObject playerJson = new JsonObject();
        playerJson.addProperty("uuid", player.getUniqueId().toString());
        playerJson.add("rank", rank);

        final JsonObject data = new JsonObject();
        data.addProperty("queue", queue.getName());
        data.add("player", playerJson);

        Portal.getInstance().getPublisher().write(JedisChannel.INDEPENDENT, JedisAction.ADD_PLAYER, data);
    }
}
