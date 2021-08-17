package com.solexgames.pear.task.server.impl;

import com.solexgames.core.CorePlugin;
import com.solexgames.core.enums.NetworkServerType;
import com.solexgames.core.server.NetworkServer;
import com.solexgames.pear.task.server.UpdateTask;
import com.solexgames.pear.task.server.callback.TypeCallback;
import com.solexgames.lib.commons.CommonLibsBukkit;
import com.solexgames.lib.commons.hologram.CommonsHologram;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GrowlyX
 * @since 7/10/2021
 */

@Getter
@RequiredArgsConstructor
public class LargescaleServerUpdateTask extends UpdateTask<Integer> {

    private final String hologramName;
    private final NetworkServerType type;

    private Integer value;

    @Override
    public void run() {
        final CommonsHologram hologram = CommonLibsBukkit.getInstance().getHologramManager().fetchHologram(this.hologramName);

        if (hologram == null) {
            return;
        }

        super.run();

        final List<String> stringList = new ArrayList<>();

        stringList.add(ChatColor.GOLD + ChatColor.BOLD.toString() + this.hologramName);
        stringList.add(this.value + " Players");

        hologram.setLines(stringList);
    }

    @Override
    public String getName() {
        return this.hologramName;
    }

    @Override
    public TypeCallback<Integer> calculateValue() {
        return () -> CorePlugin.getInstance().getServerManager().getNetworkServers()
                .stream().filter(networkServer -> networkServer.getServerType().equals(this.type))
                .mapToInt(NetworkServer::getOnlinePlayers).sum();
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
