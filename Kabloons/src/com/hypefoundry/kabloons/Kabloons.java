package com.hypefoundry.kabloons;


import com.hypefoundry.engine.game.Screen;
import com.hypefoundry.engine.game.ScreenFactory;
import com.hypefoundry.engine.impl.game.GLGame;
import com.hypefoundry.engine.movies.MovieScreen;
import com.hypefoundry.kabloons.loadingScreen.GenericLoadingScreenFactory;


public class Kabloons extends GLGame 
{	
	class MainMenuFactory implements ScreenFactory
	{
		GLGame		m_game;
		
		/**
		 * Constructor.
		 * 
		 * @param game
		 */
		MainMenuFactory( GLGame game )
		{
			m_game = game;
		}
		
		@Override
		public Screen createScreen() 
		{
			return new MainMenu( m_game, MainMenu.MenuScreen.MS_Main );
		}
	}
	
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
		return new MovieScreen( this, "movies/logo.xml", new MainMenuFactory( this ) );
	}
}

