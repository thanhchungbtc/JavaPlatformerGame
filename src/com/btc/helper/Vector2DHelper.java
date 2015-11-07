package com.btc.helper;

import com.btc.Vector2D;

public class Vector2DHelper {
	public static Vector2D AddVector(Vector2D v1, Vector2D v2) {
		return new Vector2D(v1.x + v2.x, v1.y + v2.y);
	}
	
	public static Vector2D MutilByScalar(Vector2D vector, double scalar) {
		return new Vector2D(vector.x * scalar, vector.y * scalar);
	}
	
	public static Vector2D SubstractVector(Vector2D v1, Vector2D v2) {
		return new Vector2D(v1.x - v2.x, v1.y - v2.y);
	}
	
	public static Vector2D clamped(Vector2D vector, double maxX, double maxY) {
		return new Vector2D(Utilities.clamp(vector.x, -maxX, maxX), Utilities.clamp(vector.y, -maxY, maxY));		
	}
	
	public static double DistanceBetweeen(Vector2D vector1, Vector2D vector2) {
		double a = vector2.x - vector1.x;
		double b = vector2.y - vector1.y;
		return Math.sqrt(a * a + b * b);
	}
	
	public static Vector2D normalized(Vector2D vector) {
		double length = vector.length();
		return new Vector2D(vector.x / length, vector.y / length);
	}
}
