package com.osreboot.minild60;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.SoundStore;

import com.osreboot.ridhvl.loader.HvlSoundLoader;

public class SoundManager {

	private static HvlSoundLoader menuLoader = new HvlSoundLoader(10);
	private static HvlSoundLoader songLoader = new HvlSoundLoader(10);
	private static HvlSoundLoader sfxLoader = new HvlSoundLoader(10);
	
	public enum SoundSeries{
		MENU,
		SONG,
		SFX
	}
	
	public static void initialize(){
		sfxLoader.loadResource("Record pick up");
		sfxLoader.loadResource("Character death");
		sfxLoader.loadResource("Bug death");
		songLoader.loadResource("JamStepFast");
	}
	
	public static void update(){
		if(OptionsConfig.volume == 0){
			if(getResource(SoundSeries.SONG, 0).isPlaying()) getResource(SoundSeries.SONG, 0).stop();
		}else{
			if(!getResource(SoundSeries.SONG, 0).isPlaying()) getResource(SoundSeries.SONG, 0).playAsSoundEffect(1, OptionsConfig.volume/10, false);
		}
		SoundStore.get().poll(0);
	}
	
	public static void reset(){
		if(getResource(SoundSeries.SONG, 0).isPlaying()) getResource(SoundSeries.SONG, 0).stop();
	}
	
	public static Audio getResource(SoundSeries seriesArg, int indexArg){
		switch(seriesArg){
		case MENU: return menuLoader.getResource(indexArg);
		case SONG: return songLoader.getResource(indexArg);
		case SFX: return sfxLoader.getResource(indexArg);
		default: return null;
		}
	}
	
}
