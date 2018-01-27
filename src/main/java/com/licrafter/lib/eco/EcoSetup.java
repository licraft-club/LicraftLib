package com.licrafter.lib.eco;

import com.licrafter.lib.log.LicraftLog;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by shell on 2018/1/15.
 * <p>
 * Github: https://github.com/shellljx
 */
public class EcoSetup {

    public static EconomyInterface onEnable(JavaPlugin plugin) {
        LicraftLog.consoleMessage(plugin.getName(), "Scanning for economy systems...");
        EconomyInterface economy = null;

        ValueAdapter vault = new ValueAdapter(plugin.getServer());
        if (vault.economyOK()) {
            economy = vault;
            LicraftLog.consoleMessage(plugin.getName(), " Found Vault using economy system: " + vault.getName());
        }
        if (economy == null) {
            economy = new BlackHoleEconomy();
            LicraftLog.consoleMessage(plugin.getName(), " Unable to find an economy system...");
        }
        return economy;
    }
}
