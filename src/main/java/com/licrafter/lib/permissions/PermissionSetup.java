package com.licrafter.lib.permissions;

import com.licrafter.lib.eco.ValueAdapter;
import com.licrafter.lib.log.BLog;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by shell on 2018/2/15.
 * <p>
 * Github: https://github.com/shellljx
 */
public class PermissionSetup {

    public static PermissionsInterface onEnable(JavaPlugin plugin) {
        BLog.consoleMessage(plugin.getName(), "Scanning for permission systems...");
        PermissionsInterface permission = null;

        ValueAdapter vault = new ValueAdapter(plugin.getServer());
        if (vault.economyOK()) {
            permission = vault;
            BLog.consoleMessage(plugin.getName(), " Found Vault using permission system: " + vault.getName());
        }
        return permission;
    }
}
