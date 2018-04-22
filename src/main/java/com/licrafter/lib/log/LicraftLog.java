package com.licrafter.lib.log;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * Created by shell on 2017/12/3.
 * <p>
 * Github: https://github.com/shellljx
 */
public class LicraftLog {


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

    public static void consoleMessage(String message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"[LicraftLib]" + " " + message));
    }

    public static String getLogPrefix(String sender) {
        return ChatColor.GREEN + "[" + ChatColor.GOLD + sender + ChatColor.GREEN + "]" + ChatColor.GRAY;
    }
}
