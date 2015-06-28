package com.osreboot.minild60;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.osreboot.minild60.TextureManager.TextureSeries;
import com.osreboot.ridhvl.tile.HvlLayeredTileMap;
import com.osreboot.ridhvl.tile.HvlTile;
import com.osreboot.ridhvl.tile.collection.HvlSimpleTile;

public class Level {
	public static class SpawnTile
	{
		public SpawnTile(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int x, y;
	}
	
	public static class WallSpeakerTile
	{
		public WallSpeakerTile(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int x, y;
	}
	
	public static class MobileSpeakerTile
	{
		public MobileSpeakerTile(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int x, y;
	}
	
	public static List<Level> levels;
	
	public static final int enemySpawnerTile = 63;
	public static final int wallSpeakerTile = 62;
	public static final int mobileSpeakerTile = 61;
	
	static
	{
		levels = new ArrayList<>();
		
		levels.add(new Level("res\\TestLevel.map", 0, 7, 11));
	}
	
	private HvlLayeredTileMap map;
	private int requiredAchievements;
	private int startX, startY;
	public List<SpawnTile> spawnTiles;
	public List<WallSpeakerTile> wallSpeakers;
	public List<MobileSpeakerTile> mobileSpeakers;
	
	public Level(String path, int requiredAchievements, int startX, int startY)
	{
		try {
			StringBuilder sb = new StringBuilder();
			BufferedReader read = new BufferedReader(new FileReader(path));

			String line;
			while ((line = read.readLine()) != null) {
				sb.append(line);
			}

			read.close();

			map = HvlLayeredTileMap.load(sb.toString(),
					TextureManager.getResource(TextureSeries.PLAY, 0), 0, 0,
					Player.radius * 2, Player.radius * 2);
			
			spawnTiles = new LinkedList<>();
			wallSpeakers = new LinkedList<>();
			mobileSpeakers = new LinkedList<>();
			for (int x = 0; x < map.getLayer(1).getMapWidth(); x++)
			{
				for (int y = 0; y < map.getLayer(1).getMapHeight(); y++)
				{
					HvlTile tile = map.getLayer(1).getTile(x, y);
					if (!(tile instanceof HvlSimpleTile)) continue;
					
					HvlSimpleTile sTile = (HvlSimpleTile) tile;
					
					if (sTile.getTile() == enemySpawnerTile)
					{
						spawnTiles.add(new SpawnTile(x, y));
						map.getLayer(1).setTile(x, y, null);
					}
					
					if (sTile.getTile() == wallSpeakerTile)
						wallSpeakers.add(new WallSpeakerTile(x, y));
					
					if (sTile.getTile() == mobileSpeakerTile)
						mobileSpeakers.add(new MobileSpeakerTile(x, y));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		this.requiredAchievements = requiredAchievements;
		this.startX = startX;
		this.startY = startY;
	}

	public HvlLayeredTileMap getMap() {
		return map;
	}

	public void setMap(HvlLayeredTileMap map) {
		this.map = map;
	}

	public int getRequiredAchievements() {
		return requiredAchievements;
	}

	public void setRequiredAchievements(int requiredAchievements) {
		this.requiredAchievements = requiredAchievements;
	}

	public int getStartX() {
		return startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}
}