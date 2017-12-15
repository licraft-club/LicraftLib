package com.licrafter.lib;

import com.licrafter.lib.log.LicraftLog;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by shell on 2017/12/2.
 * <p>
 * Github: https://github.com/shellljx
 */
public class LicraftLib extends JavaPlugin {

    @Override
    public void onEnable() {
        LicraftLog.printEnableInfo(this);
    }

    @Override
    public void onDisable() {
        LicraftLog.printDisableInfo(this);
    }
}
