package com.licrafter.lib.log;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;


/**
 * Created by shell on 2017/12/3.
 * <p>
 * Github: https://github.com/shellljx
 */
public class BLog {

    public static void printEnableInfo(JavaPlugin plugin) {
        String enableInfo =
                "Enabling " + plugin.getName()
                        + " (Version:" + plugin.getDescription().getVersion()
                        + " Authors:[" + String.join(",", plugin.getDescription().getAuthors())
                        + "])"
                        + " WebSite:" + plugin.getDescription().getWebsite();
        consoleMessage(plugin.getName(), enableInfo);
    }

    public static void printDisableInfo(JavaPlugin plugin) {
        String disableInfo = "The plugin " + plugin.getName() + " is Disabled,Thanks for your using!";
        consoleMessage(plugin.getName(), disableInfo);
    }

    public static void consoleMessage(String sender, String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', getLogPrefix(sender) + " " + message));
    }

    public static void consoleMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "[LicraftLib]" + " " + message));
    }

    public static void info(JavaPlugin plugin, String info) {
        plugin.getLogger().log(Level.INFO, info);
    }

    public static void warning(JavaPlugin plugin, String warning) {
        plugin.getLogger().log(Level.WARNING, warning);
    }

    public static String getLogPrefix(String sender) {
        return ChatColor.GREEN + "[" + ChatColor.GOLD + sender + ChatColor.GREEN + "]" + ChatColor.GRAY;
    }
}
