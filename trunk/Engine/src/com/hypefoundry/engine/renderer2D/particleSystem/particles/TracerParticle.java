/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.particles;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.AlphaFunc;
import com.hypefoundry.engine.renderer2D.Color;
import com.hypefoundry.engine.renderer2D.RenderState;
import com.hypefoundry.engine.renderer2D.Spline;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;

/**
 * @author Paksas
 *
 */
public class TracerParticle extends Particle 
{
	private Color				m_color;
	private RenderState			m_renderState = new RenderState();
	private Spline				m_spline = new Spline();
	
	/**
	 * Constructor.
	 * 
	 * @param color
	 * @param lineWidth
	 */
	TracerParticle( Color color, float lineWidth )
	{
		m_color = color;
		m_renderState.setLineWidth( lineWidth );
		m_renderState.enableAlphaBlending( AlphaFunc.AF_Src_Alpha, AlphaFunc.AF_Dest_Color );
		
		// initialize the spline
		m_spline.addPoint( new Vector3() );
		m_spline.addPoint( new Vector3() );
	}
	
	@Override
	protected void draw( float x, float y, SpriteBatcher batcher, float deltaTime )
	{
		if ( m_timeToLive > 0 )
		{
			// update the spline
			// TODO m_spline.m_points[0].
			
			batcher.drawSpline( m_spline, m_color, m_renderState );
		}
	}
}
