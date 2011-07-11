package com.hypefoundry.engine.impl.core;

import com.hypefoundry.engine.impl.game.AndroidGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * A view that runs in a separate thread - the renderer thread.
 * 
 * At this point it also updates the game.
 * 
 * @author paksas
 *
 */
public class AndroidFastRenderView extends SurfaceView implements Runnable 
{
	AndroidGame 				m_game;
	Bitmap 						m_framebuffer;
	Thread 						m_renderThread = null;
	SurfaceHolder 				m_holder;
	volatile boolean 			m_running = false;
	
	/**
	 * Constructor.
	 * 
	 * @param game				host game instance
	 * @param framebuffer		framebuffer to which we want to render
	 */
	public AndroidFastRenderView( AndroidGame game, Bitmap framebuffer ) 
	{
		super( game );
		
		m_game = game;
		m_framebuffer = framebuffer;
		m_holder = getHolder();
	}
	
	/**
	 * Resume rendering.
	 */
	public void resume() 
	{
		m_running = true;
		m_renderThread = new Thread( this );
		m_renderThread.start();
	}
	
	/**
	 * Pauses the rendering functionality and the game updates.
	 */
	public void pause() 
	{
		// set a flag that will make the 'run' method finish its execution
		m_running = false;
		
		// wait for the 'run' method to stop and the thread to expire
		while( true ) 
		{
			try 
			{
				m_renderThread.join();
				break;
			}
			catch (InterruptedException e) 
			{
				// retry
			}
		}
	}
	
	@Override
	public void run() 
	{
		Rect dstRect = new Rect();
		long startTime = System.nanoTime();
		while( m_running ) 
		{
			if( !m_holder.getSurface().isValid() )
			{
				continue;
			}
			
			// keep track of time passed since the last iteration
			float deltaTime = ( System.nanoTime() - startTime ) / 1000000000.0f;
			startTime = System.nanoTime();
			
			// update the game
			m_game.getCurrentScreen().update( deltaTime );
			
			// present the game
			m_game.getCurrentScreen().present( deltaTime );
			
			// draw the contents of the framebuffer
			Canvas canvas = m_holder.lockCanvas();
			canvas.getClipBounds( dstRect );
			canvas.drawBitmap( m_framebuffer, null, dstRect, null );
			m_holder.unlockCanvasAndPost(canvas);
		}	
	}

}
