package com.btc.model;

import javafx.scene.image.Image;

public class AnimatedImage {
	public Image[] frames;
	public double duration;
	public boolean reapeat = false;
			
	public AnimatedImage(Image[] frames, double duration, boolean reapeat) {
		this.frames = frames;
		this.duration = duration;
		this.reapeat = reapeat;
	}
	
	public AnimatedImage(Image[] frames, double duration) {
		this.frames = frames;
		this.duration = duration;
		this.reapeat = true;
	}
	
	public Image getFrame(double elapsedTime) {
		if (frames.length == 1) return frames[0];
		if (elapsedTime > frames.length * duration && !reapeat)
			return frames[frames.length - 1];
		
		double elapsed = (elapsedTime % (frames.length * duration));	
		int index = (int)( elapsed / duration);		
        return frames[index];
	}
	
	public boolean animationCompleted(double elapsedTime) {
		return elapsedTime > frames.length * duration;
	}
}
