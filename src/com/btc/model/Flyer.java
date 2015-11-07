package com.btc.model;

import java.io.File;
import java.net.MalformedURLException;

import com.btc.SoundManager;
import com.btc.Vector2D;
import com.btc.helper.Vector2DHelper;
import com.btc.model.Character.CharacterState;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

public class Flyer extends Enemy {

	BasicPlayer playAttackSound;
	BasicPlayer playCloseEyeSound;
	
	@Override
	public void changeState(CharacterState newState) {
		if (newState == characterState) return;
		switch (newState) {
		case ATTACKING:
			SoundManager.playSound(playAttackSound);
			break;
		case HIDING:
			SoundManager.playSound(playCloseEyeSound);
			break;
		case DEAD:
			SoundManager.playSound(playDyingSound);
			break;
	
		default:
			break;
		}
		super.changeState(newState);
	}
	
	
	public Flyer(String imageNamed) {
		super(imageNamed);
		try {			
			playDyingSound = new BasicPlayer();
			playDyingSound.open(new File("sounds/crawler_die.wav").toURL());	
			playAttackSound = new BasicPlayer();
			playAttackSound.open(new File("sounds/flyerattack.wav").toURL());		
			playCloseEyeSound = new BasicPlayer();
			playCloseEyeSound.open(new File("sounds/flyercloseeye.wav").toURL());
		} catch (BasicPlayerException | MalformedURLException e) {
			
		}
	}
	
	@Override
	public void loadAnimations() {
		frameDictionary.put(CharacterState.SEEKING, this.loadAnimations("seekingAnim", true));
		frameDictionary.put(CharacterState.ATTACKING, this.loadAnimations("attackingAnim", true));
		frameDictionary.put(CharacterState.HIDING, this.loadAnimations("hidingAnim", true));
		frameDictionary.put(CharacterState.DEAD, this.loadAnimations("dyingAnim", false));
		this.changeState(CharacterState.SEEKING);
	}
	
	@Override
	public void update(double dt) {
		if (characterState == CharacterState.DEAD) {
			// update animation only\
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
		
		double speed = 0;
		if (distance < 100) {
			changeState(CharacterState.ATTACKING);
			speed = 100;
		} else
		{
			int playerDirection = player.flipX ? -1 : 1;
			// if player toward flyer
			if (playerDirection * (this.position.x - player.position.x) > 0) {
				changeState(CharacterState.HIDING);
				speed = 0;
			} else {
				changeState(CharacterState.SEEKING);
				speed = 60;
			}
		}
		
		Vector2D v = Vector2DHelper.SubstractVector(player.position, this.position);
		v = Vector2DHelper.normalized(v);
		v = Vector2DHelper.MutilByScalar(v, speed);
		this.velocity = v;
		Vector2D stepVelocity = Vector2DHelper.MutilByScalar(velocity, dt);
		this.desiredPosition = Vector2DHelper.AddVector(this.position, stepVelocity);
		
		super.update(dt);
	}

	public Flyer() {
		// TODO Auto-generated constructor stub
	}
}
