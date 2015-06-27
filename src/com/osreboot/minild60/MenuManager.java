package com.osreboot.minild60;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.minild60.TextureManager.TextureSeries;
import com.osreboot.ridhvl.HvlFontUtil;
import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.menu.component.HvlArrangerBox;
import com.osreboot.ridhvl.menu.component.HvlArrangerBox.ArrangementStyle;
import com.osreboot.ridhvl.menu.component.HvlLabel;
import com.osreboot.ridhvl.menu.component.collection.HvlTextButton;
import com.osreboot.ridhvl.menu.component.collection.HvlTextureDrawable;
import com.osreboot.ridhvl.painter.painter2d.HvlFontPainter2D;
import com.osreboot.ridhvl.painter.painter2d.HvlPainter2D;

public class MenuManager {

	private static HvlFontPainter2D font;
	
	private static HvlMenu main, game, achievements;
	private static HvlArrangerBox mainArranger, achievementArranger;
	private static HvlLabel mainTitle, achievementTitle;
	private static HvlTextButton mainPlay, mainAchievements, mainTutorial, mainOptions, mainCredits,
	achievementBack;
	
	public static void initialize(){
		font = new HvlFontPainter2D(TextureManager.getResource(TextureSeries.UI, 2), HvlFontUtil.DEFAULT, 2048, 2048, 192, 256, 10);
		
		
		/*MAIN MENU*/
		main = new HvlMenu(){
			@Override
			public void draw(float delta){
				HvlPainter2D.hvlDrawQuad(0, 0, Display.getWidth(), Display.getHeight(), TextureManager.getResource(TextureSeries.UI, 0));
				super.draw(delta);
			}
		};
		
		mainArranger = new HvlArrangerBox(0, 0, Display.getWidth(), Display.getHeight(), ArrangementStyle.VERTICAL);
		mainArranger.setAlign(0.5f);
		main.add(mainArranger);
		
		mainTitle = new HvlLabel(0, 0, font, "name goes here", Color.red, 0.25f);
		mainArranger.add(mainTitle);
		
		mainPlay = new HvlTextButton(0, 0, Display.getWidth()/4, Display.getHeight()/8, new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 0)), new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 1)), font, "play"){
			@Override
			public void onTriggered(){
				HvlMenu.setCurrent(game);
				Game.initialize();
			}
		};
		mainPlay.setTextScale(0.4f);
		mainArranger.add(mainPlay);
		
		mainAchievements = new HvlTextButton(0, 0, Display.getWidth()/4, Display.getHeight()/8, new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 0)), new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 1)), font, "achievements"){
			@Override
			public void onTriggered(){
				HvlMenu.setCurrent(achievements);
			}
		};
		mainAchievements.setTextScale(0.4f);
		mainArranger.add(mainAchievements);
		/*END MAIN MENU*/
		
		
		/*ACHIEVEMENTS*/
		achievements = new HvlMenu(){
			@Override
			public void draw(float delta){
				HvlPainter2D.hvlDrawQuad(0, 0, Display.getWidth(), Display.getHeight(), TextureManager.getResource(TextureSeries.UI, 0));
				super.draw(delta);
			}
		};
		
		achievementArranger = new HvlArrangerBox(0, 0, Display.getWidth(), Display.getHeight(), ArrangementStyle.VERTICAL);
		achievementArranger.setAlign(0.5f);
		achievements.add(achievementArranger);
		
		achievementTitle = new HvlLabel(0, 0, font, "achievements", Color.red, 0.25f);
		achievementArranger.add(achievementTitle);
		
		achievementBack = new HvlTextButton(0, 0, Display.getWidth()/4, Display.getHeight()/8, new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 0)), new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 1)), font, "back"){
			@Override
			public void onTriggered(){
				HvlMenu.setCurrent(main);
			}
		};
		achievementBack.setTextScale(0.4f);
		achievementArranger.add(achievementBack);
		/*END ACHIEVEMENTS*/
		
		
		/*IN-GAME*/
		game = new HvlMenu(){
			@Override
			public void draw(float delta){
				Game.update(delta);
			}
		};
		/*END IN-GAME*/
		
		HvlMenu.setCurrent(main);
	}
	
}
