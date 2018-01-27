package com.licrafter.lib.permissions;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface PermissionsInterface {
    public String getPlayerGroup(Player player);

    public String getPlayerGroup(OfflinePlayer player, String world);

    public void playerAdd(String world, OfflinePlayer player, String permission);
}
