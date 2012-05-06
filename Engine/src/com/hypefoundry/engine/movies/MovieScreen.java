/**
 * 
 */
package com.hypefoundry.engine.movies;


import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

import com.hypefoundry.engine.core.Input;
import com.hypefoundry.engine.game.InputHandler;
import com.hypefoundry.engine.game.Screen;
import com.hypefoundry.engine.game.ScreenFactory;
import com.hypefoundry.engine.impl.game.GLGame;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.EntityVisualFactory;
import com.hypefoundry.engine.renderer2D.Renderer2D;
import com.hypefoundry.engine.util.serialization.xml.XMLDataLoader;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.World;
import com.hypefoundry.engine.world.serialization.EntityFactory;

/**
 * The screen is responsible for playing back an in-game movie. 
 * 
 * @author Paksas
 */
public class MovieScreen extends Screen implements InputHandler, MovieEndListener
{
	enum State
	{
		Initialize,
		Draw,
		MoveToNextScreen,
		Finished
	}
	
	
	private State		m_currentState = State.Initialize;
	private Object		m_stateSynchObj = new Object();
	

	// movie details
	World				m_world;
	Renderer2D			m_renderer;
	float				m_posX, m_posY;
	
	// following screen factory
	ScreenFactory		m_nextScreenFactory;
	
	// cooldown timer that prevents you from clicking through the movie too quickly
	float				m_inputCooldownTimer = 1.0f;
	
	
	/**
	 * Constructor.
	 * 
	 * @param game
	 * @param movieAssetPath
	 * @param nextScreenFactory			a factory that will create and activate a next screen when the movie playback is over
	 */
	public MovieScreen( GLGame game, String	movieAssetPath, ScreenFactory nextScreenFactory ) 
	{
		super(game);
		
		final MovieEndListener listener = this;
		
		m_nextScreenFactory = nextScreenFactory;
		m_world = new World();
		m_renderer = new Renderer2D( game, 480, 800 );
		
		m_world.registerEntity( MovieEntity.class, new EntityFactory() { @Override public Entity create() { return new MovieEntity(); } } );
		m_renderer.register( MovieEntity.class, new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new MovieEntityVisual( m_resourceManager, parentEntity, listener ); } } );
		
		try 
		{
			InputStream worldFileStream = game.getFileIO().readAsset( movieAssetPath );
			m_world.load( XMLDataLoader.parse( worldFileStream, "Movie" ), m_resourceManager );
		} 
		catch ( IOException e ) 
		{
			Log.d( "Game", "Error while loading world" );
			throw new RuntimeException( e );
		}
		
		// register the updatables
		m_world.attachView( m_renderer );
		addUpdatable( m_world );
		
		// register input handler
		registerInputHandler( this );
	}


	@Override
	public void present( float deltaTime ) 
	{	
		State currState = null;
		synchronized( m_stateSynchObj )
		{
			currState = m_currentState;
		}
		
		switch( currState )
		{
			case Draw:
			{
				// decrease the cooldown timer value 
				m_inputCooldownTimer -= deltaTime;
				
				// draw the movie
				m_renderer.draw( m_running ? deltaTime : 0 );
				break;
			}
			
			case MoveToNextScreen:
			{
				proceedToNextScreen();
				break;
			}
		}		
	}
	

	@Override
	public void pause() 
	{
		synchronized( m_stateSynchObj )
		{
			m_currentState = State.Initialize;
			m_stateSynchObj.notifyAll();
		}
		
		m_resourceManager.releaseResources();
	}

	@Override
	public void resume() 
	{
		m_resourceManager.loadResources();
		
		synchronized( m_stateSynchObj )
		{
			m_currentState = State.Draw;
			m_stateSynchObj.notifyAll();
		}
	}

	@Override
	public void dispose() 
	{
	}
	
	@Override
	public boolean onBackPressed() 
	{
		synchronized( m_stateSynchObj )
		{
			m_currentState = State.MoveToNextScreen;
			m_stateSynchObj.notifyAll();
		}
		
		return true;
	}


	@Override
	public boolean handleInput( Input input, float deltaTime ) 
	{
		if ( input.isTouchDown(0) )
		{
			synchronized( m_stateSynchObj )
			{
				if ( m_inputCooldownTimer <= 0.0f )
				{
					m_currentState = State.MoveToNextScreen;
					m_stateSynchObj.notifyAll();
				}
			}
			return true;
		}
		else
		{
			return false;
		}		
	}
	
	/**
	 * A helper method that cleans up and moves to the next screen.
	 */
	private void proceedToNextScreen()
	{
		unregisterInputHandler( this );
				
		// the clip has completed - jump to the next screen
		Screen nextScreen = m_nextScreenFactory.createScreen();
		m_game.setScreen( nextScreen );
		
		synchronized( m_stateSynchObj )
		{
			m_currentState = State.Finished;
			m_stateSynchObj.notifyAll();
		}
	}


	@Override
	public void onMovieEnded() 
	{
		synchronized( m_stateSynchObj )
		{
			m_currentState = State.MoveToNextScreen;
			m_stateSynchObj.notifyAll();
		}
		
	}
}
