package com.Legoman1342.utilities;

import java.util.Locale;

public class Lang {
	/**
	 * Makes a given string lowercase, formatting it as an ID
	 * @param name Name with any capitalization
	 * @return An all-lowercase name formatted as an ID
	 */
	public static String asId(String name) {
		return name.toLowerCase(Locale.ROOT);
	}
}
