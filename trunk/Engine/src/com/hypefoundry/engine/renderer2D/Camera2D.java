/**
 * 
 */
package com.hypefoundry.engine.renderer2D;

import javax.microedition.khronos.opengles.GL10;

import com.hypefoundry.engine.core.GLGraphics;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.MathLib;
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
	public float 				m_frustumWidth;
	public float 				m_frustumHeight;
	
	public int					m_screenWidth;
	public int					m_screenHeight;
	public int					m_viewportWidth;
	public int					m_viewportHeight;
	public int					m_viewportPosX;
	public int					m_viewportPosY;
	
	final GLGraphics 			m_graphics;
	private final BoundingBox	m_frustum;
	
	
	/**
	 * Constructor.
	 * 
	 * @param graphics
	 * @param frustumWidth (in pixels)
	 * @param frustumHeight (in pixels)
	 * @param pixelsToMeters		pixels to meters conversion factor
	 */
	public Camera2D( GLGraphics graphics, int frustumWidth, int frustumHeight, float pixelsToMeters ) 
	{	
		m_graphics = graphics;
		
		// Calculate the viewport parameters
		Vector3 viewportDimensions = m_graphics.getViewportDimensions();
		
		m_screenWidth = m_graphics.getWidth();
		m_screenHeight = m_graphics.getHeight();
		m_viewportWidth = (int)viewportDimensions.m_x;
		m_viewportHeight = (int)viewportDimensions.m_y;
		m_viewportPosX = ( m_screenWidth - m_viewportWidth ) / 2;
		m_viewportPosY = ( m_screenHeight - m_viewportHeight ) / 2;
				
		// calculate frustum parameters
		m_frustumWidth = frustumWidth * pixelsToMeters;
		m_frustumHeight = frustumHeight * pixelsToMeters;
		m_position = new Vector3( m_frustumWidth / 2.0f, m_frustumHeight / 2.0f, 0 );
		
		m_zoom = 1.0f;	
		m_frustum = new BoundingBox( 0, 0, m_frustumWidth, m_frustumHeight );
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
		
		gl.glViewport( m_viewportPosX, m_viewportPosY, m_viewportWidth, m_viewportHeight );
		gl.glMatrixMode( GL10.GL_PROJECTION );
		gl.glLoadIdentity();
		gl.glOrthof( m_frustum.m_minX, m_frustum.m_maxX, m_frustum.m_minY, m_frustum.m_maxY, 1, -1 );
		
		gl.glMatrixMode( GL10.GL_MODELVIEW );
		gl.glLoadIdentity();
	
	}
	
	/**
	 * Translates the touch position to the world coordinates
	 * 
	 * @param screen
	 * @param world
	 */
	public void screenPosToWorld( Vector3 screen, Vector3 world ) 
	{
		// convert to viewport coordinates
		float vX = screen.m_x - m_viewportPosX;
		float vY = ( m_screenHeight - screen.m_y ) - m_viewportPosY;
		
		world.m_x = ( vX / (float)m_viewportWidth ) * m_frustumWidth * m_zoom;
		world.m_y = ( vY / (float)m_viewportHeight ) * m_frustumHeight * m_zoom;
		world.add( m_position ).sub( m_frustumWidth * m_zoom / 2, m_frustumHeight * m_zoom / 2, 0 );
	}
	
	/**
	 * Translates a direction vector from the screen to world coordinates
	 * 
	 * @param screen
	 * @param world
	 */
	public void screenVecToWorld( Vector3 screen, Vector3 world ) 
	{				
		world.m_x = ( screen.m_x / (float)m_viewportWidth ) * m_frustumWidth * m_zoom;
		world.m_y = (-screen.m_y / (float)m_viewportHeight ) * m_frustumHeight * m_zoom;
	}
	
	/**
	 * Translates a dimension vector from the screen to world coordinates
	 * 
	 * @param screen
	 * @param world
	 */
	public void screenDimToWorld( Vector3 screen, Vector3 world ) 
	{		
		world.m_x = ( screen.m_x / (float)m_viewportWidth ) * m_frustumWidth * m_zoom;
		world.m_y = ( screen.m_y / (float)m_viewportHeight ) * m_frustumHeight * m_zoom;
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
