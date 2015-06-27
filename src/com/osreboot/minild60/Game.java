package com.osreboot.minild60;

import org.newdawn.slick.Color;

import com.osreboot.minild60.backend.Player;
import com.osreboot.ridhvl.HvlTextureUtil;
import com.osreboot.ridhvl.painter.painter2d.HvlPainter2D;

public class Game {
	public static float cameraX, cameraY;

	private static Player player;

	public static void reset() {
		player = new Player();
	}

	public static void initialize() {
		reset();
	}

	public static void update(float delta) {
		player.update(delta);
		player.draw(delta);
		HvlPainter2D.hvlDrawQuad(-16 + cameraX, 16 + cameraY, 32, 32,
				HvlTextureUtil.getColoredRect(32, 32, Color.blue));
	}
}
