package com.btc.model;

import java.io.File;
import java.net.MalformedURLException;

import com.btc.SoundManager;
import com.btc.Vector2D;
import com.btc.config.Config;
import com.btc.helper.Utilities;
import com.btc.helper.Vector2DHelper;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

public class MeanCrawler extends Enemy {

	BasicPlayer playJumpSound;
	public MeanCrawler(String imageNamed) {
		super(imageNamed);
		try {			
			playDyingSound = new BasicPlayer();
			playDyingSound.open(new File("sounds/crawler_die.wav").toURL());		
			playJumpSound = new BasicPlayer();
			playJumpSound.open(new File("sounds/crawler_jump.wav").toURL());
		} catch (BasicPlayerException | MalformedURLException e) {
			
		}
	}
	
	@Override
	public void changeState(CharacterState newState) {
		if (newState == characterState) return;
		switch (newState) {
		case JUMP_UP:
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
	public void loadAnimations() {
		frameDictionary.put(CharacterState.WALKING, this.loadAnimations("walkingAnim", true));
		frameDictionary.put(CharacterState.JUMP_UP, this.loadAnimations("jumpUpAnim", false));
		frameDictionary.put(CharacterState.DEAD, this.loadAnimations("dyingAnim", false));
		this.changeState(CharacterState.WALKING);
	}

	@Override
	public void update(double dt) {		
		if (characterState == CharacterState.DEAD) {
			// update animation only
			this.desiredPosition = this.position;
			super.update(dt);	
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
		} else {
			this.isActive = true;
		}
		
		if (this.onGround) this.changeState(CharacterState.WALKING);
		
		Vector2D positionInMap = Vector2DHelper.SubstractVector(this.position, map.position);
		Vector2D tileCoord = map.coordForPoint(positionInMap);
		int playerDirection = this.player.position.x > this.position.x ? 1 : -1;
		Vector2D twoTilesAhead = new Vector2D(tileCoord.x + playerDirection * 2, tileCoord.y);
		
		twoTilesAhead = new Vector2D(
				Utilities.clamp(twoTilesAhead.x, 0, map.mapWidth),
				Utilities.clamp(twoTilesAhead.y, 0, map.mapHeight));
		this.velocity = new Vector2D(playerDirection * Config.CrawlerProperties.MaxMoveSpeed, this.velocity.y);
		if (this.map.hasWallAtTileCoord(twoTilesAhead) || 
				distance < 100 && (this.position.y - player.position.y > 50)) {
			if (this.onGround) {
				this.velocity = new Vector2D(this.velocity.x, -Config.CrawlerProperties.JumpForce);
				this.changeState(CharacterState.JUMP_UP);
			}
		}
		
		Vector2D gravity = Config.Gravity;
		Vector2D gravityStep = Vector2DHelper.MutilByScalar(gravity, dt);
		
		this.velocity = Vector2DHelper.AddVector(this.velocity, gravityStep);		
	
		this.velocity = Vector2DHelper.clamped(this.velocity, Config.CrawlerProperties.MaxMoveSpeed, 450.0);		
		this.velocity = new Vector2D(this.velocity.x * 0.85, this.velocity.y);
		Vector2D velocityStep = Vector2DHelper.MutilByScalar(this.velocity, dt);				
		this.desiredPosition = Vector2DHelper.AddVector(this.position, velocityStep);

		
		
		super.update(dt);
	}
	
	public MeanCrawler() {
		// TODO Auto-generated constructor stub
	}
}
