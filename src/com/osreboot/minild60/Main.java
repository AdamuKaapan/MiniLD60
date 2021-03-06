package com.osreboot.minild60;

import org.lwjgl.opengl.GL11;

import com.osreboot.ridhvl.display.collection.HvlDisplayModeDefault;
import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.template.HvlTemplate2D;

public class Main extends HvlTemplate2D {
	
	public static void main(String[] args){
		new Main();
	}
	
	public Main(){
		super(60, 1280, 720, "Audiovisual Extermination by Wub Alliance", new HvlDisplayModeDefault());//TODO icon and change title
	}
	
	private static float totalTime = 0;
	
	public static float getTotalTime(){
		return totalTime;
	}
	
	@Override
	public void initialize() {
		System.out.println(GL11.glGetString(GL11.GL_VERSION));
		TextureManager.initialize();
		SoundManager.initialize();
		MenuManager.initialize();
	}

	@Override
	public void update(float delta) {
		totalTime += delta;
		SoundManager.update();
		HvlMenu.updateMenus(delta);
		MenuManager.update(delta);
		MenuManager.postUpdate(delta);
		AchievementManager.draw(delta);
	}
}
