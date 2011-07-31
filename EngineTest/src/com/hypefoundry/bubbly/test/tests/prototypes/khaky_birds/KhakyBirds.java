package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds;


import com.hypefoundry.engine.game.Screen;
import com.hypefoundry.engine.impl.game.GLGame;


public class KhakyBirds extends GLGame 
{
	@Override
	public Screen getStartScreen()
	{
		return new GameScreen( this );
	}
}

