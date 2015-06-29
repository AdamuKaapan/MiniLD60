package com.osreboot.minild60;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.minild60.ControlManager.Action;
import com.osreboot.minild60.TextureManager.TextureSeries;
import com.osreboot.ridhvl.HvlFontUtil;
import com.osreboot.ridhvl.HvlTextureUtil;
import com.osreboot.ridhvl.config.HvlConfigUtil;
import com.osreboot.ridhvl.menu.HvlComponent;
import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.menu.component.HvlArrangerBox;
import com.osreboot.ridhvl.menu.component.HvlArrangerBox.ArrangementStyle;
import com.osreboot.ridhvl.menu.component.HvlButton;
import com.osreboot.ridhvl.menu.component.HvlLabel;
import com.osreboot.ridhvl.menu.component.HvlListBox;
import com.osreboot.ridhvl.menu.component.HvlSlider;
import com.osreboot.ridhvl.menu.component.HvlSpacer;
import com.osreboot.ridhvl.menu.component.HvlSlider.SliderDirection;
import com.osreboot.ridhvl.menu.component.collection.HvlTextButton;
import com.osreboot.ridhvl.menu.component.collection.HvlTextureDrawable;
import com.osreboot.ridhvl.menu.component.collection.HvlTiledRectDrawable;
import com.osreboot.ridhvl.painter.painter2d.HvlFontPainter2D;
import com.osreboot.ridhvl.painter.painter2d.HvlPainter2D;
import com.osreboot.ridhvl.painter.painter2d.HvlTiledRect;

public class MenuManager {

	private static HvlFontPainter2D font;
	
	private static HvlMenu main, levels, game, achievements, options, paused;
	private static HvlArrangerBox mainArranger, levelArranger, achievementArranger, optionsArranger, pausedArranger;
	private static HvlLabel mainTitle, achievementTitle, levelTitle, pausedTitle, 
	optionsTitle, optionsVolumeIndicator;
	private static HvlTextButton mainPlay, mainAchievements, mainOptions, mainQuit,
	levelPlay,
	achievementBack,
	optionsSave, optionsBack,
	pausedResume, pausedQuit;
	private static HvlSlider optionsVolume;
	private static HvlListBox levelList;
	
	public static void initialize(){
		font = new HvlFontPainter2D(TextureManager.getResource(TextureSeries.UI, 2), HvlFontUtil.DEFAULT, 2048, 2048, 192, 256, 10);
		
		
		/*MAIN MENU*/
		main = new HvlMenu(){
			@Override
			public void draw(float delta){
				drawBackground();
				super.draw(delta);
			}
		};
		
		mainArranger = new HvlArrangerBox(0, 0, Display.getWidth(), Display.getHeight(), ArrangementStyle.VERTICAL);
		mainArranger.setAlign(0.5f);
		main.add(mainArranger);
		
		mainTitle = new HvlLabel(0, 0, font, "name goes here", Color.red, 0.25f);
		mainArranger.add(mainTitle);
		
		mainArranger.add(getBlankSpace());
		mainPlay = new HvlTextButton(0, 0, Display.getWidth()/4*3, Display.getHeight()/8, getButton(), new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 1)), font, "play"){
			@Override
			public void draw(float delta){
				drawEqualizerBar(this);
				super.draw(delta);
			}
			@Override
			public void onTriggered(){
				HvlMenu.setCurrent(levels);
			}
		};
		mainPlay.setTextScale(0.3f);
		mainArranger.add(mainPlay);
		
		mainArranger.add(getBlankSpace());
		mainAchievements = new HvlTextButton(0, 0, Display.getWidth()/4*3, Display.getHeight()/8, getButton(), new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 1)), font, "achievements"){
			@Override
			public void draw(float delta){
				drawEqualizerBar(this);
				super.draw(delta);
			}
			@Override
			public void onTriggered(){
				HvlMenu.setCurrent(achievements);
			}
		};
		mainAchievements.setTextScale(0.3f);
		mainArranger.add(mainAchievements);
		
		mainArranger.add(getBlankSpace());
		mainOptions = new HvlTextButton(0, 0, Display.getWidth()/4*3, Display.getHeight()/8, getButton(), new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 1)), font, "options"){
			@Override
			public void draw(float delta){
				drawEqualizerBar(this);
				super.draw(delta);
			}
			@Override
			public void onTriggered(){
				loadOptions();
				HvlMenu.setCurrent(options);
			}
		};
		mainOptions.setTextScale(0.3f);
		mainArranger.add(mainOptions);
		
		mainArranger.add(getBlankSpace());
		mainQuit = new HvlTextButton(0, 0, Display.getWidth()/4*3, Display.getHeight()/8, getButton(), new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 1)), font, "quit"){
			@Override
			public void draw(float delta){
				drawEqualizerBar(this);
				super.draw(delta);
			}
			@Override
			public void onTriggered(){
				System.exit(0);
			}
		};
		mainQuit.setTextScale(0.3f);
		mainArranger.add(mainQuit);
		/*END MAIN MENU*/
		
		
		/*LEVEL SELECT*/
		levels = new HvlMenu(){
			@Override
			public void draw(float delta){
				drawBackground();
				super.draw(delta);
			}
		};
		
		levelArranger = new HvlArrangerBox(0, 0, Display.getWidth(), Display.getHeight(), ArrangementStyle.VERTICAL);
		levelArranger.setAlign(0.5f);
		levels.add(levelArranger);
		
		levelTitle = new HvlLabel(0, 0, font, "level select", Color.red, 0.25f);
		levelArranger.add(levelTitle);
		
		levelArranger.add(getBlankSpace());
		levelList = new HvlListBox(0, 0, 512, Display.getHeight()/8*5, 
				new HvlSlider(0, 0, Display.getWidth()/8, Display.getHeight()/8*5, SliderDirection.VERTICAL, 32, 32, 0, new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 3)), getButton()), 
				new HvlButton(0, 0, 0, 0, new HvlTextureDrawable(HvlTextureUtil.getColoredRect(1, 1, Color.transparent)),  new HvlTextureDrawable(HvlTextureUtil.getColoredRect(1, 1, Color.transparent))),
				new HvlButton(0, 0, 0, 0, new HvlTextureDrawable(HvlTextureUtil.getColoredRect(1, 1, Color.transparent)),  new HvlTextureDrawable(HvlTextureUtil.getColoredRect(1, 1, Color.transparent))),
				font, new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 3)), new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 2)), 256, 2);
		for(Level level : Level.levels){
			levelList.addItem(Level.levels.indexOf(level));
		}
		levelArranger.add(levelList);
		
		levelArranger.add(getBlankSpace());
		levelPlay = new HvlTextButton(0, 0, Display.getWidth()/4*3, Display.getHeight()/8, getButton(), new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 1)), font, "play"){
			@Override
			public void draw(float delta){
				drawEqualizerBar(this);
				super.draw(delta);
			}
			@Override
			public void onTriggered(){
				Game.currentLevel = levelList.getSelectedIndex() == -1 ? Level.levels.get(0) : Level.levels.get(levelList.getSelectedIndex());
				HvlMenu.setCurrent(game);
				Game.initialize();
			}
		};
		levelPlay.setTextScale(0.3f);
		levelArranger.add(levelPlay);
		/*END LEVEL SELECT*/
		
		
		/*ACHIEVEMENTS*/
		achievements = new HvlMenu(){
			@Override
			public void draw(float delta){
				drawBackground();
				super.draw(delta);
			}
		};
		
		achievementArranger = new HvlArrangerBox(0, 0, Display.getWidth(), Display.getHeight(), ArrangementStyle.VERTICAL);
		achievementArranger.setAlign(0.5f);
		achievements.add(achievementArranger);
		
		achievementTitle = new HvlLabel(0, 0, font, "achievements", Color.red, 0.25f);
		achievementArranger.add(achievementTitle);
		
		achievementArranger.add(getBlankSpace());
		achievementBack = new HvlTextButton(0, 0, Display.getWidth()/4*3, Display.getHeight()/8, getButton(), new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 1)), font, "back"){
			@Override
			public void draw(float delta){
				drawEqualizerBar(this);
				super.draw(delta);
			}
			@Override
			public void onTriggered(){
				HvlMenu.setCurrent(main);
			}
		};
		achievementBack.setTextScale(0.3f);
		achievementArranger.add(achievementBack);
		/*END ACHIEVEMENTS*/
		
		
		/*OPTIONS*/
		options = new HvlMenu(){
			@Override
			public void draw(float delta){
				drawBackground();
				super.draw(delta);
			}
		};
		
		optionsArranger = new HvlArrangerBox(0, 0, Display.getWidth(), Display.getHeight(), ArrangementStyle.VERTICAL);
		optionsArranger.setAlign(0.5f);
		options.add(optionsArranger);
		
		optionsTitle = new HvlLabel(0, 0, font, "options", Color.red, 0.25f);
		optionsArranger.add(optionsTitle);
		
		optionsArranger.add(getBlankSpace());
		optionsVolumeIndicator = new HvlLabel(0, 0, font, "volume ", Color.red, 0.25f){
			@Override
			public void draw(float delta){
				setText("volume " + (int)(optionsVolume.getValue()*100));
				super.draw(delta);
			}
		};
		optionsArranger.add(optionsVolumeIndicator);
		
		optionsVolume = new HvlSlider(0, 0, Display.getWidth()/2, Display.getHeight()/8, SliderDirection.HORIZONTAL, 32, 32, 1, new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 3)), getButton());
		optionsVolume.setSnapInterval(0.01f);
		optionsVolume.setValue(OptionsConfig.volume);
		optionsArranger.add(optionsVolume);
		
		optionsArranger.add(getBlankSpace());
		optionsSave = new HvlTextButton(0, 0, Display.getWidth()/4*3, Display.getHeight()/8, getButton(), new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 1)), font, "save"){
			@Override
			public void draw(float delta){
				drawEqualizerBar(this);
				super.draw(delta);
			}
			@Override
			public void onTriggered(){
				saveOptions();
			}
		};
		optionsSave.setTextScale(0.3f);
		optionsArranger.add(optionsSave);
		
		optionsArranger.add(getBlankSpace());
		optionsBack = new HvlTextButton(0, 0, Display.getWidth()/4*3, Display.getHeight()/8, getButton(), new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 1)), font, "back"){
			@Override
			public void draw(float delta){
				drawEqualizerBar(this);
				super.draw(delta);
			}
			@Override
			public void onTriggered(){
				HvlConfigUtil.loadStaticConfig(OptionsConfig.class, "res\\options.txt");
				HvlMenu.setCurrent(main);
			}
		};
		optionsBack.setTextScale(0.3f);
		optionsArranger.add(optionsBack);
		/*END OPTIONS*/
		
		
		/*PAUSED*/
		paused = new HvlMenu(){
			@Override
			public void draw(float delta){
				drawBackground();
				super.draw(delta);
			}
		};
		
		pausedArranger = new HvlArrangerBox(0, 0, Display.getWidth(), Display.getHeight(), ArrangementStyle.VERTICAL);
		pausedArranger.setAlign(0.5f);
		paused.add(pausedArranger);
		
		pausedTitle = new HvlLabel(0, 0, font, "paused", Color.red, 0.25f);
		pausedArranger.add(pausedTitle);
		
		pausedArranger.add(getBlankSpace());
		pausedResume = new HvlTextButton(0, 0, Display.getWidth()/4*3, Display.getHeight()/8, getButton(), new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 1)), font, "resume"){
			@Override
			public void draw(float delta){
				drawEqualizerBar(this);
				super.draw(delta);
			}
			@Override
			public void onTriggered(){
				HvlMenu.setCurrent(game);
			}
		};
		pausedResume.setTextScale(0.3f);
		pausedArranger.add(pausedResume);
		
		pausedArranger.add(getBlankSpace());
		pausedQuit = new HvlTextButton(0, 0, Display.getWidth()/4*3, Display.getHeight()/8, getButton(), new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 1)), font, "quit"){
			@Override
			public void draw(float delta){
				drawEqualizerBar(this);
				super.draw(delta);
			}
			@Override
			public void onTriggered(){
				HvlMenu.setCurrent(main);
			}
		};
		pausedQuit.setTextScale(0.3f);
		pausedArranger.add(pausedQuit);
		/*END PAUSED*/
		
		
		/*IN-GAME*/
		game = new HvlMenu(){
			@Override
			public void draw(float delta){
				Game.update(delta);
				if(ControlManager.isActionTriggering(Action.PAUSE)) HvlMenu.setCurrent(paused);
			}
		};
		/*END IN-GAME*/
		
		HvlMenu.setCurrent(main);
	}
	
	public static HvlSpacer getBlankSpace(){
		return new HvlSpacer(0, 0, Display.getWidth(), Display.getHeight()/32);
	}
	
	public static HvlTiledRectDrawable getButton(){
		return new HvlTiledRectDrawable(new HvlTiledRect(TextureManager.getResource(TextureSeries.UI, 3), 0.45f, 0.55f, 0.45f, 0.55f, 0, 0, 0, 0, 64, 64));
	}
	
	private static float total = 0;
	public static void update(float delta){
		total += delta;
	}
	
	public static void drawBackground(){
		HvlPainter2D.hvlDrawQuad(0, 0, Display.getWidth(), Display.getHeight(), TextureManager.getResource(TextureSeries.UI, 0), Color.darkGray);
	}
	
	public static void drawEqualizerBar(HvlComponent component){
		HvlPainter2D.hvlDrawQuad(component.getX(), component.getY(), component.getWidth(), component.getHeight(), TextureManager.getResource(TextureSeries.UI, 0), new Color(0.1f, 0.1f, 0));
		HvlPainter2D.hvlDrawQuad(component.getX(), component.getY(), component.getWidth()*Math.max((float)Math.sin((total + (component.getY()/(float)Display.getHeight()))*2), 0), component.getHeight(), TextureManager.getResource(TextureSeries.UI, 0), Color.yellow);
	}
	
	private static void loadOptions(){
		HvlConfigUtil.loadStaticConfig(OptionsConfig.class, "res\\options.txt");
		optionsVolume.setValue(OptionsConfig.volume);
	}
	
	private static void saveOptions(){
		OptionsConfig.volume = optionsVolume.getValue();
		HvlConfigUtil.saveStaticConfig(OptionsConfig.class, "res\\options.txt");
	}
	
}
