package com.solexgames.hub;

import com.solexgames.hub.command.MenuCommand;
import com.solexgames.hub.handler.SubMenuHandler;
import com.solexgames.hub.listener.AntiListener;
import com.solexgames.hub.listener.EnderbuttListener;
import com.solexgames.hub.command.NeonCommand;
import com.solexgames.hub.external.ExternalConfig;
import com.solexgames.hub.listener.PlayerListener;
import com.solexgames.hub.handler.HubHandler;
import com.solexgames.hub.queue.IQueue;
import com.solexgames.hub.queue.impl.DefaultQueueImpl;
import com.solexgames.hub.queue.impl.EZQueueImpl;
import com.solexgames.hub.queue.impl.PortalQueueImpl;
import com.solexgames.hub.tablist.TablistAdapter;
import com.solexgames.hub.util.ItemUtil;
import io.github.nosequel.tab.shared.TabHandler;
import io.github.nosequel.tab.v1_10_r1.v1_10_R1TabAdapter;
import io.github.nosequel.tab.v1_12_r1.v1_12_R1TabAdapter;
import io.github.nosequel.tab.v1_14_r1.v1_14_R1TabAdapter;
import io.github.nosequel.tab.v1_15_r1.v1_15_R1TabAdapter;
import io.github.nosequel.tab.v1_16_r3.v1_16_R3TabAdapter;
import io.github.nosequel.tab.v1_7_r4.v1_7_R4TabAdapter;
import io.github.nosequel.tab.v1_8_r3.v1_8_R3TabAdapter;
import io.github.nosequel.tab.v1_9_r1.v1_9_R1TabAdapter;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class HubPlugin extends JavaPlugin {

    private final List<Player> permittedBuilders = new ArrayList<>();

    private ExternalConfig settings;
    private ExternalConfig menus;

    private String enderButt;

    private SubMenuHandler subMenuHandler;
    private HubHandler hubHandler;
    private IQueue queueImpl;

    @Override
    public void onEnable() {
        this.settings = new ExternalConfig("settings", this);
        this.menus = new ExternalConfig("menus", this);

        new NeonCommand(this).registerCommand(this);
        new MenuCommand(this).registerCommand(this);

        this.hubHandler = new HubHandler(this);
        this.subMenuHandler = new SubMenuHandler(this);
        this.enderButt = ItemUtil.getItemFromConfig("items.enderbutt", this)
                .getItemMeta()
                .getDisplayName();

        switch (this.getSettings().getString("queue.plugin")) {
            case "PORTAL":
                this.queueImpl = new PortalQueueImpl();
                break;
            case "EZQUEUE":
                this.queueImpl = new EZQueueImpl();
                break;
            default:
                this.queueImpl = new DefaultQueueImpl();
        }

        final String version = this.getServer().getVersion();

        if (this.getSettings().getBoolean("tablist.enabled")) {
            final TablistAdapter adapter = new TablistAdapter();

            if (version.contains("1.7")) {
                new TabHandler(new v1_7_R4TabAdapter(), adapter, this, 20L);
            } else if (version.contains("1.8")) {
                new TabHandler(new v1_8_R3TabAdapter(), adapter, this, 20L);
            } else if (version.contains("1.9")) {
                new TabHandler(new v1_9_R1TabAdapter(), adapter, this, 20L);
            } else if (version.contains("1.10")) {
                new TabHandler(new v1_10_R1TabAdapter(), adapter, this, 20L);
            } else if (version.contains("1.12")) {
                new TabHandler(new v1_12_R1TabAdapter(), adapter, this, 20L);
            } else if (version.contains("1.14")) {
                new TabHandler(new v1_14_R1TabAdapter(), adapter, this, 20L);
            } else if (version.contains("1.15")) {
                new TabHandler(new v1_15_R1TabAdapter(), adapter, this, 20L);
            } else if (version.contains("1.16")) {
                new TabHandler(new v1_16_R3TabAdapter(), adapter, this, 20L);
            }
        }

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        if (this.hubHandler.isAntiListeners()) {
            this.getServer().getPluginManager().registerEvents(new AntiListener(this), this);
        }

        if (this.hubHandler.isEnderButtEnabled()) {
            this.getServer().getPluginManager().registerEvents(new EnderbuttListener(this), this);
        }

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    public void reloadAllConfigs() {
        this.settings.reloadConfig();
        this.menus.reloadConfig();

        this.reloadConfig();

        this.getLogger().info("Reloaded all configs!");
    }
}
