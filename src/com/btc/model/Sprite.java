package com.btc.model;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.PrimitiveIterator.OfDouble;

import com.btc.*;
import com.btc.config.Config;
import com.btc.scene.TileMap;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprite {
	
	public Vector2D position;
	public Size size;
	protected boolean flipX;
	
	protected Scene scene;
	protected Image image;

	public TileMap map;
	
	
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	
	public void setTexture(Image newImage) {
		this.image = newImage;
		this.size = new Size(newImage.getWidth(), newImage.getHeight());
	}
	
	public Sprite(String imageNamed) {
		
		this.image = new Image(imageNamed);
		this.position = Vector2D.zero;
		this.size = new Size(image.getWidth(), image.getHeight());
	}
	public Sprite() { }
	public void update(double dt) {
		
	}
	
	public void render(GraphicsContext gc) {
		if (flipX){
			gc.drawImage(this.image, 
					position.x + size.width - size.width / 2, position.y - size.height / 2, 
					-size.width, size.height);
		}
		else
			gc.drawImage(this.image, 
					position.x - size.width / 2, position.y  - size.height / 2,
					size.width, size.height);
		
	}
}
