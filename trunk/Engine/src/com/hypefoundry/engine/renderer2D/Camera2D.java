/**
 * 
 */
package com.hypefoundry.engine.renderer2D;

import javax.microedition.khronos.opengles.GL10;

import com.hypefoundry.engine.core.GLGraphics;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;

/**
 * A 2D camera with an orthogonal projection.
 * 
 * @author paksas
 *
 */
public class Camera2D 
{	
	public final Vector3 		m_position;
	public float 				m_zoom;
	public final float 			m_frustumWidth;
	public final float 			m_frustumHeight;
	final GLGraphics 			m_graphics;
	private final BoundingBox	m_frustum;
	
	
	/**
	 * Constructor.
	 * 
	 * @param graphics
	 * @param frustumWidth
	 * @param frustumHeight
	 */
	public Camera2D( GLGraphics graphics, float frustumWidth, float frustumHeight ) 
	{
		m_graphics = graphics;
		m_frustumWidth = frustumWidth;
		m_frustumHeight = frustumHeight;
		m_position = new Vector3( frustumWidth / 2, frustumHeight / 2, 0 );
		m_zoom = 1.0f;
		
		m_frustum = new BoundingBox( 0, 0, -1, m_frustumWidth, m_frustumHeight, 1 );
	}
	
	/**
	 * Positions the camera in the world before rendering.
	 */
	public void setViewportAndMatrices() 
	{
		// update the view frustum
		float halfZoom = m_zoom / 2;
		m_frustum.m_minX = m_position.m_x - m_frustumWidth * halfZoom;
		m_frustum.m_maxX = m_position.m_x + m_frustumWidth * halfZoom;
		m_frustum.m_minY = m_position.m_y - m_frustumHeight * halfZoom;
		m_frustum.m_maxY = m_position.m_y + m_frustumHeight * halfZoom;
				
		// set the viewport
		GL10 gl = m_graphics.getGL();
		
		gl.glViewport( 0, 0, m_graphics.getWidth(), m_graphics.getHeight() );
		gl.glMatrixMode( GL10.GL_PROJECTION );
		gl.glLoadIdentity();
		gl.glOrthof( m_frustum.m_minX, m_frustum.m_maxX, m_frustum.m_minY, m_frustum.m_maxY, m_frustum.m_maxZ, m_frustum.m_minZ );
		
		gl.glMatrixMode( GL10.GL_MODELVIEW );
		gl.glLoadIdentity();
	
	}
	
	/**
	 * Translates the touch position to the world coordinates
	 * 
	 * @param touch
	 */
	public void touchToWorld( Vector3 touch ) 
	{
		touch.m_x = (touch.m_x / (float) m_graphics.getWidth()) * m_frustumWidth * m_zoom;
		touch.m_y = (1 - touch.m_y / (float) m_graphics.getHeight()) * m_frustumHeight * m_zoom;
		touch.add( m_position ).sub( m_frustumWidth * m_zoom / 2, m_frustumHeight * m_zoom / 2, 0 );
	}
	
	/**
	 * Translates a direction vector from the screen to world coordinates
	 * 
	 * @param touch
	 */
	public void directionToWorld( Vector3 touch ) 
	{
		touch.m_x = (touch.m_x / (float) m_graphics.getWidth()) * m_frustumWidth * m_zoom;
		touch.m_y = (-touch.m_y / (float) m_graphics.getHeight()) * m_frustumHeight * m_zoom;
	}

	/**
	 * Returns the camera view frustum.
	 * 
	 * @return
	 */
	public BoundingBox getFrustum() 
	{
		return m_frustum;
	}
}
