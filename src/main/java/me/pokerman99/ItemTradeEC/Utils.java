package me.pokerman99.ItemTradeEC;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.UUID;

public class Utils {

    public static String color(String string) {
        return TextSerializers.FORMATTING_CODE.serialize(Text.of(string));
    }

    public static void sendMessage(CommandSource sender, String message) {
        if (sender == null) { return; }
        sender.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(color(message)));
    }

    public static boolean isOnCooldown(Player player) {
        long cooldown = Long.valueOf(player.getOption("itemtrade-cooldown").orElse("-1"));

        if (System.currentTimeMillis() < cooldown) {
            Utils.sendMessage(player, "&cYou need to wait another " + Utils.timeDiffFormat((cooldown - System.currentTimeMillis()) / 1000, true) + " before you can use that command again!");
            return true;
        }

        return false;
    }

    public static String getFormattedItemName(String itemId) {
        String s = itemId.replaceAll("minecraft:", "").replaceAll("pixelmon:", "");
        StringBuilder sb = new StringBuilder(s);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));

        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '_') {
                sb.setCharAt(i, ' ');
                sb.setCharAt(i+1, Character.toUpperCase(sb.charAt(i+1)));
            }
        }

        return sb.toString();
    }

    public static void setCooldown(Player player, int hours) {
        Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user " + player.getName() + " meta unset itemtrade-cooldown");

        Task.builder().delayTicks(2).execute(task -> {
            int cooldown = player.hasPermission("itemtradeec.halfcooldown") ? 3600*hours*500 : 3600*hours*1000;
            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user " + player.getName() + " meta set itemtrade-cooldown " + (cooldown + System.currentTimeMillis()));
        }).submit(Main.getInstance());
    }

    public static String timeDiffFormat(long timeDiffSeconds, boolean includeSeconds) {
        String timeFormat;
        int seconds = (int) timeDiffSeconds % 60;
        timeDiffSeconds = timeDiffSeconds / 60;
        int minutes = (int) timeDiffSeconds % 60;
        timeDiffSeconds = timeDiffSeconds / 60;
        int hours = (int) timeDiffSeconds % 24;
        timeDiffSeconds = timeDiffSeconds / 24;
        int days = (int) timeDiffSeconds;

        if (days > 7) {
            timeFormat = days + " days";
        } else if (days > 0) {
            timeFormat = days + "d " + hours + "h";
        } else if (days == 0 && hours > 0) {
            if (includeSeconds) {
                timeFormat = hours + "h " + minutes + "m " + seconds + "s";
            } else {
                timeFormat = hours + "h " + minutes + "m";
            }
        } else if (days == 0 && hours == 0 && minutes > 0) {
            if (includeSeconds) {
                timeFormat = minutes + "m " + seconds + "s";
            } else {
                timeFormat = minutes + "m";
            }
        } else {
            timeFormat = seconds + "s";
        }

        return timeFormat;
    }
}