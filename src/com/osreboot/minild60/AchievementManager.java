package com.osreboot.minild60;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import com.osreboot.minild60.TextureManager.TextureSeries;
import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.config.HvlConfigIgnore;
import com.osreboot.ridhvl.config.HvlConfigUtil;
import com.osreboot.ridhvl.painter.painter2d.HvlPainter2D;

public class AchievementManager
{
	public static float distanceWalked;
	public static int killedEnemies;
	private static float timeInLevel;
	
	public static boolean lookedAtLevelSelect = false;
	public static boolean lookedAtAchievements = false;
	public static boolean lookedAtOptions = false;
	
	private static Queue<String> displayAchievements;
	private static float currentDisplayTime;
	
	private static boolean hasMoved;
	private static float timeSinceMoving;
	
	@HvlConfigIgnore
	public static final float DISPLAYTIME = 3.0f;
	
	@HvlConfigIgnore
	public static Texture[] icons;
	
	@HvlConfigIgnore
	public static String[] titles, descriptions;
	
	public static boolean unlockeds[];
	
	static
	{
		icons = new Texture[0];
		titles = new String[0];
		descriptions = new String[0];
		unlockeds = new boolean[0];
		
		displayAchievements = new LinkedList<>();
		
		addAchievement(new Achievement(null, "Indirect Interaction", "Kill an enemy", false)); // done
		addAchievement(new Achievement(null, "Guardian", "Beat a level without killing any enemies.", false)); // done
//		addAchievement(new Achievement(null, "Construction/Destruction", "Deal damage to a speaker.", false));
//		addAchievement(new Achievement(null, "Preparation - Set it up, let it go", "Kill 10 enemies in 5 seconds", false));
		addAchievement(new Achievement(null, "Infection", "Get eaten by a bug", false)); // done
		addAchievement(new Achievement(null, "Random", "Gotten by (pseudo)-random luck.", false)); // done
//		addAchievement(new Achievement(null, "Light and Darkness", "Kill 10 enemies while standing on a light tile", false));
//		addAchievement(new Achievement(null, "Growth", "Beat 3 levels", false)); // include tutorials
		addAchievement(new Achievement(null, "Swarms", "Have 30 enemies in a level at once", false)); // done
		addAchievement(new Achievement(null, "Moon/Anti-Text", "Sit still for ten seconds because you don't know how to play", false)); // done
		addAchievement(new Achievement(null, "Build The Level You Play", "Play level 7 (built by the developers)", false)); // done
//		addAchievement(new Achievement(null, "Chain Reaction", "Kill 20 enemies in 30 seconds", false));
//		addAchievement(new Achievement(null, "Weird/Unexpected/Surprise", "Get killed from behind (backstab)", false));
		addAchievement(new Achievement(null, "Minimalist", "Kill one and only one enemy", false)); // done
		addAchievement(new Achievement(null, "The Tower", "Beat the last level", false)); // done
//		addAchievement(new Achievement(null, "Roads", "Never touch a wall", false));
//		addAchievement(new Achievement(null, "Advancing Wall of Doom", "Get killed by 15 enemies at once", false));
		addAchievement(new Achievement(null, "Caverns", "Never step on a light colored tile", false)); // Tutorial HAS to have light tiles
		addAchievement(new Achievement(null, "Exploration", "Walk 1000 tile lengths", false)); // done
//		addAchievement(new Achievement(null, "Islands", "Beat the island chain.", false));
		addAchievement(new Achievement(null, "Enemies as Weapons", "Collect the last record along with the twenty enemies that helped you find it.", false));
//		addAchievement(new Achievement(null, "Discovery", "Find redacted", false));
		addAchievement(new Achievement(null, "Its dangerous to go alone! Take this!", "Beat a level with only one enemy left", false)); // done
//		addAchievement(new Achievement(null, "Escape", "Escape from a horde of 10 enemies", false)); // TENTATIVE
		addAchievement(new Achievement(null, "Alone", "Kill all of the enemies in a level.", false)); // done
		addAchievement(new Achievement(null, "Tiny World", "Beat level two", false)); // done
//		addAchievement(new Achievement(null, "Evolution", "Die in a level and then beat it", false));
		addAchievement(new Achievement(null, "You are the Villain", "Kill 1000 enemies total", false)); // done
//		addAchievement(new Achievement(null, "Minimalism", "Only step on one kind of tile", false));
		addAchievement(new Achievement(null, "10 Seconds", "Beat a level in 10 seconds", false)); // done
//		addAchievement(new Achievement(null, "You Only Get One", "Die after collecting a single disk", false));
//		addAchievement(new Achievement(null, "Beneath The Surface", "Find the secret within discovery", false));
		addAchievement(new Achievement(null, "Connected Worlds", "Look at every menu in the game", false)); // done
		
		HvlConfigUtil.loadStaticConfig(AchievementManager.class, "res\\achievements");
	}
	
	private static void addAchievement(Achievement acc)
	{
		resizeArrays(unlockeds.length + 1);
		icons[icons.length - 1] = acc.getIcon();
		titles[titles.length - 1] = acc.getTitle();
		descriptions[titles.length - 1] = acc.getDescription();
		unlockeds[unlockeds.length - 1] = acc.isUnlocked();
	}
	
	public static Achievement getAchievement(String name)
	{
		for (int i = 0; i < titles.length; i++)
		{
			if (!titles[i].equals(name)) continue;
			
			return new Achievement(icons[i], titles[i], descriptions[i], unlockeds[i]);
		}
		
		return null;
	}
	
	public static int getNumberUnlocked()
	{
		int count = 0;
		
		for (boolean b : unlockeds)
		{
			if (b) count++;
		}
		
		return count;
	}
	
	public static void setUnlocked(String name, boolean val)
	{
		for (int i = 0; i < titles.length; i++)
		{
			if (!titles[i].equals(name)) continue;
			
			if (val && !unlockeds[i])
			{
				if (displayAchievements.size() == 0)
					displayAchievements.add("achievement get!");
				displayAchievements.add(titles[i].toLowerCase());
			}
			
			unlockeds[i] = val;
			
			HvlConfigUtil.saveStaticConfig(AchievementManager.class, "res\\achievements");
		}
	}
	
 	private static void resizeArrays(int size)
	{
		Texture[] newIcons = new Texture[size];
		for (int i = 0; i < icons.length; i++)
			newIcons[i] = icons[i];
		
		String[] newTitles = new String[size];
		for (int i = 0; i < titles.length; i++)
			newTitles[i] = titles[i];
		
		String[] newDescs = new String[size];
		for (int i = 0; i < descriptions.length; i++)
			newDescs[i] = descriptions[i];
		
		boolean[] newUnlock = new boolean[size];
		for (int i = 0; i < unlockeds.length; i++)
			newUnlock[i] = unlockeds[i];
		
		icons = newIcons;
		titles = newTitles;
		descriptions = newDescs;
		unlockeds = newUnlock;
	}

 	public static List<Achievement> getUnlockedAchievements()
 	{
 		List<Achievement> tr = new ArrayList<>();
 		
 		for (int i = 0; i < unlockeds.length; i++)
 		{
 			if (unlockeds[i])
 			{
 				tr.add(getAchievement(titles[i]));
 			}
 		}
 		
 		return tr;
 	}
 	
 	public static void draw(float delta) {
 		if (HvlMath.randomIntBetween(0, 100000) == 0)
 			setUnlocked("Random", true);
 		
 		if (lookedAtLevelSelect && lookedAtAchievements && lookedAtOptions)
 			setUnlocked("Connected Worlds", true);
 		
 		if (displayAchievements.isEmpty()) return;
 		
 		HvlPainter2D.hvlDrawQuad(0, 0, 1280, 128, TextureManager.getResource(TextureSeries.UI, 6));
 		MenuManager.font.hvlDrawWord(displayAchievements.peek(), 16, 16, 0.25f, Color.red);
 		
 		currentDisplayTime += delta;
 		
 		if (currentDisplayTime >= DISPLAYTIME)
 		{
 			currentDisplayTime = 0;
 			displayAchievements.poll();
 		}
 		
 		if (killedEnemies >= 1000)
 			setUnlocked("You are the Villain", true);
 	}

 	public static void onLevelReset()
 	{
 		hasMoved = false;
 		timeSinceMoving = 0.0f;
 		timeInLevel = 0.0f;
 	}
 	
 	public static void onLevelFinish()
 	{
 		if (Game.currentLevel.spawnTiles.size() != 0)
 		{
 			if (Game.enemies.size() == Game.currentLevel.spawnTiles.size() * Game.ENEMIESPERSPAWNER)
 				setUnlocked("Guardian", true);
 			if (Game.enemies.size() == (Game.currentLevel.spawnTiles.size() * Game.ENEMIESPERSPAWNER) - 1)
				setUnlocked("Minimalist", true);
 			if (Game.enemies.size() == 1)
 				setUnlocked("Its dangerous to go alone! Take this!", true);
 			if (Game.enemies.isEmpty())
 				setUnlocked("Alone", true);
 		}
 		
 		if (timeInLevel <= 20.0f)
 			setUnlocked("10 Seconds", true);
 		
 		if (Level.levels.indexOf(Game.currentLevel) == 1)
 			setUnlocked("Tiny World", true);
 		
 		if (Level.levels.indexOf(Game.currentLevel) == 6)
 			setUnlocked("Build The Level You Play", true);
 		
 		if (Level.levels.indexOf(Game.currentLevel) == Level.levels.size() - 1)
 			setUnlocked("The Tower", true);
 	}

 	public static void onLevelUpdate(float delta)
 	{
 		if (Game.enemies.size() >= 30)
 		{
 			setUnlocked("Swarms", true);
 		}
 		
 		timeInLevel += delta;
 	}
 	
 	public static void playerMovementUpdate(float delta, float xMove, float yMove)
 	{
 		if (xMove != 0.0f || yMove != 0.0f)
 		{
 			hasMoved = true;
 			timeSinceMoving = 0.0f;
 		}
 		else
 		{
 			timeSinceMoving += delta;
 		}
 		
 		distanceWalked += (float) Math.sqrt(Math.pow(xMove, 2) + Math.pow(yMove, 2));
 		
 		if (distanceWalked / Game.map.getTileWidth() > 1000)
 			setUnlocked("Exploration", true);
 		
 		if (timeSinceMoving >= 30.0f && !hasMoved)
		{
 			setUnlocked("Moon/Anti-Text", true);
		}
 	}
}
