package com.osreboot.minild60;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.osreboot.minild60.TextureManager.TextureSeries;
import com.osreboot.ridhvl.tile.HvlLayeredTileMap;

public class Level {
	public static List<Level> levels;
	
	static
	{
		levels = new ArrayList<>();
		
		levels.add(new Level("res\\TestLevel.map", 0, 7, 11));
	}
	
	private HvlLayeredTileMap map;
	private int requiredAchievements;
	private int startX, startY;
	
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
