package com.osreboot.minild60;

import com.osreboot.minild60.backend.Player;
import com.osreboot.ridhvl.display.collection.HvlDisplayModeDefault;
import com.osreboot.ridhvl.template.HvlTemplate2DBasic;

public class Main extends HvlTemplate2DBasic {

	Player pTest;
	
	public static void main(String[] args){
		new Main();
	}
	
	public Main(){
		super(60, 1280, 720, "Mini Ludum Dare 60 - Celebrate Ludum Dare", new HvlDisplayModeDefault());//TODO icon and change title
	}

	@Override
	public void initialize() {
		pTest = new Player(1280 / 2, 720 / 2);
		TextureManager.initialize();
		SoundManager.initialize();
	}

	@Override
	public void update(float delta) {
		SoundManager.update();
		pTest.update(delta);
		pTest.draw(delta);
	}
}
