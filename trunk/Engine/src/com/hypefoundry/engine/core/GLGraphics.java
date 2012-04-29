/**
 * 
 */
package com.hypefoundry.engine.core;

import javax.microedition.khronos.opengles.GL10;

import com.hypefoundry.engine.math.MathLib;
import com.hypefoundry.engine.math.Vector3;

import android.opengl.GLSurfaceView;
import android.view.SurfaceHolder;

/**
 * OpenGL graphics system interface.
 * 
 * @author paksas
 *
 */
public class GLGraphics 
{
	private 	GLSurfaceView 		m_glView;
	private		GL10 				m_gl;
	
	// viewport definition - defines the nominal resolution for which the graphics was designed
	private		final float			m_pixelsToMeters			= 0.01f;
	private 	final float			m_nativeViewportWidth		= 480.0f;
	private 	final float			m_nativelViewportHeight		= 800.0f;
	
	private 	Vector3				m_viewportDimensions		= new Vector3();
	private 	Vector3				m_viewportWorldDimensions	= new Vector3();
	
	/**
	 * Constructor.
	 * 
	 * @param glView		openGL view instance
	 */
	public GLGraphics( GLSurfaceView glView ) 
	{
		m_glView = glView;
	}
	
	/**
	 * Returns the GL interface.
	 * 
	 * @return
	 */
	public GL10 getGL() 
	{
		return m_gl;
	}
	
	/**
	 * Sets the GL interface implementation to be used by the game.
	 * 
	 * @param gl
	 */
	public void setGL( GL10 gl ) 
	{
		m_gl = gl;
		
		// calculate the nominal viewport dimensions
		Vector3 viewportSize = new Vector3( m_glView.getWidth(), m_glView.getHeight(), 0 );
		m_viewportDimensions.set( m_nativeViewportWidth, m_nativelViewportHeight, 0 );
		MathLib.processAspectRatio( viewportSize, m_viewportDimensions );
		
		m_viewportWorldDimensions.set( m_viewportDimensions ).scale( m_pixelsToMeters );
	}

	/**
	 * Returns the rendering surface holder.
	 * 
	 * @return
	 */
	public SurfaceHolder getSurfaceHolder() 
	{
		return m_glView.getHolder();
	}
	
	/**
	 * Returns a vector with the viewport dimensions ( in pixels )
	 * 
	 * @return
	 */
	public Vector3 getViewportDimensions()
	{
		return m_viewportDimensions;
	}
	
	/**
	 * Returns a vector with the viewport dimensions ( in meters )
	 * @return
	 */
	public Vector3 getViewportWorldDimensions()
	{
		return m_viewportDimensions;
	}
	
	/**
	 * Returns the screen's width.
	 *  
	 * @return
	 */
	public int getWidth()
	{
		return m_glView.getWidth();
	}
	
	/**
	 * Returns the screen's height.
	 *  
	 * @return
	 */
	public int getHeight()
	{
		return m_glView.getHeight();
	}
}
