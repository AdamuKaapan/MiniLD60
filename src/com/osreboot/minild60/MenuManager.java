package com.osreboot.minild60;

import org.lwjgl.opengl.Display;

import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.menu.component.HvlArrangerBox;
import com.osreboot.ridhvl.menu.component.HvlArrangerBox.ArrangementStyle;
import com.osreboot.ridhvl.menu.component.collection.HvlTextButton;

public class MenuManager {

	private static HvlMenu main;
	private static HvlArrangerBox mainArranger;
	private static HvlTextButton mainPlay, mainTutorial, mainOptions, mainCredits;
	
	public static void initialize(){
		main = new HvlMenu();
		mainArranger = new HvlArrangerBox(0, 0, Display.getWidth(), Display.getHeight(), ArrangementStyle.VERTICAL);
		main.add(mainArranger);
		//mainPlay = new HvlTextButton(0, 0, Display.getWidth()/4, Display.getHeight()/16, new HvlTextureDrawable(null), onArg, fontArg, textArg)
	}
	
}
