package com.licraft.apt.config;

import com.licrafter.lib.log.LicraftLog;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import com.licraft.apt.AnnotationsAbstract;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class ConfigAnnotations extends AnnotationsAbstract {

    /**
     * Load the plugin's configuration values into the class fields
     *
     * @param plugin        {@link Plugin} to load the configuration form
     * @param classesToLoad Array of classes to set the fields in
     */
    public ConfigAnnotations loadValues(Plugin plugin, Object... classesToLoad) {
        if (plugin == null) {
            throw new IllegalArgumentException("plugin cannot be null");
        }
        if (classesToLoad.length == 0) {
            throw new IllegalArgumentException("classes cannot be empty");
        }
        for (Object toLoad : classesToLoad) {
            loadValues(plugin, toLoad);
        }
        return this;
    }

    /**
     * Load the plugin's configuration values into the class fields
     *
     * @param plugin
     * @param classToLoad
     * @return
     */
    public ConfigAnnotations loadValues(Plugin plugin, Object classToLoad) {
        if (plugin == null) {
            throw new IllegalArgumentException("plugin cannot be null");
        }
        if (classToLoad == null) {
            throw new IllegalArgumentException("class cannot be null");
        }
        for (Field field : classToLoad.getClass().getDeclaredFields()) {
            ConfigValue configValue = field.getAnnotation(ConfigValue.class);
            ConfigSection configSection = field.getAnnotation(ConfigSection.class);
            if (configValue != null) {
                decodeValueFromYml(configValue, field, null, plugin, classToLoad);
            } else if (configSection != null) {
                decodeSectionFromYml(configSection, field, plugin, classToLoad);
            }
        }
        return this;
    }

    /**
     * Decode configSection
     *
     * @param configSection
     * @param field
     * @param plugin
     * @param classToLoad
     */
    private void decodeSectionFromYml(ConfigSection configSection, Field field, Plugin plugin, Object classToLoad) {
        Class<?> clazz = classToLoad.getClass();
        FileConfiguration config = plugin.getConfig();

        if (field.getType() == List.class) {
            Type genericType = field.getGenericType();
            if (genericType != null && genericType instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) genericType;
                Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
                try {
                    List listObject = new ArrayList<>();
                    ConfigurationSection configurationSection = config.getConfigurationSection(configSection.path());
                    Set<String> listKeySet = configurationSection.getKeys(false);
                    for (String key : listKeySet) {
                        Object tempObject = genericClazz.newInstance();
                        for (Field childField : genericClazz.getDeclaredFields()) {
                            ConfigValue childConfigValue = childField.getAnnotation(ConfigValue.class);
                            if (childConfigValue != null) {
                                field.setAccessible(true);
                                if (config.contains(configSection.path())) {
                                    decodeValueFromYml(childConfigValue, childField, configSection.path() + "." + key, plugin, tempObject);
                                }
                            }
                        }
                        listObject.add(tempObject);
                    }
                    field.set(classToLoad, listObject);
                } catch (Exception e) {
                    throw new RuntimeException("Fiald get field new Instance of '" + genericClazz.getName() + "' in " + clazz + "\n" + e);
                }
            } else {
                throw new RuntimeException("The list genericType is invalid '" + field.getName() + "' in " + clazz);
            }
        }
    }

    /**
     * Decode field value from configSection
     *
     * @param configValue
     * @param field
     * @param plugin
     * @param classToLoad
     */
    private void decodeValueFromYml(ConfigValue configValue, Field field, String parentPath, Plugin plugin, Object classToLoad) {
        Class<?> clazz = classToLoad.getClass();
        FileConfiguration config = plugin.getConfig();
        String path = parentPath == null ? configValue.path() : parentPath + "." + configValue.path();
        LicraftLog.consoleMessage(path);

        try {
            field.setAccessible(true);
            if (config.contains(path)) {
                Object value = config.get(path);
                if (configValue.colorChar() != ' ') {
                    if (value instanceof String) {
                        value = ChatColor.translateAlternateColorCodes(configValue.colorChar(), (String) value);
                    }
                    if (value instanceof List) {
                        for (ListIterator iterator = ((List) value).listIterator(); iterator.hasNext(); ) {
                            Object next = iterator.next();
                            if (next instanceof String) {
                                iterator.set(ChatColor.translateAlternateColorCodes(configValue.colorChar(), (String) next));
                            }
                        }
                    }
                }
                field.set(classToLoad, value);
            } else if (!configValue.defaultsTo().isEmpty()) {
                //TODO: Parse value type
                field.set(classToLoad, configValue.defaultsTo());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to set config value for field '" + field.getName() + "' in " + clazz, e);
        }
    }

    @Override
    public void load(Plugin plugin, Object clazz) {
        loadValues(plugin, clazz);
    }
}
