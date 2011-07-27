/**
 * 
 */
package com.hypefoundry.engine.impl.openGL;

import javax.microedition.khronos.opengles.GL10;

import com.hypefoundry.engine.core.Camera2D;
import com.hypefoundry.engine.math.Vector3;

/**
 * An OpenGL 2D camera implementation
 * 
 * @author paksas
 *
 */
public class GLCamera2D implements Camera2D
{
	public final Vector3 		m_position;
	public float 				m_zoom;
	public final float 			m_frustumWidth;
	public final float 			m_frustumHeight;
	final GLGraphics 			m_graphics;
	
	/**
	 * Constructor.
	 * 
	 * @param graphics
	 * @param frustumWidth
	 * @param frustumHeight
	 */
	public GLCamera2D( GLGraphics graphics, float frustumWidth, float frustumHeight ) 
	{
		m_graphics = graphics;
		m_frustumWidth = frustumWidth;
		m_frustumHeight = frustumHeight;
		m_position = new Vector3( frustumWidth / 2, frustumHeight / 2, 0 );
		m_zoom = 1.0f;
	}
	
	@Override
	public void setViewportAndMatrices() 
	{
		GL10 gl = m_graphics.getGL();
		
		gl.glViewport( 0, 0, m_graphics.getWidth(), m_graphics.getHeight() );
		gl.glMatrixMode( GL10.GL_PROJECTION );
		gl.glLoadIdentity();
		gl.glOrthof( m_position.m_x - m_frustumWidth * m_zoom / 2,
				m_position.m_x + m_frustumWidth * m_zoom/ 2,
				m_position.m_y - m_frustumHeight * m_zoom / 2,
				m_position.m_y + m_frustumHeight * m_zoom/ 2,
		1, -1);
		
		gl.glMatrixMode( GL10.GL_MODELVIEW );
		gl.glLoadIdentity();
	}
	
	@Override
	public void touchToWorld( Vector3 touch ) 
	{
		touch.m_x = (touch.m_x / (float) m_graphics.getWidth()) * m_frustumWidth * m_zoom;
		touch.m_y = (1 - touch.m_y / (float) m_graphics.getHeight()) * m_frustumHeight * m_zoom;
		touch.add( m_position ).sub( m_frustumWidth * m_zoom / 2, m_frustumHeight * m_zoom / 2, 0 );
	}
}
