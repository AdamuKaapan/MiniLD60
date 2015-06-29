package com.osreboot.minild60;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.minild60.Level.SpawnTile;
import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.HvlTextureUtil;
import com.osreboot.ridhvl.particle.collection.HvlRadialParticleSystem;
import com.osreboot.ridhvl.tile.HvlLayeredTileMap;

public class Game {
	public static float cameraX, cameraY;
	public static float prevCameraX, prevCameraY;

	public static HvlLayeredTileMap map;
	public static Level currentLevel;
	
	public static List<Enemy> enemies;
	public static Map<HvlRadialParticleSystem, Float> deathParticles;

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
		deathParticles = new HashMap<>();
	}

	public static void initialize() {
		currentLevel = Level.levels.get(0);
		map = currentLevel.getMap();

		reset();
	}

	public static void update(float delta) {
		prevCameraX = cameraX;
		prevCameraY = cameraY;
		
		for (SpawnTile t : currentLevel.spawnTiles)
		{
			if (t.hasSpawned) continue;
			
			if (Game.getWorldX(t.x) >= 0 && Game.getWorldX(t.x) <= Display.getWidth() && Game.getWorldY(t.y) >= 0 && Game.getWorldY(t.y) <= Display.getHeight())
			{
				for (int i = 0; i < 3; i++)
				{
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
		List<HvlRadialParticleSystem> toRemove = new LinkedList<>();
		
		for (Map.Entry<HvlRadialParticleSystem, Float> entry : deathParticles.entrySet())
		{
			entry.setValue(entry.getValue() + delta);
			entry.getKey().setX(entry.getKey().getX() + (prevCameraX - cameraX));
			entry.getKey().setY(entry.getKey().getY() + (prevCameraY - cameraY));
			if (entry.getValue() > 3.0f)
			{
				toRemove.add(entry.getKey());
			}
			entry.getKey().draw(delta);
		}
		
		for (HvlRadialParticleSystem tr : toRemove)
		{
			deathParticles.remove(tr);
		}
		
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
	
	public static HvlRadialParticleSystem makeDeathParticles()
	{
		HvlRadialParticleSystem tr = new HvlRadialParticleSystem(0, 0, 12, 12, HvlTextureUtil.getColoredRect(32, 32, Color.white));
		tr.setSpawnRadius(Enemy.radius);
		tr.setMaxParticles(256);
		tr.setMinScale(0.75f);
		tr.setMaxScale(1.0f);
		tr.setxVelDecay(-5f);
		tr.setyVelDecay(-5f);
		tr.setScaleDecay(0.0f);
		tr.setMinRot(0f);
		tr.setMaxRot(360f);
		tr.setMinRotVel(45f);
		tr.setMaxRotVel(360f);
		tr.setRotVelDecay(-0.95f);
		tr.setMinLifetime(0.25f);
		tr.setMaxLifetime(0.5f);
		tr.setStartColorOne(Color.green);
		tr.setStartColorTwo(Color.orange);
		tr.setEndColorOne(Color.green);
		tr.setEndColorTwo(Color.orange);
		tr.setParticlesPerSpawn(0);
//		tr.setMinTimeToSpawn(1.0f);
//		tr.setMaxTimeToSpawn(1.0f);
//		tr.setParticlesPerSpawn(5);
		return tr;
	}
}
