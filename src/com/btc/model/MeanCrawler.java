package com.btc.model;

import com.btc.Vector2D;
import com.btc.config.Config;
import com.btc.helper.Utilities;
import com.btc.helper.Vector2DHelper;

public class MeanCrawler extends Enemy {

	public MeanCrawler(String imageNamed) {
		super(imageNamed);
		// TODO Auto-generated constructor stub
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
	
	
}
