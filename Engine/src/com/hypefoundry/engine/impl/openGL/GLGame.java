/**
 * 
 */
package com.hypefoundry.engine.impl.openGL;

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
import com.hypefoundry.engine.core.Graphics;
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
	private Input 						m_input;
	private FileIO 						m_fileIO;
	private Screen 						m_screen;
	private WakeLock 					m_wakeLock;
	
	private GLGameState 				m_state = GLGameState.Initialized;
	private Object 						m_stateChanged = new Object();
	private long 						m_startTime = System.nanoTime();
	
	
	/**
	 * Constructor.
	 */
	public GLGame() 
	{
		// TODO Auto-generated constructor stub
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
		m_fileIO = new AndroidFileIO( getAssets() );
		m_audio = new AndroidAudio( this );
		m_input = new AndroidInput( this, m_glView, 1, 1 );
		
		// we wait with the screen initialization until the render surface gets created
		m_screen = null;
		
		// setup a wake lock
		PowerManager powerManager = (PowerManager)getSystemService( Context.POWER_SERVICE );
		m_wakeLock = powerManager.newWakeLock( PowerManager.FULL_WAKE_LOCK, "GLGame" );
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
			
			// wait for the renderer to close down
			while( true ) 
			{
				try 
				{
					m_stateChanged.wait();
					break;
				} 
				catch( InterruptedException e ) {}
			}
		}
		
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
		
		if ( state == GLGameState.Running )
		{
			// update and draw the screen, if the game's running, providing it with a proper time delta
			float deltaTime = ( System.nanoTime() - m_startTime ) / 1000000000.0f;
			m_startTime = System.nanoTime();
			m_screen.update( deltaTime );
			m_screen.present( deltaTime );
		}
		
		if( state == GLGameState.Paused ) 
		{
			// pause the screen - this can take a while and since
			// it runs on a separate thread, we need to wait until
			// it's finished before we can move on and then notify
			// other locked threads that (i.e. the one that's running
			// the onPause method ), that it's safe to proceed
			m_screen.pause();
			synchronized( m_stateChanged ) 
			{
				m_state = GLGameState.Idle;
				m_stateChanged.notifyAll();
			}
		}
		
		if( state == GLGameState.Finished ) 
		{
			// stop and release the screen - also wait for that activity
			// to end before moving on and then notify
			// other locked threads that (i.e. the one that's running
			// the onPause method ), that it's safe to proceed
			m_screen.pause();
			m_screen.dispose();
			
			synchronized( m_stateChanged ) 
			{
				m_state = GLGameState.Idle;
				m_stateChanged.notifyAll();
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
			if( m_state == GLGameState.Initialized )
			{
				// initialize the screen
				m_screen = getStartScreen();
			}
			
			// switch to the running state
			m_state = GLGameState.Running;
			m_screen.resume();
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
	public Graphics getGraphics() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Audio getAudio() 
	{
		return m_audio;
	}
	
	/**
	 * TODO change this to @Override
	 * @return
	 */
	public GLGraphics getGLGraphics() 
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
		
		// close the current screen
		m_screen.pause();
		m_screen.dispose();
		
		// start up the new screen
		screen.resume();
		screen.update(0);
		
		// memorize the new screen's instance
		m_screen = screen;
	}

	@Override
	public Screen getCurrentScreen() 
	{
		return m_screen;
	}
}
