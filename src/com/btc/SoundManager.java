package com.btc;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.media.AudioClip;


public class SoundManager {
	
	static AudioClip playBackGround;
	
	static Map<String, javafx.scene.media.AudioClip> soundAssets = new HashMap<String, javafx.scene.media.AudioClip>();
	
	private SoundManager() {
		
	}

	public static void playBackGroundMusic(String fileName) {
		if (playBackGround == null)
			playBackGround = new javafx.scene.media.AudioClip(new File(fileName).toURI().toString());	
			
		if (!playBackGround.isPlaying()) {
			playBackGround.setCycleCount(-1);
			playBackGround.play();
		}
	}
	
	public static void stopBackGroundMusic() {
		if (playBackGround == null) {
			return;
		}
		
		playBackGround.stop();
		playBackGround = null;
	}
	
	public static void playSound(String fileName) {
		javafx.scene.media.AudioClip clip2;
		if (!soundAssets.containsKey(fileName)){
			clip2 = new javafx.scene.media.AudioClip(new File(fileName).toURI().toString());
			soundAssets.put(fileName, clip2);
		} else {
			clip2 = soundAssets.get(fileName);
		}
		if (!clip2.isPlaying()) {
			clip2.play();
		}			
	}
	
}
