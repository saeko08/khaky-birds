package com.hypefoundry.bubbly.test.tests.apiBasics;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;


public class SurfaceViewTest extends Activity 
{
	FastRenderView 		m_renderView;
	
	public void onCreate( Bundle savedInstanceState ) 
	{
		super.onCreate( savedInstanceState );
		
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
		
		m_renderView = new FastRenderView( this );
		setContentView( m_renderView );
	}
	
	protected void onResume() 
	{
		super.onResume();
		m_renderView.resume();
	}
	
	protected void onPause() 
	{
		super.onPause();
		m_renderView.pause();
	}
	
	class FastRenderView extends SurfaceView implements Runnable 
	{
		Thread 				m_renderThread = null;
		SurfaceHolder 		m_holder;
		volatile boolean 	m_running = false;
		
		public FastRenderView( Context context ) 
		{
			super( context );
			m_holder = getHolder();
		}
	
		public void resume() 
		{
			m_running = true;
			m_renderThread = new Thread( this );
			m_renderThread.start();
		}
		
		public void run() 
		{
			while( m_running ) 
			{
				if( !m_holder.getSurface().isValid() )
				{
					continue;
				}
				
				// perform the actual drawing
				Canvas canvas = m_holder.lockCanvas();
				canvas.drawRGB(255, 0, 0);
				m_holder.unlockCanvasAndPost( canvas );
			}
		}
		
		public void pause() 
		{
			m_running = false;
			while( true ) 
			{
				try 
				{
					m_renderThread.join();
				} 
				catch (InterruptedException e) 
				{
					// retry
				}
			}
		}
	}
}
