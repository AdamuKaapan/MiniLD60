package com.osreboot.minild60;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.newdawn.slick.Color;

import com.osreboot.minild60.TextureManager.TextureSeries;
import com.osreboot.minild60.backend.Player;
import com.osreboot.ridhvl.HvlTextureUtil;
import com.osreboot.ridhvl.painter.painter2d.HvlPainter2D;
import com.osreboot.ridhvl.tile.HvlLayeredTileMap;

public class Game {
	public static float cameraX, cameraY;

	private static HvlLayeredTileMap map;

	private static Player player;

	public static void reset() {
		player = new Player();
	}

	public static void initialize() {
		reset();

		try {
			String path = "res/TestLevel.map";
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
	}

	public static void update(float delta) {
		map.setX(cameraX);
		map.setY(cameraY);
		map.draw(delta);
		player.update(delta);
		player.draw(delta);
		HvlPainter2D.hvlDrawQuad(-16 + cameraX, 16 + cameraY, 32, 32,
				HvlTextureUtil.getColoredRect(32, 32, Color.blue));
	}
}
