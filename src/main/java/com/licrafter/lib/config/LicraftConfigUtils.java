package com.licrafter.lib.config;

import com.licrafter.lib.log.BLog;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by shell on 2017/12/3.
 * <p>
 * Github: https://github.com/shellljx
 */
public class LicraftConfigUtils {

    /**
     * write default config file of plugin from jar
     *
     * @param plugin
     */
    public static void writeDefaultConfigFromJar(JavaPlugin plugin, String configPath) {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.isDirectory()) {
            dataFolder.mkdirs();
        }
        if (!new File(dataFolder, configPath).isFile()) {
            if (writeDefaultFileFromJar(plugin, new File(plugin.getDataFolder(), configPath), configPath, true)) {
                BLog.consoleMessage(plugin.getName(), "Wrote default config...");
            }
        }
    }

    /**
     * write default file from jar
     * same as plugin saveResource
     *
     * @param plugin
     * @param writeName
     * @param jarPath
     * @param backupOld
     * @return
     */
    public static boolean writeDefaultFileFromJar(JavaPlugin plugin, File writeName, String jarPath, boolean backupOld) {
        try {
            File fileBackup = new File(plugin.getDataFolder(), "backup-" + writeName);
            File jarloc = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getCanonicalFile();
            if (jarloc.isFile()) {
                JarFile jar = new JarFile(jarloc);
                JarEntry entry = jar.getJarEntry(jarPath);
                if (entry != null && !entry.isDirectory()) {
                    InputStream in = jar.getInputStream(entry);
                    InputStreamReader isr = new InputStreamReader(in, "UTF8");
                    if (writeName.isFile()) {
                        if (backupOld) {
                            if (fileBackup.isFile()) {
                                fileBackup.delete();
                            }
                            writeName.renameTo(fileBackup);
                        } else {
                            writeName.delete();
                        }
                    }
                    FileOutputStream out = new FileOutputStream(writeName);
                    OutputStreamWriter osw = new OutputStreamWriter(out, "UTF8");
                    char[] tempbytes = new char[512];
                    int readbytes = isr.read(tempbytes, 0, 512);
                    while (readbytes > -1) {
                        osw.write(tempbytes, 0, readbytes);
                        readbytes = isr.read(tempbytes, 0, 512);
                    }
                    osw.close();
                    isr.close();
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            BLog.consoleMessage(plugin.getName(), "Failed to write file: " + writeName);
            return false;
        }
    }
}
