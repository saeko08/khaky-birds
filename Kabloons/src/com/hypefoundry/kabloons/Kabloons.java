package com.hypefoundry.kabloons;


import com.hypefoundry.engine.game.Screen;
import com.hypefoundry.engine.impl.game.GLGame;
import com.hypefoundry.kabloons.loadingScreen.GenericLoadingScreenFactory;


public class Kabloons extends GLGame 
{	
	@Override
	public void onResume()
	{
		super.onResume();
		
		// load and setup the loading screen
		setLoadingScreen( new GenericLoadingScreenFactory() );
	}
	
	@Override
	public Screen getStartScreen()
	{
		return new MainMenu( this, MainMenu.MenuScreen.MS_Main );
	}
}

