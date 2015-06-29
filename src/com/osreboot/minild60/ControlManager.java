package com.osreboot.minild60;

import org.lwjgl.input.Keyboard;

public class ControlManager {

	public enum Action{
		MOVEUP, MOVEDOWN, MOVELEFT, MOVERIGHT, PAUSE
	}

	public static boolean isActionTriggering(Action action){
		switch(action){
		case MOVEUP: return Keyboard.isKeyDown(Keyboard.KEY_W);
		case MOVEDOWN: return Keyboard.isKeyDown(Keyboard.KEY_S);
		case MOVELEFT: return Keyboard.isKeyDown(Keyboard.KEY_A);
		case MOVERIGHT: return Keyboard.isKeyDown(Keyboard.KEY_D);
		case PAUSE: return Keyboard.isKeyDown(Keyboard.KEY_ESCAPE);
		default: return false;
		}
	}

}
