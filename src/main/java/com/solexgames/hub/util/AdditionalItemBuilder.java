package com.solexgames.hub.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author GrowlyX
 * @since 8/5/2021
 */

public class AdditionalItemBuilder {

    private final Map<ItemStack, Map.Entry<Integer, Consumer<Player>>> itemStackEntryMap = new HashMap<>();

    public AdditionalItemBuilder add(Integer integer, ItemStack itemStack, Consumer<Player> consumer) {
        this.itemStackEntryMap.put(itemStack, new Map.Entry<Integer, Consumer<Player>>() {
            @Override
            public Integer getKey() {
                return integer;
            }

            @Override
            public Consumer<Player> getValue() {
                return consumer;
            }

            @Override
            public Consumer<Player> setValue(Consumer<Player> value) {
                return consumer;
            }
        });

        return this;
    }

    public Map<ItemStack, Map.Entry<Integer, Consumer<Player>>> build() {
        return this.itemStackEntryMap;
    }
}
