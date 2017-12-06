package com.licraft.apt.description;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface Description {

	/**
	 * @return Description of the plugin
	 */
	String value();

}
