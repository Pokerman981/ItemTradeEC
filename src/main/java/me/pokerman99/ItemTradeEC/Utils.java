package me.pokerman99.ItemTradeEC;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import org.spongepowered.api.command.CommandSource;
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

    public static MetaData getMetaData(UUID uuid) {
        LuckPermsApi api = LuckPerms.getApi();

        User user = api.getUserSafe(uuid).orElse(null);
        if (user == null) {
            return null;
        }

        Contexts contexts = api.getContextForUser(user).orElse(null);
        if (contexts == null) {
            return null;
        }

        return user.getCachedData().getMetaData(contexts);
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
