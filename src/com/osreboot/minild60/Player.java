package com.osreboot.minild60;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.minild60.ControlManager.Action;
import com.osreboot.minild60.Level.WallSpeakerTile;
import com.osreboot.minild60.TextureManager.TextureSeries;
import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.painter.HvlCursor;
import com.osreboot.ridhvl.painter.painter2d.HvlPainter2D;

public class Player {
	public static final int COLLIDABLE_LAYER = 1;

	public static final float RADIUS = 30f;
	public static final float MOVEMENT_SPEED = 256;

	private float angle;

	public Player() {
		this.angle = 0;
	}

	public void update(float delta) {
		float xTrans = (ControlManager.isActionTriggering(Action.MOVELEFT) ? -1 : 0) + (ControlManager.isActionTriggering(Action.MOVERIGHT) ? 1 : 0);
		float yTrans = (ControlManager.isActionTriggering(Action.MOVEUP) ? -1 : 0) + (ControlManager.isActionTriggering(Action.MOVEDOWN) ? 1 : 0);

		float len = (float) Math
				.sqrt(Math.pow(xTrans, 2) + Math.pow(yTrans, 2));
		if (len != 0) {
			xTrans /= len;
			yTrans /= len;
		}

		xTrans *= delta * MOVEMENT_SPEED;
		yTrans *= delta * MOVEMENT_SPEED;

		if (isBlockNear(RADIUS + xTrans, (RADIUS * 0.99f))
				|| isBlockNear(RADIUS + xTrans, -(RADIUS * 0.99f)))
			xTrans = Math.min(xTrans, 0);// TODO fix pixels left after collision
		if (isBlockNear((RADIUS * 0.99f), RADIUS + yTrans)
				|| isBlockNear(-(RADIUS * 0.99f), RADIUS + yTrans))
			yTrans = Math.min(yTrans, 0);
		if (isBlockNear(-RADIUS + xTrans, (RADIUS * 0.99f))
				|| isBlockNear(-RADIUS + xTrans, -(RADIUS * 0.99f)))
			xTrans = Math.max(xTrans, 0);
		if (isBlockNear((RADIUS * 0.99f), -RADIUS + yTrans)
				|| isBlockNear(-(RADIUS * 0.99f), -RADIUS + yTrans))
			yTrans = Math.max(yTrans, 0);

		Game.cameraX -= xTrans;
		Game.cameraY -= yTrans;

		angle = (float) Math.toDegrees(Math.atan2((Display.getHeight() / 2)
				- HvlCursor.getCursorY(),
				(Display.getWidth() / 2) - HvlCursor.getCursorX())) - 180;
	}

	public void draw(float delta) {
		HvlPainter2D.hvlRotate(Display.getWidth() / 2, Display.getHeight() / 2,
				angle);
		HvlPainter2D.hvlDrawQuad(Display.getWidth() / 2 - RADIUS,
				Display.getHeight() / 2 - RADIUS, 2 * RADIUS, 2 * RADIUS,
				TextureManager.getResource(TextureSeries.UI, 1), Color.red);
		HvlPainter2D.hvlResetRotation();

		for (WallSpeakerTile tile : Game.currentLevel.wallSpeakers) {
			float playerX = (float) Display.getWidth() / 2;
			float playerY = (float) Display.getHeight() / 2;

			float intersectionX = 0;
			float intersectionY = 0;
			boolean intersects = false;
			
			
			float distance = HvlMath.distance(Game.getWorldX(tile.x), Game.getWorldY(tile.y), playerX, playerY);
			//System.out.println(distance);
			for(float f = 0; f < distance; f++){
				float xPoint = lerp(Game.getWorldX(tile.x), playerX, f/distance);
				float yPoint = lerp(Game.getWorldY(tile.y), playerY, f/distance);
				
				if(Game.map.getLayer(COLLIDABLE_LAYER).getTile(Game.getTileX(xPoint), Game.getTileY(yPoint)) != null && Game.getTileX(xPoint) != tile.x && Game.getTileY(yPoint) != tile.y){
					intersects = true;
					intersectionX = xPoint;
					intersectionY = yPoint;
					//System.out.println("Triggered " + f);
					//break;
				}
				
				glBindTexture(GL_TEXTURE_2D, 0);
				glBegin(GL_LINES);
				glColor4f(intersects ? 1 : 0, intersects ? 0 : 1, 0, 1);
				glVertex2f(Game.getWorldX(Game.getTileX(xPoint)) + 5, Game.getWorldY(Game.getTileY(yPoint)) + 5);
				glEnd();
			}

			glBindTexture(GL_TEXTURE_2D, 0);
			glBegin(GL_LINES);
			glColor4f(1, 0, 0, 1);
			glVertex2f(Game.getWorldX(tile.x), Game.getWorldY(tile.y));
			glVertex2f(intersects ? intersectionX : playerX, intersects ? intersectionY : playerY);
			glEnd();
		}
	}

	private float lerp(float i1, float i2, float lerp){
		return i1 + lerp * (i2 - i1);
	}
	
	private boolean isBlockNear(float xMod, float yMod) {
		float w = (float) Display.getWidth() / 2;
		float h = (float) Display.getHeight() / 2;
		float shiftedX = w - Game.cameraX + xMod;
		float shiftedY = h - Game.cameraY + yMod;
		int tileX = (int) (shiftedX / Game.map.getTileWidth());
		int tileY = (int) (shiftedY / Game.map.getTileHeight());

		if (tileX >= Game.map.getLayer(COLLIDABLE_LAYER).getMapWidth()
				|| tileX < 0)
			return true;
		if (tileY >= Game.map.getLayer(COLLIDABLE_LAYER).getMapHeight()
				|| tileY < 0)
			return true;

		return Game.map.getLayer(COLLIDABLE_LAYER).getTile(tileX, tileY) != null;
	}
}
