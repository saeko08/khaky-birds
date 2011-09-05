/**
 * 
 */
package com.hypefoundry.engine.renderer2D.fx;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Color;
import com.hypefoundry.engine.renderer2D.Spline;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;

/**
 * An effect of a lightning.
 * 
 * @author Paksas
 *
 */
public class LightningFX 
{
	private Spline 				m_spline;
	private float				m_width;
	
	private Spline				m_particles;
	private Spline				m_worldSpaceParticles;
	private Vector3				m_tmpPos = new Vector3();
	
	private Color				m_bgColor;
	private Color				m_fgColor;
	
	
	/**
	 * Constructor.
	 * 
	 * @param spline			spline the lightning should follow
	 * @param length
	 * @param width
	 * @param particlesCount
	 * @param color
	 */
	public LightningFX( Spline spline, float length, float width, int particlesCount, Color color )
	{
		m_spline = spline;
		
		// make sure the parameters are correct
		if ( particlesCount <= 0 )
		{
			particlesCount = 2;
		}
		if ( length <= 0 )
		{
			length = 0.1f;
		}
		
		m_width = width;
		if ( m_width <= 0 )
		{
			m_width = 0.1f;
		}
		
		m_bgColor = new Color( color ).brighter( 0.5f );
		m_fgColor = color;
		
		// calculate the base placement of the particles
		m_particles = new Spline();
		m_worldSpaceParticles = new Spline();
		float scatterDist = length / particlesCount;
		float x, y;
		x = 0;
		for ( int i = 0; i < particlesCount; ++i )
		{
			if ( i == 0 || i == particlesCount - 1 )
			{
				y = 0;
			}
			else
			{
				y = (float)( Math.random() - 0.5 ) * m_width;
			}

			m_particles.addPoint( new Vector3( x, y, 0 ) );
			x += scatterDist;
			
			m_worldSpaceParticles.addPoint( new Vector3() );
		}
	}
	
	/**
	 * Draws the spline on the screen.
	 * 
	 * @param batcher
	 */
	public void draw( SpriteBatcher batcher, float offset )
	{
		// update the initial positions
		for ( int i = 2; i < m_particles.m_points.length - 1; ++i )
		{
			m_particles.m_points[i - 1].m_y = m_particles.m_points[i].m_y; 
		}
		m_particles.m_points[m_particles.m_points.length - 2].m_y = (float)( Math.random() - 0.5 ) * m_width;
		// refresh the spline
		m_particles.refresh();
		
		// create a world-space copy
		for ( int i = 0; i < m_particles.m_points.length; ++i )
		{
			m_tmpPos.set( m_particles.m_points[i] );
			m_tmpPos.m_x += offset;
			
			m_spline.transform( m_tmpPos, m_worldSpaceParticles.m_points[i] );
		}
		// refresh the spline
		m_worldSpaceParticles.refresh();
		
		// draw
		batcher.drawSpline( m_worldSpaceParticles, m_bgColor, 7.0f );
		batcher.drawSpline( m_worldSpaceParticles, m_fgColor, 2.0f );
	}
}
