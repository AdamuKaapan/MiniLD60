package com.osreboot.minild60;

import org.lwjgl.opengl.Display;

import com.osreboot.ridhvl.tile.HvlLayeredTileMap;

public class Game {
	public static float cameraX, cameraY;

	public static HvlLayeredTileMap map;
	public static Level currentLevel;
	
	public static Enemy tEnemy;

	private static Player player;

	public static void reset() {
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
		player = new Player();
		tEnemy = new Enemy(512, 128);
	}

	public static void initialize() {
		currentLevel = Level.levels.get(0);
		map = currentLevel.getMap();

		reset();
	}

	public static void update(float delta) {
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
		tEnemy.update(delta);
		tEnemy.draw(delta);
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
