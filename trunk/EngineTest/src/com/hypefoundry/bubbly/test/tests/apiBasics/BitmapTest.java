package com.hypefoundry.bubbly.test.tests.apiBasics;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


public class BitmapTest extends Activity 
{
	class RenderView extends View 
	{
		Bitmap 		m_bob565;
		Bitmap 		m_bob4444;
		Rect 		m_dst = new Rect();
		
		public RenderView( Context context ) 
		{
			super( context );
			
			InputStream inputStream = null;
			try 
			{
				AssetManager assetManager = context.getAssets();
				inputStream = assetManager.open( "bitmaps/bobrgb888.png" );
				m_bob565 = BitmapFactory.decodeStream( inputStream );
				inputStream.close();
				Log.d( "BitmapTest", "bobrgb888.png format: " + m_bob565.getConfig() );
				
				inputStream = assetManager.open( "bitmaps/bobargb8888.png" );
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.ARGB_4444;
				m_bob4444 = BitmapFactory.decodeStream( inputStream, null, options );
				inputStream.close();
				Log.d( "BitmapTest", "bobargb8888.png format: " + m_bob4444.getConfig() );
			} 
			catch (IOException e) 
			{
				Log.d( "BitmapTest", "Error loading a bitmap: " + e.getMessage() );
			} 
			finally 
			{
				// we should really close our input streams here.
				if ( inputStream != null )
				{
					try
					{
						inputStream.close();
					}
					catch( IOException e )
					{
					}
				}
			}
		}
		
		protected void onDraw( Canvas canvas ) 
		{
			m_dst.set(50, 50, 350, 350);
			if ( m_bob565 != null )
			{
				canvas.drawBitmap( m_bob565, null, m_dst, null );
			}
			
			if ( m_bob4444 != null )
			{
				canvas.drawBitmap( m_bob4444, 100, 100, null );
			}
		invalidate();
		}
	}
	
	@Override
	public void onCreate( Bundle savedInstanceState ) 
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
		setContentView( new RenderView( this ) );
	}
}
