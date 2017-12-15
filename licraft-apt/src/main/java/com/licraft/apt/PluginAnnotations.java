package com.licraft.apt;

import com.licraft.apt.command.CommandAnnotations;
import com.licraft.apt.message.MessageAnnotations;
import com.licraft.apt.config.ConfigAnnotations;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginAnnotations {

	public static final ConfigAnnotations CONFIG  = new ConfigAnnotations();
	public static final MessageAnnotations MESSAGE = new MessageAnnotations();
	public static final CommandAnnotations COMMAND = new CommandAnnotations();

	public static final AnnotationsAbstract[] ALL_ANNOTATIONS = {
			CONFIG,
			MESSAGE,
			COMMAND };

	public static void loadAll(JavaPlugin plugin, Object classToLoad) {
		for (AnnotationsAbstract annotation : ALL_ANNOTATIONS) {
			annotation.load(plugin, classToLoad);
		}
	}

}
