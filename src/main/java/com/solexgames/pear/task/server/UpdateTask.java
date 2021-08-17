package com.solexgames.pear.task.server;

import com.solexgames.pear.PearSpigotPlugin;
import com.solexgames.pear.task.server.callback.TypeCallback;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author GrowlyX
 * @since 7/10/2021
 */

public abstract class UpdateTask<T> extends BukkitRunnable {

    public UpdateTask() {
        this.runTaskTimerAsynchronously(PearSpigotPlugin.getPlugin(PearSpigotPlugin.class), 0L, 100L);
    }

    @Override
    public void run() {
        final T type = this.calculateValue().call();
        this.setValue(type);
    }

    public abstract String getName();

    public abstract TypeCallback<T> calculateValue();

    public abstract void setValue(T value);

    public abstract Integer getValue();

}
