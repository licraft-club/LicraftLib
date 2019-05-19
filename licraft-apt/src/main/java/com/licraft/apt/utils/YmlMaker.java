package com.licraft.apt.utils;

import com.google.common.base.Charsets;
import com.licrafter.lib.log.BLog;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

/**
 * Created by shell on 2017/12/2.
 * <p>
 * Github: https://github.com/shellljx
 */
public class YmlMaker {

    private JavaPlugin plugin;
    private FileConfiguration configuration = null;
    private File configFile = null;
    private String fileName;

    public YmlMaker(JavaPlugin plugin, String fileName) {
        if (plugin == null) {
            throw new IllegalArgumentException("plugin cannot be null");
        }

        this.plugin = plugin;
        this.fileName = fileName;

        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists())
            dataFolder.mkdirs();
        configFile = new File(dataFolder, fileName);
    }

    public void reloadConfig() {
        configuration = YamlConfiguration.loadConfiguration(configFile);

        InputStream defInputStream = plugin.getResource(fileName);
        if (defInputStream == null) {
            return;
        }

        InputStreamReader streamReader = new InputStreamReader(defInputStream, Charsets.UTF_8);

        configuration.setDefaults(YamlConfiguration.loadConfiguration(streamReader));

        try {
            defInputStream.close();
        } catch (IOException e) {
            plugin.getLogger().log(Level.INFO, plugin.getName(), "Faild close YmlMaker inputStream!");
        }

        try {
            streamReader.close();
        } catch (IOException e) {
            plugin.getLogger().log(Level.INFO, plugin.getName(), "Faild close YmlMaker streamReader!");
        }
    }

    public FileConfiguration getConfig() {
        if (configuration == null) {
            this.reloadConfig();
        }
        return configuration;
    }

    /**
     * 如果不存在则创建文件
     *
     * @return 文件是否创建成功
     */
    public boolean createNewFile() {
        if (configFile != null && !configFile.exists()) {
            try {
                return configFile.createNewFile();
            } catch (IOException e) {
                BLog.warning(plugin, "create " + fileName + " failed!");
                return false;
            }
        }
        return true;
    }

    /**
     * config 写入文件
     */
    public void saveConfig() {
        if (configuration == null || configFile == null) {
            return;
        }
        try {
            getConfig().save(configFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.INFO, plugin.getName(), "Could not save config to " + configFile + ex);
        }
    }

    /**
     *
     */
    public void saveDefaultConfig() {
        if (configFile != null && !configFile.exists()) {
            plugin.saveResource(fileName, false);
        }
    }
}
