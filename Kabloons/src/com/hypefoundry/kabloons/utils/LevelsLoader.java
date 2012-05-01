package com.hypefoundry.kabloons.utils;

import java.io.InputStream;



import com.hypefoundry.engine.game.Screen;
import com.hypefoundry.engine.game.ScreenFactory;
import com.hypefoundry.engine.impl.game.GLGame;
import com.hypefoundry.engine.movies.MovieScreen;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.util.serialization.xml.XMLDataLoader;
import com.hypefoundry.kabloons.GameScreen;


public class LevelsLoader 
{
	public static int							m_levelsCount = 6;
	
	
	static class GameScreenFactory implements ScreenFactory
	{
		int			m_levelIdx;
		GLGame		m_game;
		
		
		/**
		 * Constructor.
		 * 
		 * @param game
		 * @param levelIdx
		 */
		GameScreenFactory( GLGame game, int levelIdx )
		{
			m_game = game;
			m_levelIdx = levelIdx;
		}
		
		@Override
		public Screen createScreen() 
		{
			return new GameScreen( m_game, m_levelIdx );
		}
	}
	
	/**
	 * Creates a path to the specified game level.
	 * 
	 * @param levelIdx
	 * @return
	 */
	public static String getLevelPath( int levelIdx )
	{
		// assert the input data
		levelIdx = validateLevelIdx( levelIdx );
		
		// build the path
		StringBuilder levelPath = new StringBuilder();
		levelPath.append( "levels/world_" );
		if ( levelIdx < 10 )
		{
			levelPath.append( "0" );
		}
		levelPath.append( levelIdx );

		levelPath.append( ".xml" );
		
		return levelPath.toString();
	}
	
	
	/**
	 * Loads a level.
	 * 
	 * @param game
	 * @param levelIdx
	 * @param prevLevelIdx
	 */
	public static void loadLevel( GLGame game, int levelIdx, int prevLevelIdx )
	{
		levelIdx = validateLevelIdx( levelIdx );
		
		String cutscenePath = "";
		if ( prevLevelIdx != levelIdx )
		{
			// we are only interested in cutscenes if we're loading a new level, and not reloading one
			cutscenePath = getCutscenePath( game, levelIdx );
		}
		
		Screen nextScreen = null;
		if ( cutscenePath.length() > 0 )
		{
			// play the cutscene
			nextScreen = new MovieScreen( game, cutscenePath, new GameScreenFactory( game, levelIdx ) );
		}
		else
		{
			nextScreen = new GameScreen( game, levelIdx );
		}
		
		game.setScreen( nextScreen );
	}
	
	/**
	 * A helper method that loads a path to a cutscene from the specified level definition file.
	 * 
	 * @param game
	 * @param levelIdx
	 * @return
	 */
	private static String getCutscenePath( GLGame game, int levelIdx )
	{
		// check if the level has a cutscene defined and play it first
		String levelPath = getLevelPath( levelIdx );
		String cutscenePath = "";
		try
		{
			InputStream worldFileStream = game.getFileIO().readAsset( levelPath );
			DataLoader worldNode = XMLDataLoader.parse( worldFileStream, "World" );
			
			if ( worldNode != null )
			{
				DataLoader cutsceneNode = worldNode.getChild( "Cutscene" );
				if ( cutsceneNode != null )
				{
					cutscenePath = cutsceneNode.getStringValue( "path" );
				}
			}
		} 
		catch ( Exception e ) 
		{
		}
		
		return cutscenePath;
	}
	
	/**
	 * Validates and returns a corrected level index.
	 * @param idx
	 * @return
	 */
	private static int validateLevelIdx( int idx )
	{
		// assert the input data
		if ( idx < 1 )
		{
			idx = 1;
		}
		else if ( idx > m_levelsCount )
		{
			idx = m_levelsCount;
		}
		
		return idx;
	}

}
