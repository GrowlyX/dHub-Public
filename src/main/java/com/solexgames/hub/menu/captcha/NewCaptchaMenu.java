package com.solexgames.hub.menu.captcha;

import com.solexgames.core.CorePlugin;
import com.solexgames.core.util.external.Button;
import com.solexgames.core.util.external.Menu;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author GrowlyX
 * @since 6/8/2021
 */

public class NewCaptchaMenu extends Menu {

    private int id;

    private ItemStack itemStack;

    public NewCaptchaMenu() {
        this.generateOneTimeWoolId();
    }

    @Override
    public int getSize() {
        return 36;
    }

    @Override
    public String getTitle(Player player) {
        final ChatColor chatColor = WoolUtil.convertWoolDataToChatColor(this.id);

        return "Click the " + chatColor + StringUtils.capitalize(chatColor.name().toLowerCase().split("_")) + " Glass";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttonMap = new HashMap<>();



        return null;
    }

    public void generateOneTimeWoolId() {
        this.id = ThreadLocalRandom.current().nextInt(15);
    }

    public ItemStack getItemStack() {
        return this.itemStack != null ? this.itemStack : (this.itemStack = new ItemStack(Material.STAINED_GLASS_PANE, this.id));
    }

    @UtilityClass
    private class WoolUtil {

        private static final List<ChatColor> woolColors = Arrays.asList(ChatColor.WHITE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE, ChatColor.AQUA, ChatColor.YELLOW, ChatColor.GREEN, ChatColor.LIGHT_PURPLE, ChatColor.DARK_GRAY, ChatColor.GRAY, ChatColor.DARK_AQUA, ChatColor.DARK_PURPLE, ChatColor.BLUE, ChatColor.BLACK, ChatColor.DARK_GREEN, ChatColor.RED, ChatColor.BLACK);

        public static int convertChatColorToWoolData(ChatColor color) {
            if (color == ChatColor.DARK_RED) {
                color = ChatColor.RED;
            }

            return woolColors.indexOf(color);
        }

        public static ChatColor convertWoolDataToChatColor(int color) {
            return woolColors.stream()
                    .filter(color1 -> convertChatColorToWoolData(color1) == color)
                    .findFirst().orElse(ChatColor.WHITE);
        }
    }

    @UtilityClass
    public class StringUtils {

        /**
         * Capitalize a word
         *
         * @param strings the strings to capitalize
         * @return the capitalized string
         */
        public String capitalize(String... strings) {
            final StringBuilder builder = new StringBuilder();

            for (String string : strings) {
                builder
                        .append(Character.toUpperCase(string.toCharArray()[0]))
                        .append(string.substring(1));
            }

            return builder.toString();
        }

        /**
         * Repeat a string an x amount of times
         *
         * @param string the string to repeat
         * @param amount the amount of times the string should be repeated
         * @return the string generated with the provided arguments
         */
        public String repeat(String string, int amount) {
            final StringBuilder builder = new StringBuilder();

            for (int i = 0; i < amount; i++) {
                builder.append(string);
            }

            return builder.toString();
        }

        /**
         * Abbreviate a string
         *
         * @param string the string to abbreviate
         * @return the abbreviated string
         */
        public String abbreviate(String string) {
            final String[] split = string.split(" ");
            final StringBuilder toReturn = new StringBuilder();

            for (String current : split) {
                toReturn
                        .append(current.toCharArray()[0])
                        .append(".");
            }

            return toReturn.toString();
        }

        /**
         * Wrap the string to a multi-line string
         *
         * @param string     the string to wrap
         * @param amount     the amount of words per line
         * @param splitRegex the regex to use to split the string
         * @return the wrapped string
         */
        public String[] wrap(String string, int amount, String splitRegex) {
            final String[] split = string.split(splitRegex);
            final String[] toReturn = new String[split.length / amount];

            int counter = 0;
            int counter2 = 0;

            for (String current : split) {
                if (counter2++ == amount) {
                    counter++;
                    counter2 = 0;
                }

                if (toReturn[counter] == null) {
                    toReturn[counter] = current;
                } else {
                    toReturn[counter] += current;
                }

                if (counter2 != amount) {
                    toReturn[counter] += " ";
                }
            }

            return toReturn;
        }

        /**
         * Wrap the string to a multi-line string
         *
         * @param string the string to wrap
         * @param amount the amount of words per line
         * @return the wrapped string
         */
        public String[] wrap(String string, int amount) {
            return wrap(string, amount, " ");
        }
    }
}
