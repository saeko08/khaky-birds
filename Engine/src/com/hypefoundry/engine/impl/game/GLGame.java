/**
 * 
 */
package com.hypefoundry.engine.impl.game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

import com.hypefoundry.engine.core.Audio;
import com.hypefoundry.engine.core.FileIO;
import com.hypefoundry.engine.core.GLGraphics;
import com.hypefoundry.engine.core.Input;
import com.hypefoundry.engine.game.Game;
import com.hypefoundry.engine.game.Screen;
import com.hypefoundry.engine.impl.core.AndroidAudio;
import com.hypefoundry.engine.impl.core.AndroidFileIO;
import com.hypefoundry.engine.impl.core.AndroidInput;


/**
 * A game that uses openGL renderer.
 * 
 * @author paksas
 */
public abstract class GLGame extends Activity implements Game, Renderer 
{
	enum GLGameState 
	{
		Initialized,
		Running,
		Paused,
		Finished,
		Idle
	}
	
	private GLSurfaceView 				m_glView;
	private GLGraphics 					m_graphics;
	private Audio 						m_audio;
	AndroidInput 						m_input;
	private FileIO 						m_fileIO;
	Screen 								m_screen;
	private WakeLock 					m_wakeLock;
	long 								m_startTime = System.nanoTime();
	
	private GLGameState 				m_state = GLGameState.Initialized;
	private Object 						m_stateChanged = new Object();
	
	private static GLGame				s_theInstance = null;
	
	// operations
	GameLoopOperation					m_gameLoopOperation;
	private ScreenChangeTransaction		m_screenChangeTransaction;
	private GameOperation				m_operation;
	
	
	/**
	 * Constructor.
	 */
	public GLGame() 
	{
		s_theInstance = this;
	}
	
	/**
	 * Returns the singleton instance of the game. 
	 * 
	 * @return
	 */
	public static GLGame getInstance()
	{
		return s_theInstance;
	}

	@Override
	public void onCreate( Bundle savedInstanceState ) 
	{
		super.onCreate( savedInstanceState );
		
		// go fullscreen
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
		
		// create the OpenGL surface view
		m_glView = new GLSurfaceView( this );
		m_glView.setRenderer( this );
		setContentView( m_glView );
		
		// create the subsystems
		m_graphics = new GLGraphics( m_glView );
		AndroidFileIO fileIOImpl = new AndroidFileIO( getAssets() );
		m_fileIO = fileIOImpl;
		m_audio = new AndroidAudio( this );
		
		final float DOUBLE_TAP_PERIOD = 0.4f;		// TODO: config
		m_input = new AndroidInput( this, m_glView, 1, 1, DOUBLE_TAP_PERIOD );
		
		// we wait with the screen initialization until the render surface gets created
		m_screen = null;
		
		// setup a wake lock
		PowerManager powerManager = (PowerManager)getSystemService( Context.POWER_SERVICE );
		m_wakeLock = powerManager.newWakeLock( PowerManager.FULL_WAKE_LOCK, "GLGame" );
		
		// initialize the operations
		m_gameLoopOperation = new GameLoopOperation();
		m_screenChangeTransaction = new ScreenChangeTransaction( this );
		m_operation = null;
	}
	
	@Override
	public void onResume() 
	{
		super.onResume();
		
		m_glView.onResume();
		m_wakeLock.acquire();
	}
	
	@Override
	public void onPause() 
	{
		synchronized( m_stateChanged ) 
		{
			// the game is being paused or closed - determine which is
			// the case and set the proper state so that the renderer
			// update loop can close down properly
			if( isFinishing() )
			{
				m_state = GLGameState.Finished;
			}
			else
			{
				m_state = GLGameState.Paused;
			}
		}
		
		m_screenChangeTransaction.onPause();		
		m_wakeLock.release();
		m_glView.onPause();
		
		super.onPause();
	}

	@Override
	public void onDrawFrame( GL10 arg0 ) 
	{
		// wait for the state stabilization
		GLGameState state = null;
		synchronized( m_stateChanged ) 
		{
			state = m_state;
		}
			
		switch( state )
		{
			case Running:
			{			
				// update the system timer
				float deltaTime = ( System.nanoTime() - m_startTime ) / 1000000000.0f;
				m_startTime = System.nanoTime();
				
				if ( m_operation != null )
				{
					m_operation.update( deltaTime, this );
				}
				break;
			}
			
			case Paused:
			{
				// pause the screen - this can take a while and since
				// it runs on a separate thread, we need to wait until
				// it's finished before we can move on and then notify
				// other locked threads that (i.e. the one that's running
				// the onPause method ), that it's safe to proceed
				if ( m_screen != null )
				{
					m_screen.pause();
				}
				
				synchronized( m_stateChanged ) 
				{
					m_state = GLGameState.Idle;
					m_stateChanged.notifyAll();
				}
				break;
			}
			
			case Finished:
			{
				// stop and release the screen - also wait for that activity
				// to end before moving on and then notify
				// other locked threads that (i.e. the one that's running
				// the onPause method ), that it's safe to proceed
				if ( m_screen != null )
				{
					m_screen.pause();
					m_screen.dispose();
				}
					
				synchronized( m_stateChanged ) 
				{
					m_state = GLGameState.Idle;
					m_stateChanged.notifyAll();
				}
				
				break;
			}
		}
	}

	@Override
	public void onSurfaceChanged( GL10 arg0, int arg1, int arg2 ) 
	{
		// nothing to do here
	}

	@Override
	public void onSurfaceCreated( GL10 gl, EGLConfig config ) 
	{
		m_graphics.setGL( gl );
		
		synchronized( m_stateChanged ) 
		{
			if( m_state == GLGameState.Initialized || m_screen == null )
			{
				// initialize the screen
				m_screen = getStartScreen();
			}
			
			// switch to the running state
			m_state = GLGameState.Running;
			if ( m_screen != null )
			{
				m_screen.resume();
			}
			
			// inform the operations about the new surface creation
			m_screenChangeTransaction.onSurfaceCreated( this );
			
			// start running the game loop
			switchToGameLoop();
			
			// reset the timer
			m_startTime = System.nanoTime();
		}
	}


	@Override
	public Input getInput() 
	{
		return m_input;
	}

	@Override
	public FileIO getFileIO() 
	{
		return m_fileIO;
	}

	@Override
	public Audio getAudio() 
	{
		return m_audio;
	}
	
	@Override
	public GLGraphics getGraphics() 
	{
		return m_graphics;
	}

	@Override
	public void setScreen( Screen screen ) 
	{
		if ( screen == null )
		{
			throw new IllegalArgumentException( "Screen must not be null" );
		}
		
		m_operation = m_screenChangeTransaction;
		m_screenChangeTransaction.initialize( this, screen );
	}

	@Override
	public Screen getCurrentScreen() 
	{
		return m_screen;
	}
	
	@Override
	public void closeGame()
	{
		super.finish();
	}
	
	@Override
	public void onBackPressed() 
	{
		boolean wasHandled = false;
		
		// back button was pressed - forward this to the screen
		if ( m_screen != null )
		{
			wasHandled = m_screen.onBackPressed();
		}
	
		if ( !wasHandled )
		{
			// screen didn't handle this event - so forward it to the higher instance and let it do its thing
			super.onBackPressed();
		}
	}
	
	// ------------------------------------------------------------------------
	// Game operations management
	// ------------------------------------------------------------------------
	
	/**
	 * Sets a new loading screen factory.
	 * 
	 * @param loadingScreenFactory
	 */
	public void setLoadingScreen( LoadingScreenFactory loadingScreenFactory )
	{
		m_screenChangeTransaction.setLoadingScreen( loadingScreenFactory );
	}
	
	/**
	 * Switches to the main game loop operation
	 */
	void switchToGameLoop()
	{
		m_operation = m_gameLoopOperation;
	}

}
