package com.btc.model;

import javax.jws.soap.InitParam;

import com.btc.Rect;
import com.btc.Vector2D;
import com.btc.config.Config;
import com.btc.helper.Vector2DHelper;
import com.btc.model.Character.CharacterState;

import javafx.scene.image.Image;

public class Crawler extends Enemy {

	@Override
	public void loadAnimations() {
		frameDictionary.put(CharacterState.WALKING, new AnimatedImage(new Image[] {
				new Image("file:sprites/Crawler2.png"),
				new Image("file:sprites/Crawler3.png"),
				new Image("file:sprites/Crawler4.png")			

		}, 0.1, true));
		frameDictionary.put(CharacterState.JUMP_UP, new AnimatedImage(new Image[] {
				new Image("file:sprites/Crawler1.png"),
				new Image("file:sprites/Crawler3.png"),
				new Image("file:sprites/Crawler1.png")			

		}, 0.1, false));
		frameDictionary.put(CharacterState.DYING, new AnimatedImage(new Image[] {
				new Image("file:sprites/Crawler5.png"),
				new Image("file:sprites/Crawler6.png"),
				new Image("file:sprites/Crawler7.png"),
				new Image("file:sprites/Crawler8.png"),
				new Image("file:sprites/Crawler9.png")

		}, 0.2, false));
		frameDictionary.put(CharacterState.FALLING, new AnimatedImage(new Image[] {
				new Image("file:sprites/Crawler1.png")		

		}, 1, false));
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
}