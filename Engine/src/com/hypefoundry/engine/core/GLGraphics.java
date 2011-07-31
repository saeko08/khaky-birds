/**
 * 
 */
package com.hypefoundry.engine.core;

import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView;

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
	}
	
	/**
	 * Returns the width of the framebuffer.
	 * 
	 * @return
	 */
	public int getWidth() 
	{
		return m_glView.getWidth();
	}
	
	/**
	 * Returns the height of the framebuffer.
	 * 
	 * @return
	 */
	public int getHeight() 
	{
		return m_glView.getHeight();
	}
}
