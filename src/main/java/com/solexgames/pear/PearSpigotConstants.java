package com.solexgames.pear;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

/**
 * @author GrowlyX
 * @since 8/18/2021
 */

@UtilityClass
public class PearSpigotConstants {

    public static final String CHAT_PREFIX = ChatColor.GOLD + ChatColor.BOLD.toString() + "Pear " + ChatColor.GRAY + ChatColor.BOLD + "»" + " " + ChatColor.YELLOW;

    public static final String SERVER_NAME = "Tropic";
    public static final String RULES_PAGE = "www.tropic.gg/rules";

    public static final String DID_NOT_RULE_AGREE = ChatColor.RED + "Please reconnect once you've agreed to our rules.";
    public static final String RULE_AGREED = ChatColor.GREEN + "Thanks for agreeing to our rules, have fun!";

}
