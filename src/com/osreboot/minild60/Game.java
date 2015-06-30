package com.osreboot.minild60;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.minild60.Level.RecordTile;
import com.osreboot.minild60.Level.SpawnTile;
import com.osreboot.minild60.Level.WallSpeakerTile;
import com.osreboot.minild60.TextureManager.TextureSeries;
import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.HvlTextureUtil;
import com.osreboot.ridhvl.display.HvlDisplay;
import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.painter.HvlRenderFrame;
import com.osreboot.ridhvl.painter.HvlRenderFrame.HvlRenderFrameProfile;
import com.osreboot.ridhvl.painter.shader.HvlShader;
import com.osreboot.ridhvl.particle.collection.HvlRadialParticleSystem;
import com.osreboot.ridhvl.tile.HvlLayeredTileMap;

public class Game {
	public static float cameraX, cameraY;
	public static float prevCameraX, prevCameraY;
	
	public static final int ENEMIESPERSPAWNER = 3;

	public static HvlLayeredTileMap map;
	public static Level currentLevel;
	
	public static List<Enemy> enemies;
	public static Map<HvlRadialParticleSystem, Float> deathParticles, confetti;
	public static List<Record> records;

	private static Player player;

	private static HvlRenderFrame shockwaveFrame;
	private static HvlShader shockwaveShader;
	private static int[] speakerCoordsX = new int[10];
	private static int[] speakerCoordsY = new int[10];
	
	public static void reset() {
		records = new LinkedList<>();
		
		speakerCoordsX = new int[10];
		speakerCoordsY = new int[10];
		for (SpawnTile tile : currentLevel.spawnTiles)
		{
			tile.hasSpawned = false;
		}
		for (RecordTile tile : currentLevel.recordTiles)
		{
			records.add(new Record(getWorldX(tile.x), getWorldY(tile.y)));
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
		confetti = new HashMap<>();
		
		AchievementManager.onLevelReset();
	}

	public static void initialize() {
		shockwaveShader = new HvlShader(HvlShader.VERTEX_DEFAULT, HvlShader.PATH_SHADER_DEFAULT + "ShockwavePost" + HvlShader.SUFFIX_FRAGMENT);
		shockwaveFrame = new HvlRenderFrame(HvlRenderFrameProfile.DEFAULT, Display.getWidth(), Display.getHeight());
		//currentLevel = Level.levels.get(0);
		map = currentLevel.getMap();

		reset();
	}

	public static void update(float delta){
		
		HvlRenderFrame.setCurrentRenderFrame(shockwaveFrame);
		prevCameraX = cameraX;
		prevCameraY = cameraY;
		
		for(WallSpeakerTile tile : currentLevel.wallSpeakers){
			if(getWorldX(tile.x) > 0 && getWorldX(tile.x) < HvlDisplay.getDisplayMode().getCoordinateWidth() &&
					getWorldX(tile.y) > 0 && getWorldY(tile.y) < HvlDisplay.getDisplayMode().getCoordinateHeight()){
				if(currentLevel.wallSpeakers.indexOf(tile) < 10){
					speakerCoordsX[currentLevel.wallSpeakers.indexOf(tile)] = (int)getWorldX(tile.x);
					speakerCoordsY[currentLevel.wallSpeakers.indexOf(tile)] = (int)getWorldY(tile.y);
				}
			}
		}
		
		for (SpawnTile t : currentLevel.spawnTiles)
		{
			if (t.hasSpawned) continue;
			
			if (Game.getWorldX(t.x) >= 0 && Game.getWorldX(t.x) <= Display.getWidth() && Game.getWorldY(t.y) >= 0 && Game.getWorldY(t.y) <= Display.getHeight())
			{
				for (int i = 0; i < ENEMIESPERSPAWNER; i++)
				{
					enemies.add(new Enemy(Game.getWorldX(t.x), Game.getWorldY(t.y)));
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
		List<HvlRadialParticleSystem> toRemoveD = new LinkedList<>(), toRemoveC = new LinkedList<>();
		
		for (Map.Entry<HvlRadialParticleSystem, Float> entry : deathParticles.entrySet())
		{
			entry.setValue(entry.getValue() + delta);
			entry.getKey().setX(entry.getKey().getX() + (prevCameraX - cameraX));
			entry.getKey().setY(entry.getKey().getY() + (prevCameraY - cameraY));
			if (entry.getValue() > 3.0f)
			{
				toRemoveD.add(entry.getKey());
			}
			entry.getKey().draw(delta);
		}
		
		for (Map.Entry<HvlRadialParticleSystem, Float> entry : confetti.entrySet())
		{
			entry.setValue(entry.getValue() + delta);
			if (entry.getValue() > 10.0f)
			{
				toRemoveC.add(entry.getKey());
			}
			entry.getKey().draw(delta);
		}
		
		for (HvlRadialParticleSystem tr : toRemoveD)
		{
			deathParticles.remove(tr);
		}
		for (HvlRadialParticleSystem tr : toRemoveC)
		{
			confetti.remove(tr);
		}
		
		player.update(delta);
		player.draw(delta);
		for (Enemy e : enemies)
		{
			e.update(delta);
			e.draw(delta);
		}
		for (Record r : records)
		{
			r.draw(delta);
		}
		HvlRenderFrame.setCurrentRenderFrame(null);
		
		HvlShader.setCurrentShader(shockwaveShader);
		shockwaveShader.sendIntArray("xcoords", speakerCoordsX);
		shockwaveShader.sendIntArray("ycoords", speakerCoordsY);
		shockwaveShader.sendIntArray("ycoords", speakerCoordsY);
		shockwaveShader.sendFloat("time", Main.getTotalTime());
		shockwaveShader.sendFloat("playerX", (Display.getWidth()/2) + ((float)Math.cos(Math.toRadians(Game.player.getAngle())) * 35));
		shockwaveShader.sendFloat("playerY", (Display.getHeight()/2) + ((float)Math.sin(Math.toRadians(Game.player.getAngle())) * 35));
		shockwaveShader.sendFloat("targetX", (Display.getWidth()/2) + ((float)Math.cos(Math.toRadians(Game.player.getAngle())) * Player.KILLDISTANCE));
		shockwaveShader.sendFloat("targetY", (Display.getHeight()/2) + ((float)Math.sin(Math.toRadians(Game.player.getAngle())) * Player.KILLDISTANCE));
		shockwaveShader.sendFloat("intensity", Game.player.getAttackIntensity()*4);
		
		hvlDrawQuad(0, 0, Display.getWidth(), Display.getHeight(), shockwaveFrame);
		HvlShader.setCurrentShader(null);
		
		if (records.isEmpty())
		{
			HvlMenu.setCurrent(MenuManager.win);
			AchievementManager.onLevelFinish();
		}
		
		AchievementManager.onLevelUpdate(delta);
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
		HvlRadialParticleSystem tr = new HvlRadialParticleSystem(0, 0, 20, 20, TextureManager.getResource(TextureSeries.PLAY, 2));
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
	
	public static HvlRadialParticleSystem makeConfetti()
	{
		HvlRadialParticleSystem tr = new HvlRadialParticleSystem(0, 0, 15, 15, HvlTextureUtil.getColoredRect(32, 32, Color.white));
		tr.setSpawnRadius(256);
		tr.setMaxParticles(4);
		tr.setMinScale(0.75f);
		tr.setMaxScale(1.0f);
		tr.setxVelDecay(-0.5f);
		tr.setyVelDecay(0.25f);
		tr.setScaleDecay(0.0f);
		tr.setMinXVel(-128);
		tr.setMaxXVel(128);
		tr.setMinYVel(64);
		tr.setMaxYVel(128);
		tr.setMinRot(0f);
		tr.setMaxRot(360f);
		tr.setMinRotVel(45f);
		tr.setMaxRotVel(360f);
		tr.setRotVelDecay(0f);
		tr.setMinLifetime(5f);
		tr.setMaxLifetime(8f);
		tr.setStartColorOne(Color.black);
		tr.setStartColorTwo(Color.white);
		tr.setEndColorOne(Color.black);
		tr.setEndColorTwo(Color.white);
		tr.setColorCoordinated(false);
		tr.setParticlesPerSpawn(0);
		return tr;
	}
	
	public static HvlRadialParticleSystem makeBlood()
	{
		HvlRadialParticleSystem tr = new HvlRadialParticleSystem(0, 0, 15, 15, HvlTextureUtil.getColoredRect(32, 32, Color.white));
		tr.setSpawnRadius(256);
		tr.setMaxParticles(4);
		tr.setMinScale(0.75f);
		tr.setMaxScale(1.0f);
		tr.setxVelDecay(0);
		tr.setyVelDecay(1.5f);
		tr.setScaleDecay(0.0f);
		tr.setMinXVel(128);
		tr.setMaxXVel(128);
		tr.setMinYVel(128);
		tr.setMaxYVel(256);
		tr.setBaseHeight(64);
		tr.setBaseWidth(4);
		tr.setMinRot(-10);
		tr.setMaxRot(-10);
		tr.setMaxRotVel(0);
		tr.setMinRotVel(0);
		tr.setRotVelDecay(0f);
		tr.setMinLifetime(5f);
		tr.setMaxLifetime(8f);
		tr.setStartColorOne(Color.red);
		tr.setStartColorTwo(Color.red);
		tr.setEndColorOne(Color.red);
		tr.setEndColorTwo(Color.red);
		tr.setParticlesPerSpawn(0);
		return tr;
	}
}
