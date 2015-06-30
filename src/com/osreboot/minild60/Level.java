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
		public boolean hasSpawned;
	}
	
	public static class WallSpeakerTile
	{
		public WallSpeakerTile(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int x, y;
	}
	
	public static class RecordTile
	{
		public RecordTile(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int x, y;
	}
	
	public static List<Level> levels;
	
	public static final int enemySpawnerTile = 63;
	public static final int wallSpeakerTile = 62;
	public static final int recordTile = 61;
	
	public static final float TILE_SIZE = 64;
	
	static
	{
		levels = new ArrayList<>();
		
		levels.add(new Level("res\\Level1", 0, 3, 7));
		levels.add(new Level("res\\Level2", 0, 3, 3));
		levels.add(new Level("res\\Level3", 3, 2, 2));
		levels.add(new Level("res\\AdamsLevel", 3, 28, 29));
		levels.add(new Level("res\\Level4", 5, 2, 2));
		levels.add(new Level("res\\Dubstep Level Idea 2", 5, 13, 14));
		levels.add(new Level("res\\Dubstep Level Idea 5", 7, 18, 19));
	}
	
	private HvlLayeredTileMap map;
	private int requiredAchievements;
	private int startX, startY;
	public List<SpawnTile> spawnTiles;
	public List<WallSpeakerTile> wallSpeakers;
	public List<RecordTile> recordTiles;	
	
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

			map = HvlLayeredTileMap.load(sb.toString(), TextureManager.getResource(TextureSeries.PLAY, 0), 0, 0, TILE_SIZE, TILE_SIZE);
			
			spawnTiles = new LinkedList<>();
			wallSpeakers = new LinkedList<>();
			recordTiles = new LinkedList<>();
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
					
					if (sTile.getTile() == recordTile)
					{
						recordTiles.add(new RecordTile(x, y));
						map.getLayer(1).setTile(x, y, null);
					}
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
