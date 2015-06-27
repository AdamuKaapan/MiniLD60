package com.osreboot.minild60;

import org.newdawn.slick.opengl.Texture;

import com.osreboot.ridhvl.loader.HvlTextureLoader;

public class TextureManager {

	private static HvlTextureLoader uiLoader = new HvlTextureLoader(10);
	
	public enum TextureSeries{
		UI
	}
	
	public static void initialize(){
		uiLoader.loadResource("White");
		uiLoader.loadResource("TempArrow");
	}
	
	public static Texture getResource(TextureSeries seriesArg, int indexArg){
		switch(seriesArg){
		case UI: return uiLoader.getResource(indexArg);
		default: return null;
		}
	}
	
}
