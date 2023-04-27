package com.Legoman1342.utilities;

import com.mojang.math.Vector3f;

import java.awt.*;

/**
 * Contains a method for inverting a <code>Color</code>, as well as methods for converting between three types of color storage:
 * @<code>Color</code>: Used for general color storage, stores RGB values as ints between 0 and 255 (but can be accessed as floats between 0 and 1)
 * @<code>Vector3f</code>: Used to create colored dust particles, stores RGB values as floats between 0 and 1
 * @<code>int[]</code>: Used to save colors to an NBT file, stores RGB values as ints between 0 and 255
 */
public class ColorUtils {
	/**
	 * Finds the inverse of a <code>Color</code> by subtracting its RGB values from 1.
	 */
	public static Color invertColor(Color input) {
		return new Color(1 - input.getRed(), 1 - input.getGreen(), 1 - input.getBlue());
	}

	/**
	 * Converts a <code>Vector3f</code> to a <code>Color</code>.
	 */
	public static Color toColor(Vector3f input) {
		return new Color(input.x(), input.y(), input.z());
	}

	/**
	 * Converts an <code>int[]</code> to a <code>Color</code>.
	 */
	public static Color toColor(int[] input) {
		return new Color(input[0], input[1], input[2]);
	}

	/**
	 * Converts a <code>Color</code> to a <code>Vector3f</code>.
	 */
	public static Vector3f toVector3f(Color input) {
		return new Vector3f((input.getRed() - 0.5F) / 255F, (input.getGreen() - 0.5F) / 255F, (input.getBlue() - 0.5F) / 255F);
	}

	/**
	 * Converts an <code>int[]</code> to a <code>Vector3f</code>.
	 */
	public static Vector3f toVector3f(int[] input) {
		return new Vector3f((input[0] - 0.5F) / 255F, (input[1] - 0.5F) / 255F, (input[2] - 0.5F) / 255F);
	}

	/**
	 * Converts a <code>Color</code> to an <code>int[]</code>.
	 */
	public static int[] toIntArray (Color input) {
		return new int[] {input.getRed(), input.getGreen(), input.getBlue()};
	}

	/**
	 * Converts a <code>Vector3f</code> to an <code>int[]</code>.
	 */
	public static int[] toIntArray (Vector3f input) {
		return new int[] {(int) (input.x() * 255 + 0.5), (int) (input.y() * 255 + 0.5), (int) (input.z() * 255 + 0.5)};
	}
}
