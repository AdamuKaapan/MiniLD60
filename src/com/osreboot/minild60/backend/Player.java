package com.osreboot.minild60.backend;

import org.lwjgl.input.Keyboard;
import static org.lwjgl.opengl.GL11.*;

import com.osreboot.ridhvl.painter.HvlCursor;

public class Player {
	public static final float radius = 64f;
	public static final float movementSpeed = 256f;

	private float x, y;
	private float angle;

	public Player(float x, float y) {
		this.x = x;
		this.y = y;
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

		x += xTrans * delta * movementSpeed;
		y += yTrans * delta * movementSpeed;

		angle = (float) Math.toDegrees(Math.atan((HvlCursor.getCursorY() - y)
				/ (HvlCursor.getCursorX() - x)));
		if (HvlCursor.getCursorX() < x) {
			angle += 180;
		}
	}

	public void draw(float delta) {
		glBegin(GL_LINES);
		glColor4f(1, 0, 0, 1);
		glVertex2f(x, y);
		glColor4f(1, 0, 0, 1);
		glVertex2f(x + ((float) Math.cos(Math.toRadians(angle)) * 64f), y
				+ ((float) Math.sin(Math.toRadians(angle)) * 64f));
		glEnd();
	}
}
