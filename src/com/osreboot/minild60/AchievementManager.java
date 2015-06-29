package com.osreboot.minild60;

import java.util.LinkedList;
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
	
	public static float DISPLAYTIME = 3.0f;
	
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
		
		addAchievement(new Achievement(null, "Hey!", "Meh", false));
		addAchievement(new Achievement(null, "Blargh!", "Meh", false));
		
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
			
			unlockeds[i] = val;
			
			if (val)
			{
				displayAchievements.add(titles[i].toLowerCase());
			}
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

 	public static void draw(float delta) {
 		if (displayAchievements.isEmpty()) return;
 		
 		HvlPainter2D.hvlDrawQuad(0, 0, 512, 128, TextureManager.getResource(TextureSeries.UI, 6));
 		MenuManager.font.hvlDrawWord(displayAchievements.peek(), 16, 16, 0.35f, Color.red);
 		
 		currentDisplayTime += delta;
 		
 		if (currentDisplayTime >= DISPLAYTIME)
 		{
 			currentDisplayTime = 0;
 			displayAchievements.poll();
 		}
 	}
}
