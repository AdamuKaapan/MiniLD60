package com.osreboot.minild60;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.minild60.Level.WallSpeakerTile;
import com.osreboot.minild60.ControlManager.Action;
import com.osreboot.minild60.TextureManager.TextureSeries;
import com.osreboot.ridhvl.painter.HvlCursor;
import com.osreboot.ridhvl.painter.painter2d.HvlPainter2D;
import com.osreboot.ridhvl.tile.collection.HvlSimpleTile;

public class Player {
	public static final int COLLIDABLE_LAYER = 1;

	public static final float RADIUS = 30f;
	public static final float MOVEMENT_SPEED = 256;

	private float angle;

	public Player() {
		this.angle = 0;
	}

	public void update(float delta) {
		float xTrans = (ControlManager.isActionTriggering(Action.MOVELEFT) ? -1
				: 0)
				+ (ControlManager.isActionTriggering(Action.MOVERIGHT) ? 1 : 0);
		float yTrans = (ControlManager.isActionTriggering(Action.MOVEUP) ? -1
				: 0)
				+ (ControlManager.isActionTriggering(Action.MOVEDOWN) ? 1 : 0);

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
			float tileX = Game.map.getX()
					+ (tile.x * Game.map.getLayer(1).getTileWidth())
					+ (Game.map.getTileWidth() / 2);
			float tileY = Game.map.getY()
					+ (tile.y * Game.map.getLayer(1).getTileHeight())
					+ (Game.map.getTileHeight() / 2);

			float w = (float) Display.getWidth() / 2;
			float h = (float) Display.getHeight() / 2;
			float shiftedX = w - Game.cameraX;
			float shiftedY = h - Game.cameraY;
			int pTileX = (int) (shiftedX / Game.map.getTileWidth());
			int pTileY = (int) (shiftedY / Game.map.getTileHeight());

			System.out.println("Player: " + pTileX + ", " + pTileY);

			float distance = (float) Math.sqrt(Math.pow(tileY - h, 2) + Math.pow(tileX - w, 2));
			float xDiff = (tileX - w) / distance, yDiff = (tileY - h) / distance;
			xDiff *= 4;
			yDiff *= 4;
			
			float xPos = tileX;
			float yPos = tileY;
			
			while (Math.sqrt(Math.pow(yPos - h, 2) + Math.pow(xPos - w, 2)) < distance)
			{
				System.out.println(Math.sqrt(Math.pow(yPos - h, 2) + Math.pow(xPos - w, 2)));
				
				xPos += xDiff;
				yPos += yDiff;
				
				float shiftedXPos = xPos - Game.cameraX;
				float shiftedYPos = yPos - Game.cameraY;
				
				int someTileX = (int) (shiftedXPos / Game.map.getTileWidth());
				int someTileY = (int) (shiftedYPos / Game.map.getTileHeight());
				
				if (Game.map.getLayer(COLLIDABLE_LAYER).getTile(someTileX, someTileY) != null)
				{
					if (someTileX == tile.x && someTileY == tile.y)
					{
						continue;
					}
					
					System.out.println("Collision!" + someTileX + ", " + someTileY);
					break;
				}
			}

			glBindTexture(GL_TEXTURE_2D, 0);
			glBegin(GL_LINES);
			glColor4f(1, 0, 0, 1);
			glVertex2f(tileX, tileY);
			glVertex2f((Display.getWidth() / 2), (Display.getHeight() / 2));
			glEnd();
		}
	}

	public boolean isBlockNear(float xMod, float yMod) {
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
