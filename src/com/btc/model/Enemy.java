package com.btc.model;

import com.btc.Rect;
import com.btc.scene.TileMap;

public class Enemy extends Character {

	public Player player;
	public TileMap map;
	
	public Enemy(String imageNamed) {
		super(imageNamed);
		life = 100;
	}
	
	@Override
	public Rect collisionBoundingBox() {
		return new Rect( desiredPosition.x - 13, desiredPosition.y - 20, 26, 40);
	}
	
	@Override
	public void tookHit(Character character) {
		life -= 100;
		if (life <= 0) {
			changeState(CharacterState.DEAD);
			//remove();
		}
	}
	
	public void remove() {
		this.isActive = false;
	}
}
