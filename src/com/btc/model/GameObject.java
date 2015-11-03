package com.btc.model;

import java.util.HashMap;
import java.util.Map;

import com.btc.Vector2D;
import com.btc.config.Config;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class GameObject extends Sprite {
	
	public GameObject(String imageNamed) {
		super(imageNamed);
		
	}
	
	public GameObject() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void update(double dt) {
				
	}
	
}


