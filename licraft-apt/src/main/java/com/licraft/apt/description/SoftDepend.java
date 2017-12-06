package com.licraft.apt.description;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface SoftDepend {

	/**
	 * @return The plugin's soft-dependencies
	 */
	String[] value();

}
