package com.btc.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import com.btc.Rect;
import com.btc.SoundManager;
import com.btc.Vector2D;
import com.btc.config.Config;
import com.btc.helper.Utilities;
import com.btc.helper.Vector2DHelper;
import com.btc.model.Character.CharacterState;
import com.btc.scene.GameScene;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.File;

public class Player extends Character {

	// sound effect
	String playJumpSound;
	String playDyingSound;
	String playBounceSound;

	public boolean shouldJump = false;
	public boolean shouldMoveLeft = false;
	public boolean shouldMoveRight = false;
	private boolean isInvulnerable = false;
	private boolean jumpReset = true;
	private boolean canDoubleJump = true;
	public Sprite lifeBarImage;
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
		frameDictionary.put(CharacterState.STANDING, this.loadAnimations("standingAnim", true));
		frameDictionary.put(CharacterState.WALKING, this.loadAnimations("walkingAnim", true));
		frameDictionary.put(CharacterState.JUMP_UP, this.loadAnimations("jumpUpAnim", false));
		frameDictionary.put(CharacterState.DOUBLEJUMP, this.loadAnimations("jumpUpAnim", false));
		frameDictionary.put(CharacterState.FALLING, this.loadAnimations("fallingAnim", false));	
		frameDictionary.put(CharacterState.DEAD, this.loadAnimations("dyingAnim", false));	
		this.changeState(CharacterState.STANDING);	
	}
	
	@Override
	public void setOnGround(boolean onGround) {
		
		super.setOnGround(onGround);
		if (onGround)this.canDoubleJump = true;
	}

	@Override
	public void changeState(CharacterState newState) {
		if (newState == characterState) return;

		switch (newState) {
		case JUMP_UP:
		case DOUBLEJUMP:
			SoundManager.playSound(playJumpSound); 
			break;
		case DEAD:
			SoundManager.playSound(playDyingSound);
			break;

		default:
			break;
		}
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
			if (canDoubleJump && (this.characterState == CharacterState.FALLING || characterState == CharacterState.JUMP_UP) 
					&& this.jumpReset) {
				canDoubleJump = false;
				newState = CharacterState.DOUBLEJUMP;
				this.velocity = new Vector2D(this.velocity.x, -Config.PlayerProperties.JumpForce);
			} else if (this.onGround && this.jumpReset) {
				this.velocity = new Vector2D(this.velocity.x, -Config.PlayerProperties.JumpForce);
				newState = CharacterState.JUMP_UP;
				this.onGround = false;		      
			}
			this.jumpReset = false;

		} else {
			this.jumpReset = true;
			if (this.velocity.y < -Config.PlayerProperties.JumpCutOff) {
				this.velocity = new Vector2D(this.velocity.x, -Config.PlayerProperties.JumpCutOff);
			}
		}

		if (this.onGround && !this.shouldMoveLeft && !this.shouldMoveRight) {			
			newState = CharacterState.STANDING;
			
		} else if (this.onGround && (this.shouldMoveLeft || this.shouldMoveRight)) {
			newState = CharacterState.WALKING;
		}		
		else if (newState == CharacterState.DOUBLEJUMP || characterState == CharacterState.DOUBLEJUMP) {
			newState = CharacterState.DOUBLEJUMP;
		}
		else if (this.characterState == CharacterState.JUMP_UP || newState == CharacterState.JUMP_UP) {
			newState = CharacterState.JUMP_UP;		
		}
		else {
			newState = CharacterState.FALLING;
		}
		if (this.velocity.y > 0) 
			newState = CharacterState.FALLING;
		this.changeState(newState);
	}

	public Player(String imageNamed) {
		super(imageNamed);	
		life = 500;
		playJumpSound = "sounds/jump1.mp3";
		playDyingSound = "sounds/player_die.wav";			
		playBounceSound = "sounds/bounce.wav";
	}

	public Player() {
		life = 500;
	}

	@Override
	public void tookHit(Character character) {
		setLife(life - 50);
		if (life <= 0) {
			changeState(CharacterState.DEAD);

		} else {
			isActive = false;
			isInvulnerable = true;
			if (this.position.x < character.position.x) {
				this.velocity = new Vector2D(-80, -80);
			} else {
				this.velocity = new Vector2D(80, -80);
			}
		}

	}

	public void setLife(int newLife) {
		if (newLife == life) {
			return;
		}
		life = newLife;
		int lifeIdx = (newLife + 50) / 100;

		lifeIdx =(int) Utilities.clamp((double)lifeIdx, 0, 5);
		// set lifebar
		try {
			lifeBarImage.setTexture(new Image(new FileInputStream("sprites/Life_Bar_" + lifeIdx + "_5.png")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	int invulnerableTickCount = 0;
	int engameTickCount = 0;
	@Override
	public void update(double dt) {
		if (characterState == CharacterState.DEAD) {
			// update animation only
			super.update(dt);	
			if (animation.animationCompleted(this.timeElapsedSinceStartAnimation)){
				engameTickCount++;
				if (engameTickCount > 30) {
					// end the game
					endGame();
				}
			} 
			return;
		}

		// logic comes here
		if (isInvulnerable) {
			invulnerableTickCount++;
			if (invulnerableTickCount > 100) {
				invulnerableTickCount = 0;
				isActive = true;
				isInvulnerable = false;
			}
		}

		updateState(dt);

		Vector2D gravity = Config.Gravity;
		Vector2D gravityStep = Vector2DHelper.MutilByScalar(gravity, dt);		
		this.velocity = Vector2DHelper.AddVector(this.velocity, gravityStep);		

		this.velocity = Vector2DHelper.clamped(this.velocity, Config.PlayerProperties.MaxMoveSpeed, 450.0);		
		this.velocity = new Vector2D(this.velocity.x * 0.85, this.velocity.y);
		Vector2D velocityStep = Vector2DHelper.MutilByScalar(this.velocity, dt);				
		this.desiredPosition = Vector2DHelper.AddVector(this.position, velocityStep);

		// setup current frame for class	
		super.update(dt);
	}

	@Override
	public void render(GraphicsContext gc) {
		if (isInvulnerable) {
			gc.setGlobalAlpha(0.5);
			super.render(gc);
			gc.setGlobalAlpha(1.0);
		} else {
			super.render(gc);
		}
	}

	public void endGame() {
		GameScene gameScene = (GameScene)scene;
		gameScene.looseGame();		
	}

	public void bounce() {
		this.setOnGround(true);
		this.velocity = new Vector2D(this.velocity.x, -Config.PlayerProperties.JumpForce / 3);		
		SoundManager.playSound(playBounceSound);
	}

}
