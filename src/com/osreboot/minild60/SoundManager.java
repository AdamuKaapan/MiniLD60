package com.osreboot.minild60;

import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.Texture;

import com.osreboot.ridhvl.loader.HvlTextureLoader;

public class SoundManager {

	private static HvlTextureLoader menuLoader = new HvlTextureLoader(10);
	
	public enum TextureSeries{
		MENU
	}
	
	public static void initialize(){
		
	}
	
	public static void update(){
		SoundStore.get().poll(0);
	}
	
	public static Texture getResource(TextureSeries seriesArg, int indexArg){
		switch(seriesArg){
		case MENU: return menuLoader.getResource(indexArg);
		default: return null;
		}
	}
	
}
