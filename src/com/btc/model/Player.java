package com.btc.model;

import java.util.HashMap;

import com.btc.Rect;
import com.btc.Vector2D;
import com.btc.config.Config;
import com.btc.helper.Utilities;
import com.btc.helper.Vector2DHelper;
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
		return new Rect(bounding.x, bounding.y + 3, bounding.width, bounding.height);
	}
	
	@Override
	public void loadAnimations() {
		frameDictionary = new HashMap<CharacterState, AnimatedImage>();
		frameDictionary.put(CharacterState.STANDING, new AnimatedImage(new Image[] {
				new Image("sprites/Player1.png")				
		}, 1));
		frameDictionary.put(CharacterState.WALKING, new AnimatedImage(new Image[] {
				new Image("sprites/Player2.png"),
				new Image("sprites/Player3.png"),
				new Image("sprites/Player4.png"),
				new Image("sprites/Player5.png"),
				new Image("sprites/Player6.png"),
				new Image("sprites/Player7.png"),
				new Image("sprites/Player8.png"),
				new Image("sprites/Player9.png")				
		}, 0.1));
		
		frameDictionary.put(CharacterState.JUMP_UP, new AnimatedImage(new Image[] {
				new Image("sprites/Player1.png"),
				new Image("sprites/Player10.png"),
				new Image("sprites/Player11.png"),
				new Image("sprites/Player12.png")				
		}, 0.1, false));
		
		frameDictionary.put(CharacterState.FALLING, new AnimatedImage(new Image[] {
				new Image("sprites/Player10.png")				
		}, 1));
		 this.changeState(CharacterState.STANDING);
		
	}
	
	@Override
	public void changeState(CharacterState newState) {
		if (newState == this.characterState) return;
		this.characterState = newState;
		animation = frameDictionary.get(this.characterState);		
	}
	
	@Override
	public CharacterState getCurrentState() {
		if (!this.onGround) characterState = CharacterState.FALLING;
		else if (velocity.y < 0) characterState = CharacterState.JUMP_UP;
		else if (velocity.x == 0 ) {
			characterState = CharacterState.STANDING;
		} else {
			characterState = CharacterState.WALKING;
		}
		return characterState;
	}
	/*
	private void updateState(double dt) {
		// get current State
		
		CharacterState newState = getCurrentState();
		
		switch (characterState) {
		case STANDING:
			Vector2D moveForce = Vector2D.zero;
			if (shouldMoveLeft) moveForce = new Vector2D(-Config.PlayerProperties.WalkingAccelerate, moveForce.y);
			else if (shouldMoveRight) moveForce = new Vector2D(Config.PlayerProperties.WalkingAccelerate, moveForce.y);
						
			Vector2D moveForceStep = Vector2DHelper.MutilByScalar(moveForce, dt);
			if (shouldJump) {
				moveForceStep = new Vector2D(moveForceStep.x, -Config.PlayerProperties.JumpForce);				
			}
			this.velocity = Vector2DHelper.AddVector(this.velocity, moveForceStep);
			
			if (moveForce.y != 0)
				newState = CharacterState.JUMP_UP;
			else if (moveForce.x != 0)
				newState = CharacterState.WALKING;			
			break;
		case WALKING:
			moveForce = Vector2D.zero;
			if (shouldMoveLeft) moveForce = new Vector2D(-Config.PlayerProperties.WalkingAccelerate, moveForce.y);
			else if (shouldMoveRight) moveForce = new Vector2D(Config.PlayerProperties.WalkingAccelerate, moveForce.y);
			moveForceStep = Vector2DHelper.MutilByScalar(moveForce, dt);
			if (shouldJump) {
				moveForceStep = new Vector2D(moveForceStep.x, -Config.PlayerProperties.JumpForce);
			}
			
			this.velocity = Vector2DHelper.AddVector(this.velocity, moveForceStep);
			
			if (moveForce.y != 0)
				newState = CharacterState.JUMP_UP;
			else if (!shouldMoveLeft && !shouldMoveRight)
				newState = CharacterState.STANDING;
			break;
		case JUMP_UP:
			moveForce = Vector2D.zero;
			if (shouldMoveLeft) moveForce = new Vector2D(-Config.PlayerProperties.WalkingAccelerate, moveForce.y);
			else if (shouldMoveRight) moveForce = new Vector2D(Config.PlayerProperties.WalkingAccelerate, moveForce.y);
			moveForceStep = Vector2DHelper.MutilByScalar(moveForce, dt);
			
			
			this.velocity = Vector2DHelper.AddVector(this.velocity, moveForceStep);
			
			if (this.velocity.y > 0) {
				newState = CharacterState.FALLING;
			}
			break;
		case FALLING:
			moveForce = Vector2D.zero;
			if (shouldMoveLeft) moveForce = new Vector2D(-Config.PlayerProperties.WalkingAccelerate, moveForce.y);
			else if (shouldMoveRight) moveForce = new Vector2D(Config.PlayerProperties.WalkingAccelerate, moveForce.y);
			
			moveForceStep = Vector2DHelper.MutilByScalar(moveForce, dt);
			this.velocity = Vector2DHelper.AddVector(this.velocity, moveForceStep);
			
			if (this.onGround) {
				newState = CharacterState.STANDING;
			}
			break;

		default:
			break;
		}
		
		
		this.changeState(newState);
	}
	*/
	private void updateState(double dt) {
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
