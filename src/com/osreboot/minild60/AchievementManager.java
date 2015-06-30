package com.osreboot.minild60;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import com.osreboot.minild60.TextureManager.TextureSeries;
import com.osreboot.ridhvl.config.HvlConfigIgnore;
import com.osreboot.ridhvl.config.HvlConfigUtil;
import com.osreboot.ridhvl.painter.painter2d.HvlPainter2D;

public class AchievementManager
{
	private static Queue<String> displayAchievements;
	private static float currentDisplayTime;
	
	@HvlConfigIgnore
	public static final float DISPLAYTIME = 5.0f;
	
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
		
		addAchievement(new Achievement(null, "Indirect Interaction", "Beat the second level (kill an enemy)", false));
		addAchievement(new Achievement(null, "Guardian", "Beat a level without killing any enemies.", false)); // done
		addAchievement(new Achievement(null, "Construction/Destruction", "Deal damage to a speaker.", false));
		addAchievement(new Achievement(null, "Preparation - Set it up, let it go", "Kill 10 enemies in 5 seconds", false));
		addAchievement(new Achievement(null, "Infection", "Get eaten by a bug.", false));
		addAchievement(new Achievement(null, "Random", "Gotten by (pseudo)-pure chance.", false));
		addAchievement(new Achievement(null, "Light and Darkness", "Kill 10 enemies while standing on a light tile", false));
		addAchievement(new Achievement(null, "Growth", "Beath 3 levels", false));
		addAchievement(new Achievement(null, "Swarms", "Have 30 enemies in a level at once", false));
		addAchievement(new Achievement(null, "Moon/Anti-Text", "Sit still for ten seconds (because you don't know how to play)", false));
		addAchievement(new Achievement(null, "Build The Level You Play", "Play level 7 (built by the developers)", false));
		addAchievement(new Achievement(null, "Chain Reaction", "Kill 20 enemies in 30 seconds", false));
		addAchievement(new Achievement(null, "Weird/Unexpected/Surprise", "Get killed from behind (backstab)", false));
		addAchievement(new Achievement(null, "Minimalist", "Kill one and only one enemy", false));
		addAchievement(new Achievement(null, "The Tower", "Beat the last level", false));
		addAchievement(new Achievement(null, "Roads", "Never touch a wall", false));
		addAchievement(new Achievement(null, "Advancing Wall of Doom", "Get killed by 15 enemies at once", false));
		addAchievement(new Achievement(null, "Caverns", "Never step on a light colored tile", false)); // Tutorial HAS to have light tiles
		addAchievement(new Achievement(null, "Exploration", "Walk 1000 tile lengths", false));
		addAchievement(new Achievement(null, "Islands", "Beat the island chain.", false));
		addAchievement(new Achievement(null, "Enemies as Weapons", "Collect the last record along with the twenty enemies that helped you find it.", false));
		addAchievement(new Achievement(null, "Discovery", "Find redacted", false));
		addAchievement(new Achievement(null, "Its dangerous to go alone! Take this!", "Beat a level with only one enemy left", false));
		addAchievement(new Achievement(null, "Escape", "Escape from a horde of 10 enemies", false)); // TENTATIVE
		addAchievement(new Achievement(null, "Alone", "Kill all of the enemies in a level.", false));
		addAchievement(new Achievement(null, "Tiny World", "Beat level two", false));
		addAchievement(new Achievement(null, "Evolution", "Die in a level and then beat it", false));
		addAchievement(new Achievement(null, "You are the Villain", "Kill 1000 enemies total", false));
		addAchievement(new Achievement(null, "Minimalism", "Only step on one kind of tile", false));
		addAchievement(new Achievement(null, "10 Seconds", "Beat a level in 10 seconds", false));
		addAchievement(new Achievement(null, "You Only Get One", "Die after collecting a single disk", false));
		addAchievement(new Achievement(null, "Beneath The Surface", "Find the secret within discovery", false));
		addAchievement(new Achievement(null, "Connected Worlds", "Look at every menu in the game", false));
		
		HvlConfigUtil.saveStaticConfig(AchievementManager.class, "res\\achievements");
		
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
 		if (displayAchievements.isEmpty()) return;
 		
 		HvlPainter2D.hvlDrawQuad(0, 0, 1280, 128, TextureManager.getResource(TextureSeries.UI, 6));
 		MenuManager.font.hvlDrawWord(displayAchievements.peek(), 16, 16, 0.35f, Color.red);
 		
 		currentDisplayTime += delta;
 		
 		if (currentDisplayTime >= DISPLAYTIME)
 		{
 			currentDisplayTime = 0;
 			displayAchievements.poll();
 		}
 	}

 	public static void onLevelFinish()
 	{
 		if (Game.currentLevel.spawnTiles.size() != 0)
 		{
 			if (Game.enemies.size() == Game.currentLevel.spawnTiles.size() * Game.ENEMIESPERSPAWNER)
 			{
 				setUnlocked("Guardian", true);
 			}
 		}
 	}
}
