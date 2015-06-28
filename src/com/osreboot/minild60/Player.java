package com.osreboot.minild60;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import static org.lwjgl.opengl.GL11.*;

import com.osreboot.minild60.TextureManager.TextureSeries;
import com.osreboot.ridhvl.painter.HvlCursor;
import com.osreboot.ridhvl.painter.painter2d.HvlPainter2D;

public class Player {
	public static final float radius = 32f;
	public static final float movementSpeed = 256f;

	private float angle;

	public Player() {
		this.angle = 0;
	}

	public void update(float delta) {
		float xTrans = 0.0f, yTrans = 0.0f;

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			xTrans += 1.0f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			xTrans -= 1.0f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			yTrans -= 1.0f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			yTrans += 1.0f;
		}

		float len = (float) Math
				.sqrt(Math.pow(xTrans, 2) + Math.pow(yTrans, 2));
		if (len != 0) {
			xTrans /= len;
			yTrans /= len;
		}

		float w = (float) Display.getWidth() / 2;
		float h = (float) Display.getHeight() / 2;

		Game.cameraX -= xTrans * delta * movementSpeed;
		Game.cameraY -= yTrans * delta * movementSpeed;

		if (isCollidedOnRight() && xTrans > 0)
			Game.cameraX += xTrans * delta * movementSpeed;
		if (isCollidedOnBottom() && yTrans > 0)
			Game.cameraY += yTrans * delta * movementSpeed;
		if (isCollidedOnLeft() && xTrans < 0)
			Game.cameraX += xTrans * delta * movementSpeed;
		if (isCollidedOnTop() && yTrans < 0)
			Game.cameraY += yTrans * delta * movementSpeed;

		angle = (float) Math.toDegrees(Math.atan((h - HvlCursor.getCursorY())
				/ (w - HvlCursor.getCursorX())));
		if (HvlCursor.getCursorX() < Display.getWidth() / 2) {
			angle += 180;
		}
	}

	public void draw(float delta) {
		HvlPainter2D.hvlRotate(Display.getWidth() / 2, Display.getHeight() / 2,
				angle);
		HvlPainter2D.hvlDrawQuad(Display.getWidth() / 2 - radius,
				Display.getHeight() / 2 - radius, 2 * radius, 2 * radius,
				TextureManager.getResource(TextureSeries.UI, 1), Color.red);
		HvlPainter2D.hvlResetRotation();

		glBindTexture(GL_TEXTURE_2D, 0);
		glBegin(GL_LINES);
		glColor4f(1, 0, 0, 1);
		glVertex2f(0, 0);
		glVertex2f((Display.getWidth() / 2), (Display.getHeight() / 2));
		glEnd();
	}

	public boolean isCollidedOnRight() {
		float w = (float) Display.getWidth() / 2;
		float h = (float) Display.getHeight() / 2;
		float shiftedX = w - Game.cameraX - radius * 0.75f;
		float shiftedY = h - Game.cameraY;
		int tileX = (int) (shiftedX / Game.map.getTileWidth());
		int tileY = (int) (shiftedY / Game.map.getTileHeight());

		if (tileX + 1 > Game.map.getLayer(1).getMapWidth() - 1) return true;
		
		return Game.map.getLayer(1).getTile(tileX + 1, tileY) != null;
	}

	public boolean isCollidedOnBottom() {
		float w = (float) Display.getWidth() / 2;
		float h = (float) Display.getHeight() / 2;
		float shiftedX = w - Game.cameraX;
		float shiftedY = h - Game.cameraY - radius * 0.75f;
		int tileX = (int) (shiftedX / Game.map.getTileWidth());
		int tileY = (int) (shiftedY / Game.map.getTileHeight());

		if (tileY + 1 > Game.map.getLayer(1).getMapHeight() - 1) return true;

		return Game.map.getLayer(1).getTile(tileX, tileY + 1) != null;
	}

	public boolean isCollidedOnLeft() {
		float w = (float) Display.getWidth() / 2;
		float h = (float) Display.getHeight() / 2;
		float shiftedX = w - Game.cameraX + radius * 0.75f;
		float shiftedY = h - Game.cameraY;
		int tileX = (int) (shiftedX / Game.map.getTileWidth());
		int tileY = (int) (shiftedY / Game.map.getTileHeight());

		if (tileX < 1) return true;
		
		return Game.map.getLayer(1).getTile(tileX - 1, tileY) != null;
	}

	public boolean isCollidedOnTop() {
		float w = (float) Display.getWidth() / 2;
		float h = (float) Display.getHeight() / 2;
		float shiftedX = w - Game.cameraX;
		float shiftedY = h - Game.cameraY + radius * 0.75f;
		int tileX = (int) (shiftedX / Game.map.getTileWidth());
		int tileY = (int) (shiftedY / Game.map.getTileHeight());
		if (tileY < 1) return true;

		return Game.map.getLayer(1).getTile(tileX, tileY - 1) != null;
	}
}
