package com.btc.model;

import java.io.File;
import java.net.MalformedURLException;

import com.btc.Rect;
import com.btc.SoundManager;
import com.btc.Vector2D;
import com.btc.scene.TileMap;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

public class Enemy extends Character {

	BasicPlayer playDyingSound;
	public Player player;
	public TileMap map;
	
	public Enemy(String imageNamed) {
		super(imageNamed);
		life = 100;
		
	}
	
	@Override
	public Rect collisionBoundingBox() {
		return super.collisionBoundingBox();
	}
	
	@Override
	public void tookHit(Character character) {
		life -= 100;
		if (life <= 0) {
			changeState(CharacterState.DEAD);					
		}
	}
	
	public void remove() {
		this.isActive = false;
	}
	
	public Enemy() {
		// TODO Auto-generated constructor stub
	}
}
