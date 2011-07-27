package com.hypefoundry.bubbly.test.tests.openGL;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


public class GLSurfaceViewTest extends Activity 
{
	private GLSurfaceView 		m_glView;
	
	static class SimpleRenderer implements Renderer 
	{
		Random m_rand = new Random();
		
		@Override
		public void onSurfaceCreated( GL10 gl, EGLConfig config ) 
		{
			Log.d("GLSurfaceViewTest", "surface created");
		}
		
		@Override
		public void onSurfaceChanged( GL10 gl, int width, int height ) 
		{
			Log.d("GLSurfaceViewTest", "surface changed: " + width + "x" + height);
		}
		
		@Override
		public void onDrawFrame( GL10 gl ) 
		{
			gl.glClearColor( m_rand.nextFloat(), m_rand.nextFloat(), m_rand.nextFloat(), 1 );
			gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
		}
	}
	
	public void onCreate( Bundle savedInstanceState ) 
	{
		super.onCreate( savedInstanceState );
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
		m_glView = new GLSurfaceView( this );
		m_glView.setRenderer( new SimpleRenderer() );
		setContentView( m_glView );
	}
	
	@Override
	public void onResume() 
	{
		super.onPause();
		m_glView.onResume();
	}
	
	@Override
	public void onPause() 
	{
		super.onPause();
		m_glView.onPause();
	}
}
