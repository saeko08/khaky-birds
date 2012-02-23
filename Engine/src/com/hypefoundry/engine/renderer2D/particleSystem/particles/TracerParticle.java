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
	private RenderState			m_renderState = new RenderState();
	private Spline				m_spline = new Spline();
	private boolean				m_fade;
	private float				m_fadeSpeed;
	
	/**
	 * Constructor.
	 * 
	 * @param color
	 * @param lineWidth
	 * @param fade			makes the particle fade away is its lifetime ends
	 */
	TracerParticle( Color color, float lineWidth, boolean fade )
	{
		m_fade = fade;
		
		m_renderState.setLineWidth( lineWidth );
		m_renderState.enableAlphaBlending( AlphaFunc.AF_Src_Alpha, AlphaFunc.AF_One_Minus_Src_Alpha );
		
		// initialize the spline
		m_spline.addPoint( new Vector3(), new Color( color ) ).m_colors[0].m_vals[ Color.Alpha ] = 0.0f;
		m_spline.addPoint( new Vector3(), new Color( color ) ).m_colors[1].m_vals[ Color.Alpha ] = 1.0f;
	}
	
	@Override
	public void onInitialized() 
	{
		// initialize the fade speed
		m_fadeSpeed = ( m_fade && ( m_timeToLive > 0.0f ) ) ? ( 1.0f / m_timeToLive ) : 0.0f;
		
		// reset the position and color
		m_spline.m_points[0].set( m_position );
		m_spline.m_points[1].set( m_position );
		m_spline.m_colors[1].m_vals[Color.Alpha] = 1.0f;
	}
	
	@Override
	protected void draw( float x, float y, SpriteBatcher batcher, float deltaTime )
	{		
		if ( m_timeToLive > 0 )
		{
			// update the spline
			m_spline.m_points[0].set( m_spline.m_points[1] );
			m_spline.m_points[1].set( m_position );
			m_spline.m_colors[1].m_vals[Color.Alpha] -= m_fadeSpeed * deltaTime;
			m_spline.refresh();
			
			batcher.drawSpline( x, y, m_spline, m_renderState );
		}
	}
}
