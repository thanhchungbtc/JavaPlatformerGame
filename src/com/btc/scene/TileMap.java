package com.btc.scene;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.btc.Rect;
import com.btc.Vector2D;
import com.btc.config.Config;
import com.btc.helper.Vector2DHelper;
import com.btc.model.GameObject;
import com.btc.model.MeanCrawler;
import com.btc.model.Player;
import com.btc.model.PowerUp;
import com.btc.model.Sprite;
import com.btc.model.Character.CharacterState;
import com.btc.model.Crawler;
import com.btc.model.Enemy;
import com.btc.model.Flyer;
import com.btc.model.Character;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import xmlwise.XmlElement;
import xmlwise.Xmlwise;

public class TileMap extends GameObject {

	static final String[] levelBackgrounds = {"city", "cave", "hell" };
	static final int[][] numPerLayers = {
			{6, 6, 4, 2},
			{6, 5, 4, 2},
			{6, 6, 4, 0}
	};
	public int mapWidth;
	public int mapHeight;

	public int tileWidth;
	public int tileHeight;	

	public Image tileset;
	public int[] data;

	List<Sprite> children = new ArrayList<Sprite>();
	public List<Enemy> enemies;
	public List<PowerUp> powerUps;
	public Player player;
	public Vector2D exitPoint;
	public List<Sprite> backgroundLayer1;
	public List<Sprite> backgroundLayer2;
	public List<Sprite> backgroundLayer3;
	public List<Sprite> backgroundLayer4;
	public int currentLevel;
	private void loadMapFromXML(String fileName) {
		try {
			XmlElement root = Xmlwise.loadXml("levels/" + fileName);

			this.tileWidth = Integer.valueOf(root.getAttribute("tilewidth"));
			this.tileHeight = Integer.valueOf(root.getAttribute("tileheight"));
			this.mapWidth = Integer.valueOf(root.getAttribute("width"));
			this.mapHeight = Integer.valueOf(root.getAttribute("height"));
			XmlElement tilesetNode = root.getFirst();
			XmlElement imageSourceNode = tilesetNode.getFirst();
			String imageURL = imageSourceNode.getAttribute("source");
			this.tileset = new Image(new FileInputStream("levels/" + imageURL));


			// load data
			LinkedList<XmlElement> layerNode = root.get("layer");
			XmlElement dataNode = layerNode.getFirst().get("data").getFirst();
			String[] dataValue = dataNode.getValue().trim().split(",");

			this.data = new int[mapWidth * mapHeight];
			for(int i = 0; i < data.length; i++) {
				String result = dataValue[i].trim();
				this.data[i] = Integer.parseInt(result);
			}
			
			// load objects: enemies and player
			enemies = new LinkedList<Enemy>();
			powerUps = new LinkedList<PowerUp>();
			LinkedList<XmlElement> objectGroups = root.get("objectgroup");
			for (XmlElement objectGroup: objectGroups) {
				// load objects: player, entrance, exit
				if (objectGroup.getAttribute("name").equals("objects")) {
					loadCharacterFromXmlElement(objectGroup.get("object"), "player");
					loadCharacterFromXmlElement(objectGroup, "exit");
				}
				// load enemies
				else if (objectGroup.getAttribute("name").equals("enemies")) {
					loadCharacterFromXmlElement(objectGroup, "Crawler");
					loadCharacterFromXmlElement(objectGroup, "MeanCrawler");
					loadCharacterFromXmlElement(objectGroup, "Flyer");
				}
				// load powerups object
				else if (objectGroup.getAttribute("name").equals("powerups")) {
					loadCharacterFromXmlElement(objectGroup, "Health");
				}
			}
			

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	private void loadCharacterFromXmlElement(List<XmlElement> list, String name) {
		for (XmlElement object: list) {
			if (object.getAttribute("name").equals(name))
			{
				String type = object.getAttribute("type");
				GameObject gameObject = null;
				switch (type) {
				case "player":
					gameObject = new Player("sprites/Player1.png");	
					Player player = (Player)gameObject;
					
					player.isActive = true;	
					this.player = player;
					break;	
				case "Health":
					gameObject = new PowerUp("sprites/Health.png");
					PowerUp powerUp = (PowerUp)gameObject;
					this.powerUps.add(powerUp);
					break;
				case "exit":
					//double diff1 = mapHeight * tileHeight - ((GameScene)this.scene).gameHeight();
					double exitX =  Double.valueOf(object.getAttribute("x").trim());
					double exitY =  Double.valueOf(object.getAttribute("y").trim()) ;//- diff1;
					exitPoint = new Vector2D(exitX, exitY);
					
					return;
					
				default:
					if (type.equals("Crawler")) gameObject = new Crawler("sprites/Crawler1.png");
					else if (type.equals("MeanCrawler")) gameObject = new MeanCrawler("sprites/MeanCrawler1.png");
					else if (type.equals("Flyer")) gameObject = new Flyer("sprites/Flyer1.png"); 
					else {
						gameObject = new Crawler("sprites/Crawler1.png");
					}
					Enemy enemy = (Enemy)gameObject;
					enemy.player = this.player;
					enemy.map = this;
					enemy.isActive = false;
					this.enemies.add(enemy);
					break;
				}
				double diff = mapHeight * tileHeight - ((GameScene)this.scene).gameHeight();
				double x =  Double.valueOf(object.getAttribute("x").trim());
				double y =  Double.valueOf(object.getAttribute("y").trim()) - diff;
				gameObject.position = new Vector2D(x, y);
				this.addChild(gameObject);
			
			}
		}
	}
	
	private void loadBackgrounds() {
		String levelName = this.levelBackgrounds[currentLevel - 1];
		//String levelName = "city";
		int[] layerNum = numPerLayers[currentLevel - 1];
		backgroundLayer1 = new LinkedList<Sprite>();
		for (int i = 0; i < layerNum[0]; i++) {
			Sprite background1 = new Sprite("images/" + levelName + "1-" + (i + 1) + ".png");
			GameScene gameScene = (GameScene)this.scene;
			background1.position = new Vector2D(background1.size.width / 2 + background1.size.width * i, background1.size.height / 2 - 100);
			backgroundLayer1.add(background1);
		}
		backgroundLayer2 = new LinkedList<Sprite>();
		for (int i = 0; i < layerNum[1]; i++) {
			Sprite background1 = new Sprite("images/" + levelName + "2-" + (i + 1) + ".png");
			GameScene gameScene = (GameScene)this.scene;
			background1.position = new Vector2D(background1.size.width / 2 + background1.size.width * i,background1.size.height / 2 - 100);
			backgroundLayer2.add(background1);
		}
		backgroundLayer3 = new LinkedList<Sprite>();
		for (int i = 0; i < layerNum[2]; i++) {
			Sprite background1 = new Sprite("images/" + levelName + "3-" + (i + 1) + ".png");
			GameScene gameScene = (GameScene)this.scene;
			background1.position = new Vector2D(background1.size.width / 2 + background1.size.width * i, background1.size.height / 2 - 100);
			backgroundLayer3.add(background1);
		}
		backgroundLayer4 = new LinkedList<Sprite>();
		for (int i = 0; i < layerNum[3]; i++) {
			Sprite background1 = new Sprite("images/" + levelName + "4-" + (i + 1) + ".png");
			GameScene gameScene = (GameScene)this.scene;
			background1.position = new Vector2D(background1.size.width / 2 + background1.size.width * i, background1.size.height/ 2 - 100);
			backgroundLayer4.add(background1);
		}
	}
	
	

	public TileMap(GameScene scene, int level) {
		this.scene = scene;
		this.currentLevel = level;
		loadMapFromXML("level" + level + ".tmx");
		loadBackgrounds();
	}

	public void addChild(Sprite sprite) {
		children.add(sprite);

	}

	public void removeChild(Sprite sprite) {
		children.remove(sprite);
	}

	public boolean hasWallAtTileCoord(Vector2D tileCoord) {
		int index = (int)tileCoord.y * this.mapWidth + (int)tileCoord.x;
		return this.data[index] > 0;
	}


	public int mapWidthInPixel() {
		return mapWidth * tileWidth;
	}
	public int mapHeightInPixel() {
		return mapHeight * tileHeight;
	}
	public double diffHeight() { return mapHeightInPixel() - this.scene.getHeight(); }

	public Vector2D coordForPoint(Vector2D point) {

		return new Vector2D((int)point.x /  tileWidth, (int)point.y / tileHeight);
	}

	public Rect rectFromCoord(Vector2D tileCoord) {

		return new Rect(tileWidth * tileCoord.x, tileHeight * tileCoord.y, tileWidth, tileHeight);
	}

	public int tileGIDAtTileCoord(Vector2D tileCoord) {

		int index= (int) (tileCoord.y * mapWidth + tileCoord.x);
		if (index < 0 || index > mapHeight*mapWidth - 1) return 0;
		return this.data[index];
	}

	public void setPosition(Vector2D position) {

		Vector2D offset = Vector2DHelper.SubstractVector(this.position, position);
		// set position for childred, because children's position relative to map's position
		for (Sprite sprite: this.children) {
			sprite.position = Vector2DHelper.SubstractVector(sprite.position, offset);
		}
		for (Sprite background: this.backgroundLayer1) {
			background.position = Vector2DHelper.SubstractVector(background.position, Vector2DHelper.MutilByScalar(offset, 0.4));
		}
		for (Sprite background: this.backgroundLayer2) {
			background.position = Vector2DHelper.SubstractVector(background.position, Vector2DHelper.MutilByScalar(offset, 0.3));
		}
		for (Sprite background: this.backgroundLayer3) {
			background.position = Vector2DHelper.SubstractVector(background.position, Vector2DHelper.MutilByScalar(offset, 0.1));
		}
		for (Sprite background: this.backgroundLayer4) {
			background.position = Vector2DHelper.SubstractVector(background.position, Vector2DHelper.MutilByScalar(offset, 0.05));
		}
		this.position = position;
	}


	public void render(GraphicsContext gc) {	
		for (Sprite background: this.backgroundLayer4) {
			background.render(gc);
		}
		for (Sprite background: this.backgroundLayer3) {
			background.render(gc);
		}
		for (Sprite background: this.backgroundLayer2) {
			background.render(gc);
		}
		for (Sprite background: this.backgroundLayer1) {
			background.render(gc);
		}
		
		for (int i = 0; i < mapWidth * mapHeight; i++) {
			int gid = data[i];
			if (gid == 0) continue;
			gid--;
			int row = i / mapWidth;
			int col = i % mapWidth;
			double dy = this.position.y + row * tileHeight;
			double dx = this.position.x + col * tileWidth;

			int tileRow = gid / 5;
			int tileCol = gid % 5;
			int sx = tileCol * 32;
			int sy = tileRow * 32;

			gc.drawImage(tileset, sx, sy, 32, 32, dx, dy, tileWidth, tileHeight);
		}
		
		

	}


}