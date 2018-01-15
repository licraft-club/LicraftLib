package com.licrafter.lib.eco;

import org.bukkit.OfflinePlayer;

/**
 * Created by shell on 2018/1/16.
 * <p>
 * Github: https://github.com/shellljx
 */
public class BlackHoleEconomy implements EconomyInterface {
    @Override
    public double getBalance(OfflinePlayer player) {
        return 0;
    }

    @Override
    public boolean canAfford(OfflinePlayer player, double amount) {
        return false;
    }

    @Override
    public boolean add(OfflinePlayer player, double amount) {
        return false;
    }

    @Override
    public boolean subtract(OfflinePlayer player, double amount) {
        return false;
    }

    @Override
    public boolean transfer(OfflinePlayer playerFrom, OfflinePlayer playerTo, double amount) {
        return false;
    }

    @Override
    public String getName() {
        return "BlackHoleEconoy";
    }

    @Override
    public String format(double amount) {
        return String.valueOf(Math.round(amount * 100) / 100D);
    }
}
