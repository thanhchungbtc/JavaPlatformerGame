package com.btc.config;


import com.btc.Vector2D;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Config {
	
	public final static double NANOSECONDPERSEC = 1000000000.0;
	
	public final static Vector2D Gravity = new Vector2D(0, 450);
	
	public static class WindowProperties {
		public final static int WINDOW_WIDTH = 900;
		public final static int WINDOW_HEIGHT = 576;
	}	
	
	public static class PlayerProperties {
		public final static double JumpForce = 350;
		public final static double MaxMoveSpeed = 650;
		public final static double WalkingAccelerate = 2500;
		
		public final static int Width = 38;
		public final static int Height = 30;
	}
	
	public static class CrawlerProperties {
		public final static double MaxMoveSpeed = 100;
	}
	
	
}
