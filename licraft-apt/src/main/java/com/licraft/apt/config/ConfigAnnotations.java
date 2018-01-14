package com.licraft.apt.config;

import com.licraft.apt.utils.APTUtils;
import com.licrafter.lib.config.DataConfigFile;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import com.licraft.apt.AnnotationsAbstract;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class ConfigAnnotations extends AnnotationsAbstract {

    private AnnotationInterpreter interpreter;

    public ConfigAnnotations() {
        interpreter = AnnotationInterpreter.getInstance();
    }

    /**
     * Load the plugin's configuration values into the class fields
     *
     * @param plugin        {@link Plugin} to load the configuration form
     * @param classesToLoad Array of classes to set the fields in
     */
    public ConfigAnnotations loadValues(JavaPlugin plugin, Object... classesToLoad) {
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
    public ConfigAnnotations loadValues(JavaPlugin plugin, Object classToLoad) {
        interpreter.onInterpreter(plugin, classToLoad, new AnnotationInterpreter.Result() {
            @Override
            public void onInterpreter(ConfigValue configValue, Field field, String parentPath, DataConfigFile configFile, Object target) {
                decodeValueFromYml(configValue, field, parentPath, configFile, target);
            }

            @Override
            public void onInterpreter(ConfigSection configSection, Field field, DataConfigFile configFile, Object target) {
                decodeSectionFromYml(configSection, field, configFile, target);
            }
        });
        interpreter.releaseConfigFile();
        return this;
    }

    /**
     * Decode configSection
     *
     * @param configSection
     * @param field
     * @param configFile
     * @param classToLoad
     */
    private void decodeSectionFromYml(ConfigSection configSection, Field field, DataConfigFile configFile, Object classToLoad) {
        Class<?> clazz = classToLoad.getClass();
        FileConfiguration config = configFile.getConfig();

        if (field.getType() == List.class) {
            Type genericType = field.getGenericType();
            if (genericType != null && genericType instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) genericType;
                Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
                field.setAccessible(true);
                try {
                    List sectionValue = new ArrayList<>();
                    ConfigurationSection section = config.getConfigurationSection(configSection.path());
                    Set<String> sectionKeySet = section.getKeys(false);

                    for (String key : sectionKeySet) {
                        Object genericObject = genericClazz.newInstance();
                        for (Field genericField : genericClazz.getDeclaredFields()) {
                            ConfigValue genericConfigValue = genericField.getAnnotation(ConfigValue.class);
                            if (genericConfigValue != null && config.contains(configSection.path())) {
                                decodeValueFromYml(genericConfigValue, genericField, configSection.path() + "." + key, configFile, genericObject);
                            }
                        }
                        sectionValue.add(genericObject);
                    }
                    field.set(classToLoad, sectionValue);
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
     * @param configFile
     * @param classToLoad
     */
    private void decodeValueFromYml(ConfigValue configValue, Field field, String parentPath, DataConfigFile configFile, Object classToLoad) {
        Class<?> clazz = classToLoad.getClass();
        boolean parentNode = configValue.valueKey();
        String childPath = configValue.path();
        String path = parentPath == null ? configValue.path() : parentPath
                + (childPath.equals("") ? "" : "." + configValue.path());
        FileConfiguration config = configFile.getConfig();

        try {
            field.setAccessible(true);
            if (!parentNode && config.contains(path)) {
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
                field.set(classToLoad, configValue.defaultsTo());
            } else if (parentNode) {
                field.set(classToLoad, parentPath);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to set config value for field '" + field.getName() + "' in " + clazz, e);
        }
    }

    @Override
    public void load(JavaPlugin plugin, Object clazz) {
        loadValues(plugin, clazz);
    }

    /**
     * save memory data to file
     *
     * @param plugin
     * @param classesToSave
     */
    public void saveValues(JavaPlugin plugin, Object... classesToSave) {
        if (plugin == null) {
            throw new IllegalArgumentException("plugin cannot be null");
        }
        if (classesToSave.length == 0) {
            throw new IllegalArgumentException("classes cannot be empty");
        }
        for (Object toSave : classesToSave) {
            saveValue(plugin, toSave);
        }
    }

    /**
     * save memory data to file
     *
     * @param plugin
     * @param classToSave
     */
    public void saveValue(JavaPlugin plugin, Object classToSave) {
        interpreter.onInterpreter(plugin, classToSave, new AnnotationInterpreter.Result() {
            @Override
            public void onInterpreter(ConfigValue configValue, Field field, String parentPath, DataConfigFile configFile, Object target) {
                encodeValueToYml(configValue, field, parentPath, configFile, target);
            }

            @Override
            public void onInterpreter(ConfigSection configSection, Field field, DataConfigFile configFile, Object target) {
                encodeSectionToYml(configSection, field, configFile, target);
            }
        });
        interpreter.getConfigFile().saveConfig();
        interpreter.releaseConfigFile();
    }

    public void encodeSectionToYml(ConfigSection configSection, Field field, DataConfigFile configFile, Object target) {
        field.setAccessible(true);
        String path = configSection.path();

        try {
            if (field.getType() == List.class) {
                List sectionValue = (List) field.get(target);
                for (Object value : sectionValue) {
                    for (Field childFile : value.getClass().getDeclaredFields()) {
                        ConfigValue configValue = childFile.getAnnotation(ConfigValue.class);
                        if (configValue.valueKey()) {
                            path = (String) childFile.get(value);
                            break;
                        }
                    }

                    for (Field childFile : value.getClass().getDeclaredFields()) {
                        ConfigValue configValue = childFile.getAnnotation(ConfigValue.class);
                        if (configValue != null && !configValue.valueKey()) {
                            Object fieldValue = childFile.get(value);
                            encodeValueToYml(configValue, childFile, path, configFile, fieldValue);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("The section value is invalid '" + field.getName() + "' in " + target.getClass() + e);
        }
    }

    public void encodeValueToYml(ConfigValue configValue, Field field, String parentPath, DataConfigFile configFile, Object target) {
        String path = parentPath == null ? configValue.path() : parentPath + (configValue.path().equals("") ? "" : ("." + configValue.path()));
        field.setAccessible(true);

        if (configValue.colorChar() != ' ') {
            if (target instanceof String) {
                target = APTUtils.encodeAlternateColorCodes(configValue.colorChar(), (String) target);
            }
            if (target instanceof List) {
                for (ListIterator iterator = ((List) target).listIterator(); iterator.hasNext(); ) {
                    Object next = iterator.next();
                    if (next instanceof String) {
                        iterator.set(APTUtils.encodeAlternateColorCodes(configValue.colorChar(), (String) next));
                    }
                }
            }
        }
        configFile.getConfig().set(path, target);
    }
}
