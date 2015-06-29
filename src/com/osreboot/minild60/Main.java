package com.osreboot.minild60;

import com.osreboot.ridhvl.display.collection.HvlDisplayModeDefault;
import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.template.HvlTemplate2DBasic;

public class Main extends HvlTemplate2DBasic {
	
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
	}
}
