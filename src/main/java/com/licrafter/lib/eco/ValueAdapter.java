package com.licrafter.lib.eco;

import com.licrafter.lib.permissions.PermissionsInterface;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Created by shell on 2018/1/16.
 * <p>
 * Github: https://github.com/shellljx
 */
public class ValueAdapter implements EconomyInterface, PermissionsInterface {

    public static Permission permissions = null;
    public static Economy economy = null;
    public static Chat chat = null;

    public ValueAdapter(Server server) {
        setupPermissions(server);
        setupEconomy(server);
        setupChat(server);
    }

    private static boolean setupPermissions(Server s) {
        RegisteredServiceProvider<Permission> permissionProvider = s.getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permissions = permissionProvider.getProvider();
        }
        return (permissions != null);
    }

    private static boolean setupChat(Server s) {
        RegisteredServiceProvider<Chat> chatProvider = s.getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }
        return (chat != null);
    }

    private static boolean setupEconomy(Server s) {
        RegisteredServiceProvider<Economy> economyProvider = s.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

    public boolean permissionsOK() {
        if (permissions != null && !permissions.getName().equalsIgnoreCase("SuperPerms")) {
            return true;
        }
        return false;
    }

    public boolean economyOK() {
        return economy != null;
    }

    public boolean chatOK() {
        return chat != null;
    }


    @Override
    public boolean transfer(OfflinePlayer playerFrom, OfflinePlayer playerTo, double amount) {
        if (economy.withdrawPlayer(playerFrom, amount).transactionSuccess()) {
            if (economy.depositPlayer(playerTo, amount).transactionSuccess()) {
                return true;
            }
            economy.depositPlayer(playerFrom, amount);
            return false;
        }
        return false;
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return economy.getBalance(player);
    }

    @Override
    public boolean canAfford(OfflinePlayer player, double amount) {
        return economy.has(player, amount);
    }

    @Override
    public boolean add(OfflinePlayer player, double amount) {
        return economy.depositPlayer(player, amount).transactionSuccess();
    }

    @Override
    public boolean subtract(OfflinePlayer player, double amount) {
        return economy.withdrawPlayer(player, amount).transactionSuccess();
    }

    @Override
    public String getName() {
        return "Value";
    }

    @Override
    public String format(double amount) {
        return economy.format(amount);
    }

    @Override
    public String getPlayerGroup(Player player) {
        String group = permissions.getPrimaryGroup(player).toLowerCase();
        if (group == null) {
            return group;
        }
        return group.toLowerCase();
    }

    @Override
    public String getPlayerGroup(String player, String world) {
        String group = permissions.getPrimaryGroup(world, player);
        if (group == null) {
            return group;
        }
        return group.toLowerCase();
    }
}
