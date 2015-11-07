package com.btc.model;


import com.btc.Rect;
import com.btc.Vector2D;
import com.btc.config.Config;
import com.btc.helper.Vector2DHelper;
import com.btc.model.Character.CharacterState;

import javafx.scene.image.Image;

public class Crawler extends Enemy {

	@Override
	public void loadAnimations() {
		frameDictionary.put(CharacterState.WALKING, this.loadAnimations("walkingAnim", true));
		frameDictionary.put(CharacterState.JUMP_UP, this.loadAnimations("jumpUpAnim", false));
		frameDictionary.put(CharacterState.DEAD, this.loadAnimations("dyingAnim", false));
		frameDictionary.put(CharacterState.FALLING, this.loadAnimations("fallingAnim", false));
		
		this.changeState(CharacterState.WALKING);
	}

	@Override
	public void changeState(CharacterState newState) {

		super.changeState(newState);
	}

	@Override
	protected void updateState(double dt) {
		if (this.characterState == CharacterState.DEAD) {
			this.desiredPosition = this.position;
			return;
		}

		double distance = Vector2DHelper.DistanceBetweeen(this.player.position, this.position); 

		if (distance > 1000) {
			this.desiredPosition = this.position;
//			 this.isActive = false;
			return;
		} else {
//			 this.isActive = true
		}

		if (this.onGround) {
			this.changeState(CharacterState.WALKING);
			if (this.flipX) {
				this.velocity = new Vector2D(-Config.CrawlerProperties.MaxMoveSpeed, 0);
			} else {
				this.velocity = new Vector2D(Config.CrawlerProperties.MaxMoveSpeed, 0);

			}
		} else {
			this.changeState(CharacterState.FALLING);
			this.velocity = new Vector2D(this.velocity.x * 0.98, this.velocity.y);	    		  
		}
		
		if (this.onWall) {
			this.velocity = new Vector2D(-this.velocity.x, this.velocity.y);
			
		}
	}

	@Override
	public Rect collisionBoundingBox() {
		// TODO Auto-generated method stub
		return super.collisionBoundingBox();
	}
	
	@Override
	public void update(double dt) {
		if (characterState == CharacterState.DEAD) {
			// update animation only
			this.desiredPosition = this.position;
			super.update(dt);	
			// when animation completed
			
			if (animation.animationCompleted(this.timeElapsedSinceStartAnimation)){
				remove();			
			} 
			
			return;
		}
		Double distance = Vector2DHelper.DistanceBetweeen(this.position, player.position);
		if (distance > 1000) {
			this.desiredPosition = this.position;
			this.isActive = false;
			return; 
		} else 
			this.isActive = true;
		
		// logic comes here
		updateState(dt);

		Vector2D gravity = Config.Gravity;
		Vector2D gravityStep = Vector2DHelper.MutilByScalar(gravity, dt);		
		this.velocity = Vector2DHelper.AddVector(this.velocity, gravityStep);		
	
		this.velocity = Vector2DHelper.clamped(this.velocity, Config.CrawlerProperties.MaxMoveSpeed, 450.0);		
		this.velocity = new Vector2D(this.velocity.x * 0.85, this.velocity.y);
		Vector2D velocityStep = Vector2DHelper.MutilByScalar(this.velocity, dt);				
		this.desiredPosition = Vector2DHelper.AddVector(this.position, velocityStep);

		super.update(dt);
	}

	public Crawler(String imageNamed) {
		super(imageNamed);
		// TODO Auto-generated constructor stub
	}	
	public Crawler() {
		// TODO Auto-generated constructor stub
	}
}