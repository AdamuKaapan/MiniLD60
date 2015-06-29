package com.osreboot.minild60;

import org.newdawn.slick.opengl.Texture;

import com.osreboot.ridhvl.config.HvlConfigIgnore;
import com.osreboot.ridhvl.config.HvlConfigUtil;

public class AchievementManager
{
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
		
		addAchievement(new Achievement(null, "Hey!", "Meh", false));
		
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
}
