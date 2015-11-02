package com.btc;


public class Vector2D {
	public double x;
	public double y;	
	
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public final static Vector2D zero = new Vector2D(0, 0);
}

