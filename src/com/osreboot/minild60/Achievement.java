package com.osreboot.minild60;

import org.newdawn.slick.opengl.Texture;

public class Achievement {
	private Texture icon;
	private String title, description;
	private boolean isUnlocked;
	
	public Achievement(Texture icon, String title, String description,
			boolean isUnlocked) {
		super();
		this.icon = icon;
		this.title = title;
		this.description = description;
		this.isUnlocked = isUnlocked;
	}
	
	public Texture getIcon() {
		return icon;
	}
	public void setIcon(Texture icon) {
		this.icon = icon;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isUnlocked() {
		return isUnlocked;
	}
	public void setUnlocked(boolean isUnlocked) {
		this.isUnlocked = isUnlocked;
	}
	
	@Override
	public String toString()
	{
		return title.toLowerCase();
	}
}
