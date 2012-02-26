package com.hypefoundry.kabloons;


import com.hypefoundry.engine.game.Screen;
import com.hypefoundry.engine.impl.game.GLGame;


public class Kabloons extends GLGame 
{
	@Override
	public Screen getStartScreen()
	{
		return new MainMenu( this, MainMenu.MenuScreen.MS_Main );
	}
}

