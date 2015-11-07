package com.btc.scene;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

import com.btc.Rect;
import com.btc.Vector2D;
import com.btc.config.Config;
import com.btc.helper.CollisionsHelper;
import com.btc.helper.Utilities;
import com.btc.helper.Vector2DHelper;
import com.btc.model.Character;
import com.btc.model.Player;
import com.btc.model.PowerUp;
import com.btc.model.Sprite;
import com.btc.model.Character.CharacterState;
import com.btc.model.Crawler;
import com.btc.model.Enemy;
import com.btc.model.Flyer;
import com.btc.model.MeanCrawler;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class GameScene extends Scene {
	public Group root;
	public Canvas canvas;

	long lastUpdateTime = 0;
	TileMap map;
	Player player;
	Crawler crawler;
	MeanCrawler meanCrawler;
	Flyer flyer;

	List<Sprite> sprites;
	Image backgroundImage;
	Sprite lifeBarImage;

	List<Enemy> enemies;
	List<PowerUp> powerUps;
	Vector2D exitPoint;
	int currentLevel = 1;
	public double gameHeight() {
		return canvas.getHeight();
	}

	private double gameWidth() {
		return canvas.getWidth();
	}
	private void setupGameLoop() {
		this.root = (Group) super.getRoot();
		this.canvas = new Canvas(Config.WindowProperties.WINDOW_WIDTH, Config.WindowProperties.WINDOW_HEIGHT);
		root.getChildren().add(canvas);

		ArrayList<String> input = new ArrayList<String>();
		this.setOnKeyPressed(
				new EventHandler<KeyEvent>()
				{
					public void handle(KeyEvent e)
					{
						String code = e.getCode().toString();

						// only add once... prevent duplicates
						if ( !input.contains(code) )
							input.add( code );
					}
				});

		this.setOnKeyReleased(
				new EventHandler<KeyEvent>()
				{
					public void handle(KeyEvent e)
					{
						String code = e.getCode().toString();
						input.remove( code );
					}
				});

		new AnimationTimer() {

			@Override
			public void handle(long currentTime) {		
				handleEvents(input);
				update(currentTime);				
				render(canvas.getGraphicsContext2D());
			}
		}.start();
	}

	private void addSprite(Sprite sprite) {
		sprite.setScene(this);
		sprite.map = this.map;
		sprites.add(sprite);
	}

	private void setupPlayer() {
//		player = new Player("/sprites/Player1.png");
//		map.addChild(player);
//		player.isActive = true;
//		player.position = new Vector2D(100, 300);
		player = map.player;
		player.lifeBarImage = this.lifeBarImage;
	}

	private void loadEnemies() {
//		enemies = new LinkedList<Enemy>();
//		crawler = new Crawler("sprites/Crawler1.png");
//		map.addChild(crawler);
//		crawler.player = player;
//		crawler.position = new Vector2D(800, 400);
//		enemies.add(crawler);
//
//		meanCrawler = new MeanCrawler("sprites/MeanCrawler1.png");
//		meanCrawler.player = player;
//		meanCrawler.map = map;
//		map.addChild(meanCrawler);
//		meanCrawler.position = new Vector2D(500, 400);
//		enemies.add(meanCrawler);
//
//		flyer = new Flyer("sprites/Flyer1.png");
//		flyer.player = player;
//		flyer.map = map;
//		map.addChild(flyer);
//		flyer.position = new Vector2D(500, 400);
//		enemies.add(flyer);
		this.enemies = map.enemies;
		
	}

	private void checkForExit() {
		Vector2D playerPositionInMap = Vector2DHelper.SubstractVector(player.position, map.position); 
		double distance = Vector2DHelper.DistanceBetweeen(playerPositionInMap, exitPoint);
		if (distance < 100) { // level clear
			currentLevel++;
			newLevel();
		}
	}
	
	private void newLevel() {
		backgroundImage = new Image("images/city1-2.png");
		map = new TileMap(this, currentLevel);
		double gameHeight = this.gameHeight();
		double diffHeight = map.mapHeightInPixel() - gameHeight;
		map.position = new Vector2D(0, -diffHeight);
		lifeBarImage = new Sprite("sprites/Life_Bar_5_5.png");
		lifeBarImage.position = new Vector2D(100, 50);
		//sprites = new ArrayList<Sprite>();

		setupPlayer();
		loadEnemies();
		powerUps = map.powerUps;
		exitPoint = map.exitPoint;
		System.out.println(exitPoint.x);
		
	}
	
	private void checkForEnemyCollisions(Enemy enemy) {
		if (enemy.isActive && player.isActive && enemy.getState() != CharacterState.DEAD) {
			if(CollisionsHelper.RectIntersectsRect(this.player.collisionBoundingBox(), enemy.collisionBoundingBox())) {
				Vector2D playerFootPoint = new Vector2D(player.position.x, player.position.y + player.collisionBoundingBox().height / 2);
				if (this.player.velocity.y > 0 && playerFootPoint.y < enemy.position.y) {
					enemy.tookHit(player);
					player.bounce();
				} else {
					player.tookHit(enemy);
				}
			}
		}
	}

	private void checkAndResolveCollision(Character character) {

		character.setOnGround(false);
		character.setOnWall(false);

		int[] indices = {7, 1, 3, 5, 0, 2, 6, 8 };
		for (int i = 0; i < indices.length; i++) 
		{
			int tileIndex = indices[i];
			// calculate player's position relative to map
			Vector2D characterPosition = Vector2DHelper.SubstractVector(character.desiredPosition, map.position);

			Vector2D characterCoord = map.coordForPoint(characterPosition);


			int tileColumn = tileIndex % 3;
			int tileRow = tileIndex / 3;
			Vector2D tileCoord = new Vector2D((int)characterCoord.x + tileColumn - 1, (int)characterCoord.y + tileRow - 1);

			int gid = map.tileGIDAtTileCoord(tileCoord);
			if (gid == 0) continue;
			Rect characterRect = character.collisionBoundingBox();
			characterRect = new Rect(characterRect.x - map.position.x, characterRect.y - map.position.y,
					characterRect.width, characterRect.height);
			Rect tileRect = map.rectFromCoord(tileCoord);

			if (CollisionsHelper.RectIntersectsRect(characterRect, tileRect)) {
				Rect intersection = CollisionsHelper.RectIntersection(characterRect, tileRect);
				if (tileIndex == 7) {
					character.desiredPosition = new Vector2D(character.desiredPosition.x, character.desiredPosition.y - intersection.height);

					character.setOnGround(true);

				} else if (tileIndex == 1) {
					character.desiredPosition = new Vector2D(character.desiredPosition.x, character.desiredPosition.y + intersection.height);
					character.velocity = new Vector2D(character.velocity.x, 0);

				} else if (tileIndex == 3) {
					character.desiredPosition = new Vector2D(character.desiredPosition.x + intersection.width, character.desiredPosition.y);
					character.setOnWall(true);
				} else if (tileIndex == 5) {
					character.desiredPosition = new Vector2D(character.desiredPosition.x - intersection.width, character.desiredPosition.y);
					character.setOnWall(true);

				} else 
				{
					if (intersection.width > intersection.height)
					{
						double resolutionHeight = 0;
						if (tileIndex > 4){
							resolutionHeight = -intersection.height;
							if (character.velocity.y > 0) {
								character.setOnGround(true);
							}							
						} else {
							resolutionHeight =  intersection.height;
							if (character.velocity.y < 0) {
								character.velocity = new Vector2D(character.velocity.x, 0);
							}
						}
						character.desiredPosition = new Vector2D(character.desiredPosition.x, character.desiredPosition.y + resolutionHeight);
					} else {
						//tile is diagonal, but resolving horizontally
						double resolutionWidth = 0;
						if (tileIndex == 6 || tileIndex == 0) {
							resolutionWidth = intersection.width;
						} else {
							resolutionWidth = -intersection.width;
						}
						character.desiredPosition = new Vector2D(character.desiredPosition.x + resolutionWidth, character.desiredPosition.y);
						if (tileIndex == 6 || tileIndex == 8) {
							character.setOnWall(true);
						}
						character.velocity = new Vector2D(0.0, character.velocity.y);
					}

				}

			}

		}
		character.position = character.desiredPosition; 
		//		if (player.desiredPosition.y + player.size.height / 2 > gameHeight()) {
		//			player.desiredPosition =new Vector2D(player.desiredPosition.x, gameHeight() - player.size.height / 2);
		//			player.setOnGround(true);
		//		}

	}

	private void checkForPowerUp() {
		List<PowerUp> powerUpsToRemove = null;
		for (PowerUp powerUp: this.powerUps) {
			if (CollisionsHelper.RectIntersectsRect(powerUp.collisionBoundingBox(), player.collisionBoundingBox())) {
				if (powerUpsToRemove == null) powerUpsToRemove = new LinkedList<PowerUp>();
				
				int playerLife = player.life + 50;
				playerLife = (int) Utilities.clamp((double)playerLife, 0, 500);
				player.setLife(playerLife);
				powerUpsToRemove.add(powerUp);
			}
		}
		if (powerUpsToRemove != null) {
			for(PowerUp powerUp: powerUpsToRemove) {
				this.powerUps.remove(powerUp);
				this.map.removeChild(powerUp);
				System.out.println(powerUps.size());
			}
		}
	}	
	
	public GameScene() {
		super(new Group());
		setupGameLoop();		
		newLevel();
	}

	public void handleEvents(List<String> input) {
		if (input.contains("LEFT"))
			this.player.shouldMoveLeft = true;
		else if (input.contains("RIGHT"))
			this.player.shouldMoveRight = true;
		else {
			this.player.shouldMoveLeft = false;
			this.player.shouldMoveRight = false;
		}

		if (input.contains("SPACE"))
			this.player.shouldJump = true;
		else {
			this.player.shouldJump = false;
		}
	}			

	private void moveMapCenterPlayer(double dt) {
		Vector2D locationInMap = Vector2DHelper.SubstractVector(player.position, map.position);
		if (locationInMap.x > gameWidth() / 2 && locationInMap.x < map.mapWidthInPixel() - gameWidth() / 2) {

			Vector2D centerOfView = new Vector2D(gameWidth() / 2, gameHeight() / 2);
			double distanceToCenter = player.position.x - centerOfView.x;
			map.setPosition(new Vector2D(map.position.x - distanceToCenter, map.position.y));
			//player.position = new Vector2D(player.position.x - distanceToCenter, player.position.y);
		} 
		if (locationInMap.y > gameHeight() / 2 && locationInMap.y < map.mapHeightInPixel() - gameHeight() / 2) {

			Vector2D centerOfView = new Vector2D(gameWidth() / 2, gameHeight() / 2);
			double distanceToCenter = player.position.y - centerOfView.y;
			map.setPosition(new Vector2D(map.position.x, map.position.y - distanceToCenter));
			//player.position = new Vector2D(player.position.x, player.position.y - distanceToCenter);
		} 

	}

	public void update(long currentTime) {
		checkForExit();
		
		double dt = (currentTime - lastUpdateTime) / Config.NANOSECONDPERSEC;
		if (dt > 0.03) dt = 0.03;
		lastUpdateTime = currentTime;

		player.update(dt);		
		this.checkAndResolveCollision(player);

		List<Enemy> enemiesToDelete = null;

		for (Enemy enemy: enemies) {

			enemy.update(dt);
			if (enemy.isActive) {
				this.checkAndResolveCollision(enemy);
				this.checkForEnemyCollisions(enemy);
			}
			
			if (!enemy.isActive 
					&& enemy.getState() == CharacterState.DEAD) {
				if (enemiesToDelete == null) {
					enemiesToDelete = new ArrayList<Enemy>();
					
				}
				enemiesToDelete.add(enemy);
			}
		}		
		if (enemiesToDelete != null) {
			System.out.println(enemiesToDelete.size());
			for (Enemy enemy: enemiesToDelete) {
				enemies.remove(enemy);
				map.removeChild(enemy);
				enemy.remove();
			}
			enemiesToDelete.clear();
		}
		this.checkForPowerUp();

		moveMapCenterPlayer(dt);
		// for debug purpose
		if (debugInterval >= 30) {
			debugInterval = 0;
			this.fps = (int)(1 / dt);
		}
		debugInterval++;
	}

	int debugInterval = 0;
	public void render(GraphicsContext gc) {
		// clear canvas
		gc.clearRect(0, 0, Config.WindowProperties.WINDOW_WIDTH, Config.WindowProperties.WINDOW_HEIGHT);
		gc.drawImage(backgroundImage, 0, 0);
		map.render(gc);
		for (PowerUp powerUp: powerUps) {
			powerUp.render(gc);
		}
		player.render(gc);
		for (Enemy enemy: enemies) 
			enemy.render(gc);
		lifeBarImage.render(gc);
		
		// for debug purpose	
		gc.setStroke(Color.AQUA);
		gc.strokeText("FPS: " + String.valueOf(this.fps), this.getWidth() - 100, this.getHeight() - 30);
	}
	// for debug
	int fps;
}
