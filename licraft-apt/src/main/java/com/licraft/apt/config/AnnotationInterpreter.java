package com.licraft.apt.config;

import com.licrafter.lib.config.DataConfigFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

/**
 * Created by shell on 2017/12/16.
 * <p>
 * Github: https://github.com/shellljx
 */
public class AnnotationInterpreter {

    private DataConfigFile dataConfigFile;

    public void onInterpreter(JavaPlugin plugin, Object target, Result decodeResult) {
        if (plugin == null) {
            throw new IllegalArgumentException("plugin cannot be null");
        }
        if (target == null) {
            throw new IllegalArgumentException("class cannot be null");
        }

        ConfigBean configBean = target.getClass().getAnnotation(ConfigBean.class);
        String configPath = null;
        if (configBean == null) {
            for (Field field : target.getClass().getDeclaredFields()) {
                configBean = field.getAnnotation(ConfigBean.class);
                if (configBean != null) {
                    try {
                        configPath = (String) field.get(target);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Get config path failed! " + e);
                    }
                }
            }
        } else {
            configPath = configBean.file();
        }

        if (configPath != null) {
            dataConfigFile = new DataConfigFile(plugin, configPath);
            for (Field field : target.getClass().getDeclaredFields()) {
                ConfigValue configValue = field.getAnnotation(ConfigValue.class);
                ConfigSection configSection = field.getAnnotation(ConfigSection.class);
                if (configValue != null) {
                    decodeResult.onInterpreter(configValue, field, null, dataConfigFile, target);
                } else if (configSection != null) {
                    decodeResult.onInterpreter(configSection, field, dataConfigFile, target);
                }
            }
        }
    }

    public DataConfigFile getConfigFile() {
        return dataConfigFile;
    }

    /**
     * decode result callback
     */
    public static interface Result {

        void onInterpreter(ConfigValue configValue, Field field, String parentPath, DataConfigFile configFile, Object target);

        void onInterpreter(ConfigSection configSection, Field field, DataConfigFile configFile, Object target);
    }
}
