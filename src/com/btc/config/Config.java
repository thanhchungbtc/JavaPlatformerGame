package com.btc.config;


import com.btc.Vector2D;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Config {
	
	public final static double NANOSECONDPERSEC = 1000000000.0;
	
	public final static Vector2D Gravity = new Vector2D(0, 650);
	
	public static class WindowProperties {
		public final static int WINDOW_WIDTH = 900;
		public final static int WINDOW_HEIGHT = 576;
	}	
	
	public static class PlayerProperties {
		public final static double JumpForce = 400;
		public final static double JumpCutOff = 200;
		public final static double MaxMoveSpeed = 800;
		public final static double WalkingAccelerate = 2500;
		
		public final static int Width = 30;
		public final static int Height = 38;
	}
	
	public static class CrawlerProperties {
		public final static double MaxMoveSpeed = 100;
		public final static double JumpForce = 250;
	}
	
	
}
