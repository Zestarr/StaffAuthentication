package com.spacedev.auth.util;

import com.spacedev.auth.Auth;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtils {

    public static String format(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String getPluginMessage(Auth plugin, String s) {
        return format(plugin.getConfig().getString("AuthMessagePrefix") + s);
    }

}
