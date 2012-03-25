/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.particles;

import com.hypefoundry.engine.core.GLGraphics;
import com.hypefoundry.engine.renderer2D.Color;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.fx.LightShaft;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;

/**
 * A particle that looks like a light shaft
 * 
 * @author Paksas
 */
public class LightShaftParticle extends Particle 
{
	private LightShaft		m_lightShaft;
	private float			m_originalWidth;
	
	
	/**
	 * Constructor.
	 * 
	 * @param graphics
	 * @param color
	 */
	LightShaftParticle( GLGraphics graphics, Color color, float width )
	{
		m_lightShaft = new LightShaft( graphics, color );
		m_originalWidth = width;
	}
	
	@Override
	protected void draw( float x, float y, SpriteBatcher batcher, float deltaTime )
	{
		m_lightShaft.m_origin.set( x, y, 0.0f );
		m_lightShaft.m_end.set( m_position.m_x + x, m_position.m_y + y, 0.0f );
		m_lightShaft.m_width = m_originalWidth * m_scale;
		m_lightShaft.setAlpha( m_color.m_vals[Color.Alpha] );
		m_lightShaft.onShaftChanged();
		
		m_lightShaft.draw( batcher );
	}
}
