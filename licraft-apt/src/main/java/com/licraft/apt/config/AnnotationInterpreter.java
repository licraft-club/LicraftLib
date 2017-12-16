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

    private static AnnotationInterpreter instance;
    private DataConfigFile dataConfigFile;

    public synchronized static AnnotationInterpreter getInstance() {
        if (instance == null) {
            instance = new AnnotationInterpreter();
        }
        return instance;
    }

    public void onInterpreter(JavaPlugin plugin, Object target, Result decodeResult) {
        if (plugin == null) {
            throw new IllegalArgumentException("plugin cannot be null");
        }
        if (target == null) {
            throw new IllegalArgumentException("class cannot be null");
        }

        ConfigBean configBean = target.getClass().getAnnotation(ConfigBean.class);

        if (configBean != null) {
            String configFile = configBean.file();
            dataConfigFile = new DataConfigFile(plugin, configFile);

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

    public DataConfigFile getConfigFile(){
        return dataConfigFile;
    }

    public void releaseConfigFile(){
        dataConfigFile = null;
    }

    /**
     * decode result callback
     */
    public static interface Result {

        void onInterpreter(ConfigValue configValue, Field field, String parentPath, DataConfigFile configFile, Object target);

        void onInterpreter(ConfigSection configSection, Field field, DataConfigFile configFile, Object target);
    }
}
