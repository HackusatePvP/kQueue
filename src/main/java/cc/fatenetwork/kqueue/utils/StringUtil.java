package cc.fatenetwork.kqueue.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {

    public StringUtil() {
        throw new UnsupportedOperationException("Util class cannot be initialized");
    }

    public static String format(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> format(List<String> messages) {
        List<String> toReturn = new ArrayList<>();
        for (String message : messages) {
            message = ChatColor.translateAlternateColorCodes('&', message);

            toReturn.add(message);
        }
        return toReturn;
    }
}
