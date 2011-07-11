package com.hypefoundry.engine.impl.game;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
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
import com.hypefoundry.engine.game.Updatable;
import com.hypefoundry.engine.impl.core.AndroidAudio;
import com.hypefoundry.engine.impl.core.AndroidFastRenderView;
import com.hypefoundry.engine.impl.core.AndroidFileIO;
import com.hypefoundry.engine.impl.core.AndroidGraphics;
import com.hypefoundry.engine.impl.core.AndroidInput;


/**
 * A Game implementation using the Android SDK.
 * 
 * This implementation manages the device's WAKE LOCK, so keep in mind
 * to add the following line to the application's manifest:
 * 		<uses-permission android:name="android.permission.WAKE_LOCK"/>
 * 
 * @author paksas
 *
 */
public class AndroidGame extends Activity implements Game 
{
	AndroidFastRenderView 		m_renderView;
	Graphics 					m_graphics;
	Audio 						m_audio;
	Input 						m_input;
	FileIO 						m_fileIO;
	Screen 						m_screen;
	WakeLock 					m_wakeLock;
	List<Updatable>				m_updatables;
	
	@Override
	public void onCreate( Bundle savedInstanceState ) 
	{
		super.onCreate( savedInstanceState );
		
		// go fullscreen
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
		
		// create a framebuffer
		boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
		int frameBufferWidth = isLandscape ? 480 : 320;
		int frameBufferHeight = isLandscape ? 320 : 480;
		Bitmap frameBuffer = Bitmap.createBitmap( frameBufferWidth, frameBufferHeight, Config.RGB_565 );
		
		// calculate the view scale
		float scaleX = (float) frameBufferWidth / getWindowManager().getDefaultDisplay().getWidth();
		float scaleY = (float) frameBufferHeight / getWindowManager().getDefaultDisplay().getHeight();
		
		// create the subsystems
		m_renderView = new AndroidFastRenderView( this, frameBuffer );
		m_graphics = new AndroidGraphics( getAssets(), frameBuffer );
		m_fileIO = new AndroidFileIO( getAssets() );
		m_audio = new AndroidAudio( this );
		m_input = new AndroidInput( this, m_renderView, scaleX, scaleY );
		m_screen = getStartScreen();
		
		// set the view
		setContentView( m_renderView );
		
		// setup a wake lock
		PowerManager powerManager = (PowerManager)getSystemService( Context.POWER_SERVICE );
		m_wakeLock = powerManager.newWakeLock( PowerManager.FULL_WAKE_LOCK, "GLGame" );
		
		// create the list that will store the registered updatable objects
		m_updatables = new ArrayList<Updatable>();
	}
	
	@Override
	public void onResume() 
	{
		super.onResume();
		
		// acquire the wake lock
		m_wakeLock.acquire();
		
		// resume the screen's functionality and the rendering
		m_screen.resume();
		m_renderView.resume();
	}
	
	@Override
	public void onPause() 
	{
		super.onPause();
		
		// release the wake lock
		m_wakeLock.release();
		
		// stop rendering
		m_renderView.pause();
		m_screen.pause();
		
		// we're closing down - release the used memory
		if ( isFinishing() )
		{
			m_screen.dispose();
		}
	}
	
	/**
	 * Updates the state of the game.
	 * 
	 * @param deltaTime
	 */
	public void update( float deltaTime )
	{
		Screen currentScreen = getCurrentScreen();
		
		// update the updatable objects
		for ( Updatable updatable : m_updatables )
		{
			updatable.update( deltaTime );
		}
		
		// update current screen
		currentScreen.update( deltaTime );
		
		// present the screen
		currentScreen.present( deltaTime );
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
		return m_graphics;
	}

	@Override
	public Audio getAudio() 
	{
		return m_audio;
	}

	@Override
	public void setScreen( Screen screen ) 
	{
		if (screen == null)
		{
			throw new IllegalArgumentException( "Screen must not be null" );
		}
		
		// stop the current screen and release all associated resources
		m_screen.pause();
		m_screen.dispose();
		
		// start up the new screen
		m_screen.resume();
		m_screen.update(0);
		m_screen = screen;
	}

	@Override
	public Screen getCurrentScreen() 
	{
		return m_screen;
	}

	@Override
	public Screen getStartScreen() 
	{
		return m_screen;
	}
	
	@Override
	public void addUpdatable( Updatable updatable )
	{
		if ( updatable != null )
		{
			m_updatables.add( updatable );
		}
	}
	
	@Override
	public void removeUpdatable( Updatable updatable )
	{
		if ( updatable != null )
		{
			m_updatables.remove( updatable );
		}
	}
}
