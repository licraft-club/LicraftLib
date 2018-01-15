package com.licrafter.lib.eco;

import org.bukkit.OfflinePlayer;

/**
 * Created by shell on 2018/1/15.
 * <p>
 * Github: https://github.com/shellljx
 */
public interface EconomyInterface {

    /**
     * 获取玩家游戏币数量
     *
     * @param player
     * @return
     */
    public double getBalance(OfflinePlayer player);

    /**
     * 判断玩家能否负担起该数目的扣费
     *
     * @param player
     * @param amount
     * @return
     */
    public boolean canAfford(OfflinePlayer player, double amount);

    /**
     * 添加指定数目的游戏币
     *
     * @param player
     * @param amount
     * @return
     */
    public boolean add(OfflinePlayer player, double amount);

    /**
     * 扣除指定数目的游戏币
     *
     * @param player
     * @param amount
     * @return
     */
    public boolean subtract(OfflinePlayer player, double amount);

    /**
     * 从一个玩家转移到另一个玩家
     *
     * @param playerFrom
     * @param playerTo
     * @param amount
     * @return
     */
    public boolean transfer(OfflinePlayer playerFrom, OfflinePlayer playerTo, double amount);

    public String getName();

    public String format(double amount);
}
