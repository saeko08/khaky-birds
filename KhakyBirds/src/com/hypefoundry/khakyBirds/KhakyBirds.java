package com.hypefoundry.khakyBirds;


import com.hypefoundry.engine.game.Screen;
import com.hypefoundry.engine.impl.game.GLGame;


public class KhakyBirds extends GLGame 
{
	@Override
	public Screen getStartScreen()
	{
		return new MenuScreen( this );
	}
}

