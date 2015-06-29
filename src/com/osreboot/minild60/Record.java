package com.osreboot.minild60;

import com.osreboot.minild60.TextureManager.TextureSeries;
import com.osreboot.ridhvl.painter.painter2d.HvlPainter2D;

public class Record {
	private float relX, relY;
	
	public static final float radius = 30f;
	
	public Record(float x, float y)
	{
		relX = x - Game.cameraX;
		relY = y - Game.cameraY;
	}
	
	public void draw(float delta)
	{
		HvlPainter2D.hvlDrawQuad(Game.cameraX + relX - radius, Game.cameraY + relY - radius, radius * 2, radius * 2, TextureManager.getResource(TextureSeries.PLAY, 3));
	}
}
