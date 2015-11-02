package com.btc.helper;

public class Utilities {
	public static double clamp(double input, double min, double max) {
		if (input < min) input = min;
		if (input > max) input = max;
		return input;
	}
}
