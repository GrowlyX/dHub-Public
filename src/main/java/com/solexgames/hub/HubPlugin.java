package com.solexgames.hub;

import com.solexgames.core.CorePlugin;
import com.solexgames.core.enums.ServerType;
import com.solexgames.hub.command.HeadCommand;
import com.solexgames.hub.command.MenuCommand;
import com.solexgames.hub.handler.CosmeticHandler;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

@Getter
public final class HubPlugin extends JavaPlugin {

    private final List<Player> permittedBuilders = new ArrayList<>();
    private final Map<String, AbstractMap.SimpleEntry<Integer, ItemStack>> itemCache = new HashMap<>();

    private ExternalConfig settings;
    private ExternalConfig menus;

    private CosmeticHandler cosmeticHandler;
    private SubMenuHandler subMenuHandler;
    private HubHandler hubHandler;
    private IQueue queueImpl;

    @Override
    public void onEnable() {
        this.settings = new ExternalConfig("settings", this);
        this.menus = new ExternalConfig("menus", this);

        new NeonCommand(this).registerCommand(this);
        new MenuCommand(this).registerCommand(this);
        new HeadCommand(this).registerCommand(this);

        this.cosmeticHandler = new CosmeticHandler(this);
        this.cosmeticHandler.loadArmorCosmetics();
        this.cosmeticHandler.loadParticleCosmetics();

        this.hubHandler = new HubHandler(this);
        this.subMenuHandler = new SubMenuHandler(this);

        this.itemCache.put("enderbutt", ItemUtil.getInventoryItemFromConfig("items.enderbutt", this));
        this.itemCache.put("hub-selector", ItemUtil.getInventoryItemFromConfig("items.hub-selector", this));
        this.itemCache.put("cosmetics", ItemUtil.getInventoryItemFromConfig("items.cosmetics", this));
        this.itemCache.put("server-selector", ItemUtil.getInventoryItemFromConfig("items.server-selector", this));

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

        /*final String version = this.getServer().getVersion();

        if (this.getSettings().getBoolean("tablist.enabled") && CorePlugin.getInstance().getServerManager().getNetwork().equals(ServerType.PVPBAR)) {
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
        }*/

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
