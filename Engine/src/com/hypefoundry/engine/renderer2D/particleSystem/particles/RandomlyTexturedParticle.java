/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.particles;

import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;

/**
 * @author Paksas
 *
 */
public class RandomlyTexturedParticle extends Particle 
{
	private TextureRegion	m_textureRegion = null;
	private float			m_width;
	private float			m_height;
	
	/**
	 * Constructor.
	 * 
	 * @param region
	 * @param width
	 * @param height
	 */
	public RandomlyTexturedParticle( TextureRegion region )
	{
		m_textureRegion = region;
		m_width = m_textureRegion.widthInPixels() * 0.01f;
		m_height = m_textureRegion.heightInPixels() * 0.01f;
	}
	
	@Override
	protected void draw( float x, float y, SpriteBatcher batcher, float deltaTime )
	{
		if ( m_timeToLive > 0 )
		{
			batcher.drawSprite( m_position.m_x + x, m_position.m_y + y, m_width * m_scale, m_height * m_scale, m_orientation, m_textureRegion );
		}
	}
}
