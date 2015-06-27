package com.osreboot.minild60.backend;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import static org.lwjgl.opengl.GL11.*;

import com.osreboot.minild60.Game;
import com.osreboot.minild60.TextureManager;
import com.osreboot.minild60.TextureManager.TextureSeries;
import com.osreboot.ridhvl.painter.HvlCursor;
import com.osreboot.ridhvl.painter.painter2d.HvlPainter2D;

public class Player {
	public static final float radius = 64f;
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

//		Game.cameraX -= xTrans * delta * movementSpeed;
//		Game.cameraY -= yTrans * delta * movementSpeed;

		angle = (float) Math.toDegrees(Math.atan((HvlCursor.getCursorY() - (Display.getWidth() / 2))
				/ (HvlCursor.getCursorX() - (Display.getWidth() / 2))));
		if (HvlCursor.getCursorX() < Display.getWidth() / 2) {
			angle += 180;
		}
		
		System.out.println(angle);
	}

	public void draw(float delta) {
		HvlPainter2D.hvlRotate(Display.getWidth() / 2, Display.getHeight() / 2, angle);
		HvlPainter2D.hvlDrawQuad(Display.getWidth() / 2 - radius, Display.getHeight() / 2 - radius, 2 * radius, 2 * radius, TextureManager.getResource(TextureSeries.UI, 0), Color.red);
		HvlPainter2D.hvlResetRotation();
	}
}
