package com.osreboot.minild60;

import org.newdawn.slick.opengl.Texture;

import com.osreboot.ridhvl.loader.HvlTextureLoader;

public class TextureManager {

	private static HvlTextureLoader uiLoader = new HvlTextureLoader(20);
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
		uiLoader.loadResource("Achievement");
		uiLoader.loadResource("Bar2");
		uiLoader.loadResource("Slider2");
		uiLoader.loadResource("Select");
		uiLoader.loadResource("Checked");//10
		uiLoader.loadResource("Unchecked");
		uiLoader.loadResource("List");
		uiLoader.loadResource("ListChecked");
		uiLoader.loadResource("WubAllianceSplash");
		uiLoader.loadResource("WubAllianceSplash2");
		
		playLoader.loadResource("TestTilesheet");
		playLoader.loadResource("Enemy");
		playLoader.loadResource("Particle");
		playLoader.loadResource("Record");
	}
	
	public static Texture getResource(TextureSeries seriesArg, int indexArg){
		switch(seriesArg){
		case UI: return uiLoader.getResource(indexArg);
		case PLAY: return playLoader.getResource(indexArg);
		default: return null;
		}
	}
	
}
