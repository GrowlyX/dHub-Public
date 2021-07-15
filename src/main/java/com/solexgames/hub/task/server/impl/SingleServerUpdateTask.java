package com.solexgames.hub.task.server.impl;

import com.solexgames.core.server.NetworkServer;
import com.solexgames.hub.task.server.UpdateTask;
import com.solexgames.hub.task.server.callback.TypeCallback;
import com.solexgames.lib.commons.CommonLibsBukkit;
import com.solexgames.lib.commons.hologram.CommonsHologram;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.lucko.helper.Services;
import me.lucko.helper.messaging.bungee.BungeeCord;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author GrowlyX
 * @since 7/10/2021
 */

@Getter
@RequiredArgsConstructor
public class SingleServerUpdateTask extends UpdateTask<Integer> {

    private final String hologramName;
    private final String server;

    private Integer value;

    @Override
    public String getName() {
        return this.hologramName;
    }

    @Override
    public void run() {
        final CommonsHologram hologram = CommonLibsBukkit.getInstance().getHologramManager().fetchHologram(this.hologramName);

        if (hologram == null) {
            return;
        }

        super.run();

        final List<String> stringList = new ArrayList<>();

        stringList.add(ChatColor.GOLD + ChatColor.BOLD.toString() + this.hologramName.replace("_", " "));
        stringList.add(this.value + " Players");

        hologram.setLines(stringList);
    }

    @Override
    public TypeCallback<Integer> calculateValue() {
        return () -> {
            final NetworkServer server = NetworkServer.getByName(this.server);

            return server == null ? 0 : server.getOnlinePlayers();
        };
    }

    @Override
    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
