package com.solexgames.hub.module.impl;

import me.lucko.helper.Events;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @author GrowlyX
 * @since 8/5/2021
 */

public interface HubModuleItemAdapter {

    Map<ItemStack, Map.Entry<Integer, Consumer<Player>>> getAdditionalItemsAndClickEvents();

    default void handle(Player player) {
        this.getAdditionalItemsAndClickEvents().forEach((itemStack, integerConsumerEntry) -> {
            player.getInventory().setItem(
                    integerConsumerEntry.getKey(),
                    itemStack
            );
        });
    }

    default void init() {
        this.getAdditionalItemsAndClickEvents().forEach((itemStack, integerConsumerEntry) -> {
            Events.subscribe(PlayerInteractEvent.class)
                    .filter(event -> event.getAction().name().contains("RIGHT"))
                    .filter(event -> event.getItem().getItemMeta() != null
                            && event.getItem().getItemMeta().getDisplayName()
                            .equals(itemStack.getItemMeta().getDisplayName())
                    ).handler(event -> {
                        integerConsumerEntry.getValue().accept(event.getPlayer());
                    });
        });
    }

}
