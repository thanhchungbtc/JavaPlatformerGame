package com.btc.model;

import java.util.HashMap;
import java.util.Map;

import com.btc.Rect;
import com.btc.Vector2D;
import com.btc.config.Config;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class GameObject extends Sprite {
	
	public Rect collisionBoundingBox() {
		return new Rect(position.x - this.size.width / 2, position.y - this.size.height / 2, this.size.width, this.size.height);
	}
	
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


