package com.osreboot.minild60;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.opengl.Display;

import com.osreboot.minild60.Level.SpawnTile;
import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.tile.HvlLayeredTileMap;

public class Game {
	public static float cameraX, cameraY;

	public static HvlLayeredTileMap map;
	public static Level currentLevel;
	
	public static List<Enemy> enemies;

	private static Player player;

	public static void reset() {
		for (SpawnTile tile : currentLevel.spawnTiles)
		{
			tile.hasSpawned = false;
		}
		
		cameraX = (Display.getWidth() / 2)
				- (currentLevel.getStartX() * map.getTileWidth() - 32);
		cameraY = (Display.getHeight() / 2)
				- (currentLevel.getStartY() * map.getTileHeight() - 32);
		cameraX = (Display.getWidth() / 2)
				- (currentLevel.getStartX() * map.getTileWidth())
				+ Player.RADIUS;
		cameraY = (Display.getHeight() / 2)
				- (currentLevel.getStartY() * map.getTileHeight())
				+ Player.RADIUS;
		map.setX(cameraX);
		map.setY(cameraY);
		player = new Player();
		enemies = new LinkedList<>();
	}

	public static void initialize() {
		currentLevel = Level.levels.get(0);
		map = currentLevel.getMap();

		reset();
	}

	public static void update(float delta) {
		for (SpawnTile t : currentLevel.spawnTiles)
		{
			if (t.hasSpawned) continue;
			
			if (Game.getWorldX(t.x) >= 0 && Game.getWorldX(t.x) <= Display.getWidth() && Game.getWorldY(t.y) >= 0 && Game.getWorldY(t.y) <= Display.getHeight())
			{
				for (int i = 0; i < 3; i++)
				{
					if (enemies.size() < 1)
					enemies.add(new Enemy(Game.getWorldX(t.x + HvlMath.randomIntBetween(-2, 3)), Game.getWorldY(t.y) + HvlMath.randomIntBetween(-2, 3)));
				}
				t.hasSpawned = true;
			}
		}
		
		map.setX(cameraX);
		map.setY(cameraY);
		map.setCutOff(true);
		map.setxLeft(0);
		map.setxRight(Display.getWidth());
		map.setyTop(0);
		map.setyBottom(Display.getHeight());
		map.draw(delta);
		player.update(delta);
		player.draw(delta);
		for (Enemy e : enemies)
		{
			e.update(delta);
			e.draw(delta);
		}
	}
	
	public static int getTileX(float xPos)
	{
		return (int) ((xPos - map.getX()) / map.getTileWidth());
	}
	
	public static int getTileY(float yPos)
	{
		return (int) ((yPos - map.getY()) / map.getTileHeight());
	}
	
	public static float getWorldX(int xTile)
	{
		return map.getX() + (xTile * map.getTileWidth()) + (map.getTileWidth() / 2);
	}
	
	public static float getWorldY(int yTile)
	{
		return map.getY() + (yTile * map.getTileHeight() + (map.getTileHeight() / 2));
	}
}
