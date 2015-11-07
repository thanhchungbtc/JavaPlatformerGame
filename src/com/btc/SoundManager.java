package com.btc;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

public class SoundManager {
	public static void playSound(BasicPlayer basicPlayer) {
		try {
			basicPlayer.play();
		} catch (BasicPlayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
