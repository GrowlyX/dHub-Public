package com.solexgames.hub.processor.adapter;

import com.solexgames.core.util.LocationUtil;
import com.solexgames.lib.processor.config.internal.adapt.ObjectAdapter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author GrowlyX
 * @since 6/29/2021
 */

public class PositionObjectAdapter implements ObjectAdapter<Location, String> {

    @Override
    public Location read(String key, ConfigurationSection configurationSection) {
        final String value = configurationSection.getString(key);

        if (value.equals("Unknown")) {
            return null;
        }

        return LocationUtil.getLocationFromString(value).orElse(null);
    }

    @Override
    public String write(Location position) {
        return LocationUtil.getStringFromLocation(position).orElse("Unknown");
    }
}
