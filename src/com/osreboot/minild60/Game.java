package com.osreboot.minild60;

import org.lwjgl.opengl.Display;

import com.osreboot.ridhvl.tile.HvlLayeredTileMap;

public class Game {
	public static float cameraX, cameraY;

	public static HvlLayeredTileMap map;
	public static Level currentLevel;

	private static Player player;

	public static void reset() {
		cameraX = (Display.getWidth() / 2) - (currentLevel.getStartX() * map.getTileWidth()) - Player.radius;
		cameraY = (Display.getHeight() / 2) - (currentLevel.getStartY() * map.getTileHeight()) - Player.radius;
		player = new Player();
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
	}
}
