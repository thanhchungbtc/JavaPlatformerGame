package com.btc.model;

import java.util.HashMap;
import java.util.Map;

import com.btc.Rect;
import com.btc.Vector2D;
import com.btc.model.Character.CharacterState;
import com.btc.scene.TileMap;

import javafx.scene.image.Image;
import xmlwise.Plist;

public abstract class Character extends GameObject {
	public enum CharacterState {
		STANDING, WALKING, JUMP_UP, FALLING, DYING, DEAD
	}
	protected Map<CharacterState, AnimatedImage> frameDictionary;	
	protected double timeElapsedSinceStartAnimation; // second	
	protected AnimatedImage animation;
	
	protected CharacterState characterState;
		
	protected boolean onGround = false;
	protected boolean onWall = false;

	public Vector2D desiredPosition;
	public Vector2D velocity = Vector2D.zero;
	
	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
		if (this.onGround) 
			this.velocity = new Vector2D(this.velocity.x, 0);
	}
	
	public void setOnWall(boolean onWall) {
		this.onWall = onWall;
		if (this.onWall)
			this.velocity = new Vector2D(0, this.velocity.y);
	}
	
	public boolean getOnWall() { return this.onWall; }
	
	public Rect collisionBoundingBox() {
		return new Rect(desiredPosition.x - this.size.width / 2, desiredPosition.y - this.size.height / 2, this.size.width, this.size.height);
	}
	
	public void loadAnimations() {}
	
	public void changeState(CharacterState newState) {
		if (newState == this.characterState) return;
		this.characterState = newState;
		this.timeElapsedSinceStartAnimation = 0;
		animation = frameDictionary.get(this.characterState);	
	}

	public Character(String imageNamed) {
		super(imageNamed);
		frameDictionary = new HashMap<CharacterState, AnimatedImage>();
		this.timeElapsedSinceStartAnimation = 0;
		this.loadAnimations();
	}
	
	protected void updateState(double dt) {
		
	}
	
	@Override
	public void update(double dt) {
		// setup current frame for animation purpose
		this.flipX = this.velocity.x < 0;
		this.timeElapsedSinceStartAnimation += dt;		
		double elapsedTime = (double)(this.timeElapsedSinceStartAnimation);
		animation = frameDictionary.get(this.characterState);
		this.setTexture(animation.getFrame(elapsedTime));
	}
	protected AnimatedImage loadAnimations(String className, String animationName, boolean repeat) {
		try {
			Map<String, Object> root = Plist.load("data/" + className + ".plist");
			Map<String, Object> properties = (Map<String, Object>)root.get(animationName);
			String[] imageNames = properties.get("animationFrames").toString().split(",");
			double duration = Double.valueOf(properties.get("delay").toString());
			
			Image[] images = new Image[imageNames.length];
			for (int i = 0; i < images.length; i++) {
				images[i] = new Image("sprites/" + className + imageNames[i] + ".png");
			}
			AnimatedImage result = new AnimatedImage(images, duration, repeat);
			return result;
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}
	

}
