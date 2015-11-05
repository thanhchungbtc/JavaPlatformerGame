package com.btc.model;

import java.util.HashMap;

import com.btc.Rect;
import com.btc.Vector2D;
import com.btc.config.Config;
import com.btc.helper.Utilities;
import com.btc.helper.Vector2DHelper;
import com.btc.model.Character.CharacterState;
import com.sun.javafx.fxml.BeanAdapter;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.LineTo;

public class Player extends Character {
	
	public boolean shouldJump = false;
	public boolean shouldMoveLeft = false;
	public boolean shouldMoveRight = false;
	
	@Override
	public void setTexture(Image newImage) {
		// TODO Auto-generated method stub
		super.setTexture(newImage);
	}
	
	@Override	
	public Rect collisionBoundingBox() {
		Rect bounding = new Rect(this.desiredPosition.x - Config.PlayerProperties.Width / 2, 
				this.desiredPosition.y - Config.PlayerProperties.Height / 2,
				Config.PlayerProperties.Width, 
				Config.PlayerProperties.Height);
		return new Rect(bounding.x, bounding.y, bounding.width, bounding.height);
	}
	
	@Override
	public void loadAnimations() {
		frameDictionary.put(CharacterState.STANDING, this.loadAnimations("Player", "standingAnim", true));
		frameDictionary.put(CharacterState.WALKING, this.loadAnimations("Player", "walkingAnim", true));
		frameDictionary.put(CharacterState.JUMP_UP, this.loadAnimations("Player", "jumpUpAnim", false));
		frameDictionary.put(CharacterState.FALLING, this.loadAnimations("Player", "fallingAnim", false));		
		this.changeState(CharacterState.STANDING);	
	}
	
	@Override
	public void changeState(CharacterState newState) {
		super.changeState(newState);
	}
	
	@Override	
	protected void updateState(double dt) {
		CharacterState newState = this.characterState;

		  Vector2D joyForce = Vector2D.zero;
		  if (this.shouldMoveLeft) {		    
		    joyForce = new Vector2D(-Config.PlayerProperties.WalkingAccelerate, 0);
		  } else if (this.shouldMoveRight) {		    
		    joyForce = new Vector2D(Config.PlayerProperties.WalkingAccelerate, 0);
		  }

		  Vector2D joyForceStep = Vector2DHelper.MutilByScalar(joyForce, dt);
		  this.velocity = Vector2DHelper.AddVector(this.velocity, joyForceStep);

		  if (this.shouldJump)
		  {
		    if (this.onGround) {
		      this.velocity = new Vector2D(this.velocity.x, -Config.PlayerProperties.JumpForce);
		      newState = CharacterState.JUMP_UP;
		      this.onGround = false;
		    } 
		  } 
		    
 	     if (this.onGround) {
			 if (!this.shouldMoveLeft && !this.shouldMoveRight)
			    newState = CharacterState.STANDING;
			 else  newState = CharacterState.WALKING;
		} else if (this.characterState == CharacterState.JUMP_UP || newState == CharacterState.JUMP_UP) {
			    newState = CharacterState.JUMP_UP;
		} else {
		 newState = CharacterState.FALLING;
		}
	    if (this.velocity.y > 0) 
			  newState = CharacterState.FALLING;
		 this.changeState(newState);
	}
	
	public Player(String imageNamed) {
		super(imageNamed);		
	}
	
	@Override
	public void update(double dt) {
		// logic comes here
		updateState(dt);
		
		Vector2D gravity = Config.Gravity;
		Vector2D gravityStep = Vector2DHelper.MutilByScalar(gravity, dt);		
		this.velocity = Vector2DHelper.AddVector(this.velocity, gravityStep);		
		// clamp velocity
//		 velocity = new Vector2D(Utilities.clamp(velocity.x, -Config.PlayerProperties.MaxMoveSpeed, Config.PlayerProperties.MaxMoveSpeed),
//				Utilities.clamp(velocity.y, 0, 450));
		this.velocity = Vector2DHelper.clamped(this.velocity, Config.PlayerProperties.MaxMoveSpeed, 450.0);		
		this.velocity = new Vector2D(this.velocity.x * 0.85, this.velocity.y);
		Vector2D velocityStep = Vector2DHelper.MutilByScalar(this.velocity, dt);				
		this.desiredPosition = Vector2DHelper.AddVector(this.position, velocityStep);
		
		// setup current frame for class	
		super.update(dt);
	}
	
}
