/**
 * 
 */
package com.hypefoundry.engine.impl.game;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.game.Screen;
import com.hypefoundry.engine.impl.game.GLGame;
import com.hypefoundry.engine.impl.game.GameOperation;
import com.hypefoundry.engine.renderer2D.Renderer2D;
import com.hypefoundry.engine.world.World;


/**
 * @author Paksas
 *
 */
class ScreenChangeTransaction implements GameOperation
{
	enum State
	{
		FadeOut,
		LoadNewScreen,
		WaitUntilScreenLoaded,
		ScreenLoaded,
		FadeIn,
	}
	
	Screen					m_newScreen				= null;
	State					m_state					= State.FadeOut;
	Object					m_stateChanged			= new Object();
	
	ResourceManager			m_resMgr				= null;
	Renderer2D				m_worldRenderer			= null;
	World					m_world					= new World();
	
	LoadingScreenFactory	m_loadingScreenFactory	= new NullLoadingScreenFactory();
	LoadingScreen			m_loadingScreen			= new LoadingScreen();
	
	// ------------------------------------------------------------------------
	// Level loading thread
	// ------------------------------------------------------------------------
	
	class LevelLoaderThread extends Thread
	{
		private GLGame		m_game;
		
		/**
		 * Constructor.
		 * @param game
		 */
		LevelLoaderThread( GLGame game )
		{
			m_game = game;
		}
		
		@Override
		public void run() 
		{
			// start up the new screen
			if ( m_newScreen != null )
			{
				m_newScreen.resume();
				m_newScreen.update(0);
			}
			
			// memorize the new screen's instance	
			m_game.m_screen = m_newScreen;
			m_newScreen = null;
			
			// change the state
			synchronized( m_stateChanged )
			{
				m_state = ScreenChangeTransaction.State.ScreenLoaded;
				m_stateChanged.notifyAll();
			}
        }
	}
	
	// ------------------------------------------------------------------------
	
	/**
	 * Constructor.
	 * 
	 * @param game
	 * @param loadingScreenFactory
	 */
	public ScreenChangeTransaction( GLGame game )
	{
		m_resMgr = new ResourceManager( game );
			
		m_world.setSize( 4.8f, 8.0f ); // TODO: screen size configuration
	}
	
	/**
	 * Sets a new loading screen factory.
	 * 
	 * @param loadingScreenFactory
	 */
	public void setLoadingScreen( LoadingScreenFactory loadingScreenFactory )
	{
		if ( loadingScreenFactory == null )
		{
			return;
		}
		
		m_loadingScreenFactory = loadingScreenFactory;
		m_loadingScreenFactory.initialize( m_resMgr );
		
		if ( m_worldRenderer != null )
		{
			m_worldRenderer.register( LoadingScreen.class, m_loadingScreenFactory );
		}
	}
	
	
	
	/**
	 * Initializes the transaction.
	 * 
	 * @param game
	 * @param newScreen
	 */
	public void initialize( GLGame game, Screen newScreen )
	{
		m_newScreen = newScreen;

		// clear the input
		game.m_input.clear();
		
		m_world.addEntity( m_loadingScreen );
	
		synchronized( m_stateChanged )
		{
			m_state	= State.FadeOut;
			m_stateChanged.notifyAll();
		}
	}
	
	@Override
	public void update( float deltaTime, GLGame game )
	{	
		State currState = null;
		synchronized( m_stateChanged )
		{
			currState = m_state;
		}
		
		// by default disable additive rendering - enable it only when we're rendering the background scene
		m_worldRenderer.setAdditiveMode( false );
		
		switch( currState )
		{		
			case FadeOut:
			{
				// keep running the game loop for the fading
				simulateGameLoop( deltaTime, game );
				
				if ( m_loadingScreen.hasFadedOut() )
				{
					synchronized( m_stateChanged )
					{
						m_state = State.LoadNewScreen;
						m_stateChanged.notifyAll();
					}
				}
				break;
			}
			
			case LoadNewScreen:
			{
				// close the current screen
				Screen oldScreen = game.getCurrentScreen();
				if ( oldScreen != null )
				{
					oldScreen.pause();
					oldScreen.dispose();
				}
				
				synchronized( m_stateChanged )
				{
					m_state = State.WaitUntilScreenLoaded;
					m_stateChanged.notifyAll();
				}
				
				// start the level loading thread
				new LevelLoaderThread( game ).start();
				
				break;
			}
			
			case ScreenLoaded:
			{				
				// go to the next stage
				m_loadingScreen.startFadeIn();
				
				synchronized( m_stateChanged )
				{
					m_state = State.FadeIn;
					m_stateChanged.notifyAll();
				}
				break;
			}
			
			case FadeIn:
			{
				// keep running the game loop for the fading
				simulateGameLoop( deltaTime, game );
								
				// once everything is done - switch to the game loop operation
				if ( m_loadingScreen.hasFadedIn() )
				{
					game.switchToGameLoop();
					
					// and clean up
					m_world.removeEntity( m_loadingScreen );
					m_resMgr.clearResources();
				}
				break;
			}
		}
		
		// TODO: find out what's causeing the screen to turn blue when we're rendeirng the background scene
		
		// keep on drawing the overlay all the time
		m_world.update( deltaTime );
		m_worldRenderer.draw( deltaTime );
	}
	
	/**
	 * Simulates the game loop operation, without certain features - i.e. input is not updated. 
	 * 
	 * @param deltaTime
	 * @param game
	 */
	private void simulateGameLoop( float deltaTime, GLGame game )
	{
		m_worldRenderer.setAdditiveMode( true );
		game.m_screen.update( deltaTime );
		game.m_screen.present( deltaTime );
	}
	
	/**
	 * Call this when the new rendering surface is created
	 * 
	 * @param game
	 */
	void onSurfaceCreated( GLGame game )
	{
		if ( m_worldRenderer != null )
		{
			m_world.detachView( m_worldRenderer );
		}
		
		m_worldRenderer = new Renderer2D( game );
		m_worldRenderer.register( LoadingScreen.class, m_loadingScreenFactory );
		m_world.attachView( m_worldRenderer );
		
		m_resMgr.loadResources();
	}
	
	/**
	 * Releases the resources used by the operation.
	 */
	void onPause()
	{
		m_resMgr.releaseResources();
		
		m_world.detachView( m_worldRenderer );
		m_worldRenderer = null;
	}
}
