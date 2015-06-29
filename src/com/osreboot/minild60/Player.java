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
import com.osreboot.ridhvl.particle.collection.HvlRadialParticleSystem;

public class Player {
	public static final boolean DEBUG_LINES = true;

	public static final int COLLIDABLE_LAYER = 1;

	public static final float RADIUS = 20f;
	public static final float MOVEMENT_SPEED = 200;
	public static final float LASER_ACCURACY = 48f;

	public static final float KILLANGLE = 45;
	public static final float KILLDISTANCE = 256;
	public static final float SPEAKERKILL = 512;
	public static final float SPEAKERANGLEKILL = 30;
	public static final float DAMAGERATE = 0.65f;

	private float angle;

	private boolean isDead;
	private boolean isReflecting;
	private float attackIntensity;

	public Player() {
		this.angle = 0;
	}

	public void update(float delta) {
		if (isDead)
			return;

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

		float cX = (float) Display.getWidth() / 2;
		float cY = (float) Display.getHeight() / 2;

		for (Enemy e : Game.enemies)
		{
			if (HvlMath.distance(cX, cY, Game.cameraX + e.getRelX(), Game.cameraY + e.getRelY()) <= RADIUS + Enemy.radius)
			{
				System.out.println("DEATH!");
				isDead = true;
				Game.reset();
			}
		}
	}

	public void draw(float delta) {
		isReflecting = false;
		attackIntensity = 0.0f;

		if (!isDead) {
			HvlPainter2D.hvlRotate(Display.getWidth() / 2,
					Display.getHeight() / 2, angle);
			HvlPainter2D.hvlDrawQuad(Display.getWidth() / 2 - RADIUS,
					Display.getHeight() / 2 - RADIUS, 2 * RADIUS, 2 * RADIUS,
					TextureManager.getResource(TextureSeries.UI, 1),
					Color.white);
			HvlPainter2D.hvlResetRotation();

			for (WallSpeakerTile tile : Game.currentLevel.wallSpeakers) {
				float playerX = (float) Display.getWidth() / 2;
				float playerY = (float) Display.getHeight() / 2;

				float intersectionX = 0;
				float intersectionY = 0;
				boolean intersects = false;

				float distance = HvlMath.distance(Game.getWorldX(tile.x),
						Game.getWorldY(tile.y), playerX, playerY);
				for (float f = 0; f < distance; f += LASER_ACCURACY) {
					float xPoint = lerp(Game.getWorldX(tile.x), playerX, f
							/ distance);
					float yPoint = lerp(Game.getWorldY(tile.y), playerY, f
							/ distance);

					if (Game.map.getLayer(COLLIDABLE_LAYER).getTile(
							Game.getTileX(xPoint), Game.getTileY(yPoint)) != null
							&& !(Game.getTileX(xPoint) == tile.x && Game
							.getTileY(yPoint) == tile.y)) {
						intersects = true;
						intersectionX = xPoint;
						intersectionY = yPoint;
						break;
					}
				}

				if(DEBUG_LINES){
					glBindTexture(GL_TEXTURE_2D, 0);
					glBegin(GL_LINES);
					glColor4f(1, 0, 0, 1);
					glVertex2f(Game.getWorldX(tile.x), Game.getWorldY(tile.y));
					glVertex2f(intersects ? intersectionX : playerX,
							intersects ? intersectionY : playerY);
					glEnd();
				}

				if (!intersects) {
					float angleToSpeaker = (float) Math.atan2(
							playerY - Game.getWorldY(tile.y),
							playerX - Game.getWorldX(tile.x))
							+ (float) Math.PI;

					float diff = (angleToSpeaker - (float) Math
							.toRadians(angle));
					diff %= Math.toRadians(360);

					if (diff < Math.toRadians(SPEAKERANGLEKILL) || diff > Math.toRadians(360 - SPEAKERANGLEKILL)) {
						if (diff > Math.PI)
							diff = (2 * (float)Math.PI) - diff;
						
						isReflecting = true;
						attackIntensity = Math.max(0, ((SPEAKERKILL - distance) / SPEAKERKILL) * ((float)(SPEAKERANGLEKILL - Math.toDegrees(Math.abs(diff))) / SPEAKERANGLEKILL));
						System.out.println(Math.toDegrees(diff));
						for (int i = 0; i < Game.enemies.size(); i++) {
							Enemy e = Game.enemies.get(i);
							float eX = Game.cameraX + e.getRelX();
							float eY = Game.cameraY + e.getRelY();
							float enemyDiff = (float) Math.atan2(eY - playerY,
									eX - playerX)
									- (float) Math.toRadians(angle);

							if (Math.abs(Math.toDegrees(enemyDiff)) < KILLANGLE) {
								float angleDamageMultiplier = ((-1.0f / KILLANGLE) * (float) Math
										.abs(Math.toDegrees(enemyDiff))) + 1;
								float distanceDamageMultiplier = ((-1.0f / KILLDISTANCE) * (float) HvlMath
										.distance(playerX, playerY, eX, eY)) + 1;
								float damage = DAMAGERATE
										* angleDamageMultiplier
										* distanceDamageMultiplier * attackIntensity * delta;
								e.setHealth(e.getHealth() - Math.max(damage, 0));
								if (e.getHealth() <= 0) {
									Game.enemies.remove(i--);

									float angle = (float) Math.atan2(Game.cameraY + e.getRelY() - playerY, Game.cameraX + e.getRelX() - playerX);
									HvlRadialParticleSystem ps = Game.makeDeathParticles();
									float min = 256;
									float max = 384;
									float pX = (float) Math.cos(angle);
									float pY = (float) Math.sin(angle);

									ps.setMinXVel(min * pX);
									ps.setMinYVel(min * pY);
									ps.setMaxXVel(max * pX);
									ps.setMaxYVel(max * pY);
									ps.setX(Game.cameraX + e.getRelX());
									ps.setY(Game.cameraY + e.getRelY());

									ps.spawnAllParticles();
									Game.deathParticles.put(ps, 0f);

									AchievementManager.setUnlocked("Hey!", true);
									AchievementManager.setUnlocked("Blargh!", true);
								}
							}
						}

						if(DEBUG_LINES){
							glBindTexture(GL_TEXTURE_2D, 0);
							glBegin(GL_LINES);
							glColor4f(0, 1, 0, 1);
							glVertex2f(playerX, playerY);
							glVertex2f(
									playerX
									+ ((float) Math.cos(Math
											.toRadians(angle)) * KILLDISTANCE),
											playerY
											+ ((float) Math.sin(Math
													.toRadians(angle)) * KILLDISTANCE));
							glEnd();

							glBindTexture(GL_TEXTURE_2D, 0);
							glBegin(GL_LINES);
							glColor4f(0, 0, 1, 1);
							glVertex2f(playerX, playerY);
							glVertex2f(
									playerX
									+ ((float) Math.cos(Math
											.toRadians(angle + KILLANGLE)) * KILLDISTANCE),
											playerY
											+ ((float) Math.sin(Math
													.toRadians(angle + KILLANGLE)) * KILLDISTANCE));
							glEnd();

							glBindTexture(GL_TEXTURE_2D, 0);
							glBegin(GL_LINES);
							glColor4f(0, 0, 1, 1);
							glVertex2f(playerX, playerY);
							glVertex2f(
									playerX
									+ ((float) Math.cos(Math
											.toRadians(angle - KILLANGLE)) * KILLDISTANCE),
											playerY
											+ ((float) Math.sin(Math
													.toRadians(angle - KILLANGLE)) * KILLDISTANCE));
							glEnd();
						}
					}
				}
			}
		}
	}

	private float lerp(float i1, float i2, float lerp) {
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

	public float getAngle() {
		return angle;
	}

	public boolean isReflecting() {
		return isReflecting;
	}

	public float getAttackIntensity() {
		return attackIntensity;
	}
}
