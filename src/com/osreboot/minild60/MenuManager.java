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
	
	private static HvlMenu main;
	private static HvlArrangerBox mainArranger;
	private static HvlLabel mainTitle;
	private static HvlTextButton mainPlay, mainTutorial, mainOptions, mainCredits;
	
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
		mainTitle = new HvlLabel(0, 0, font, "welcome to death", Color.red, 0.25f);
		mainArranger.add(mainTitle);
		mainPlay = new HvlTextButton(0, 0, Display.getWidth()/4, Display.getHeight()/8, new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 0)), new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 1)), font, "play");
		mainPlay.setTextScale(0.4f);
		mainPlay.setxAlign(0.5f);
		mainPlay.setyAlign(0.5f);
		mainArranger.add(mainPlay);
		/*END MAIN MENU*/
		
		HvlMenu.setCurrent(main);
	}
	
}
