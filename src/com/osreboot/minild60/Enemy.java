package com.osreboot.minild60;

import org.lwjgl.opengl.Display;

import com.osreboot.minild60.TextureManager.TextureSeries;
import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.painter.painter2d.HvlPainter2D;

public class Enemy {
	public static final float radius = 8f;
	public static final float movementSpeed = 128f;
	
	private float relX, relY;
	
	private float offset;
	private float health;
	
	public Enemy(float x, float y)
	{
		this.relX = x - Game.cameraX;
		this.relY = y - Game.cameraY;
		offset = (float)(Math.random()*Math.PI*4);
		health = 1.0f;
	}
	
	public void update(float delta)
	{
		float xC = (float) Display.getWidth() / 2;
		float yC = (float) Display.getHeight() / 2;
		
		float d = HvlMath.distance(Game.cameraX + relX, Game.cameraY + relY, xC, yC);
		float xDiff = xC - (Game.cameraX + relX);
		float yDiff = yC - (Game.cameraY + relY);
		xDiff /= d;
		yDiff /= d;
		
		float xTrans = xDiff * delta * movementSpeed * (float)Math.max(Math.cos(offset + (Main.getTotalTime()*5)), 0.25f);
		float yTrans = yDiff * delta * movementSpeed * (float)Math.max(Math.sin(offset + (Main.getTotalTime()*5)), 0.25f);
		
		if (isBlockNear(radius + xTrans, (radius * 0.99f))
				|| isBlockNear(radius + xTrans, -(radius * 0.99f)))
			xTrans = Math.min(xTrans, 0);// TODO fix pixels left after collision
		if (isBlockNear((radius * 0.99f), radius + yTrans)
				|| isBlockNear(-(radius * 0.99f), radius + yTrans))
			yTrans = Math.min(yTrans, 0);
		if (isBlockNear(-radius + xTrans, (radius * 0.99f))
				|| isBlockNear(-radius + xTrans, -(radius * 0.99f)))
			xTrans = Math.max(xTrans, 0);
		if (isBlockNear((radius * 0.99f), -radius + yTrans)
				|| isBlockNear(-(radius * 0.99f), -radius + yTrans))
			yTrans = Math.max(yTrans, 0);
		
		relX += xTrans;
		relY += yTrans;
	}
	
	public void draw(float delta)
	{
		float xC = (float) Display.getWidth() / 2;
		float yC = (float) Display.getHeight() / 2;
		
		HvlPainter2D.hvlRotate(Game.cameraX + relX, Game.cameraY + relY, (float) Math.toDegrees(Math.atan2(Game.cameraY + relY - yC, Game.cameraX + relX - xC)));
		HvlPainter2D.hvlDrawQuad(Game.cameraX + relX - radius, Game.cameraY + relY - radius, radius * 2, radius * 2, TextureManager.getResource(TextureSeries.PLAY, 1));
		HvlPainter2D.hvlResetRotation();
	}
	
	public boolean isBlockNear(float xMod, float yMod) {
		float shiftedX = relX + xMod;
		float shiftedY = relY + yMod;
		int tileX = (int) (shiftedX / Game.map.getTileWidth());
		int tileY = (int) (shiftedY / Game.map.getTileHeight());

		if (tileX >= Game.map.getLayer(Player.COLLIDABLE_LAYER).getMapWidth()
				|| tileX < 0)
			return true;
		if (tileY >= Game.map.getLayer(Player.COLLIDABLE_LAYER).getMapHeight()
				|| tileY < 0)
			return true;
		
		return Game.map.getLayer(Player.COLLIDABLE_LAYER).getTile(tileX, tileY) != null;
	}

	public float getRelX() {
		return relX;
	}

	public float getRelY() {
		return relY;
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}
}
