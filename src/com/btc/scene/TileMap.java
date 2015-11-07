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

	public int mapWidth;
	public int mapHeight;

	public int tileWidth;
	public int tileHeight;	

	public Image tileset;
	public int[] data;

	List<Sprite> children = new ArrayList<Sprite>();
	public List<Enemy> enemies;
	public Player player;

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
			LinkedList<XmlElement> objectGroups = root.get("objectgroup");
			for (XmlElement objectGroup: objectGroups) {
				// load objects: player, entrance, exit
				if (objectGroup.getAttribute("name").equals("objects")) {
					loadCharacterFromXmlElement(objectGroup.get("object"), "player");
				}
				// load enemies
				else if (objectGroup.getAttribute("name").equals("enemies")) {
					loadCharacterFromXmlElement(objectGroup, "Crawler");
					loadCharacterFromXmlElement(objectGroup, "MeanCrawler");
					loadCharacterFromXmlElement(objectGroup, "Flyer");
				}
				// load powerups object
				else if (objectGroup.getAttribute("name").equals("powerups")) {
					
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
				Character character = null;
				switch (type) {
				case "player":
					character = new Player("sprites/Player1.png");	
					character.isActive = true;	
					this.player = (Player)character;
					break;				
				default:
					if (type.equals("Crawler")) character = new Crawler("sprites/Crawler1.png");
					else if (type.equals("MeanCrawler")) character = new MeanCrawler("sprites/MeanCrawler1.png");
					else if (type.equals("Flyer")) character = new Flyer("sprites/Flyer1.png"); 
					else {
						character = new Crawler("sprites/Crawler1.png");
					}
					Enemy enemy = (Enemy)character;
					enemy.player = this.player;
					enemy.map = this;
					enemy.isActive = false;
					this.enemies.add(enemy);
					break;
				}
				double diff = mapHeight * tileHeight - ((GameScene)this.scene).gameHeight();
				double x =  Double.valueOf(object.getAttribute("x").trim());
				double y =  Double.valueOf(object.getAttribute("y").trim()) - diff;
				character.position = new Vector2D(x, y);
				this.addChild(character);
			
			}
		}
	}
	
	public TileMap(String imageNamed) {
		super(imageNamed);
		loadMapFromXML("level1.tmx");
	}

	public TileMap(GameScene scene) {
		this.scene = scene;
		loadMapFromXML("level1.tmx");
		System.out.println(children.size());
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
		this.position = position;
	}


	public void render(GraphicsContext gc) {	
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