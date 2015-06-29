package com.osreboot.minild60;

import org.newdawn.slick.opengl.Texture;

import com.osreboot.ridhvl.loader.HvlTextureLoader;

public class TextureManager {

	private static HvlTextureLoader uiLoader = new HvlTextureLoader(10);
	private static HvlTextureLoader playLoader = new HvlTextureLoader(10);
	
	public enum TextureSeries{
		UI,
		PLAY
	}
	
	public static void initialize(){
		uiLoader.loadResource("White");
		uiLoader.loadResource("TempArrow");
		uiLoader.loadResource("Font");
		uiLoader.loadResource("Equalizer");
		uiLoader.loadResource("Slider");
		uiLoader.loadResource("Bar");
		
		playLoader.loadResource("TestTilesheet");
		playLoader.loadResource("Enemy");
	}
	
	public static Texture getResource(TextureSeries seriesArg, int indexArg){
		switch(seriesArg){
		case UI: return uiLoader.getResource(indexArg);
		case PLAY: return playLoader.getResource(indexArg);
		default: return null;
		}
	}
	
}
