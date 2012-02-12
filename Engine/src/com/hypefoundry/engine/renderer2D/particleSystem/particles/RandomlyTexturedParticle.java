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

	/**
	 * Constructor.
	 * 
	 * @param region
	 */
	public RandomlyTexturedParticle( TextureRegion region )
	{
		m_textureRegion = region;
	}
	
	@Override
	protected void draw( float x, float y, SpriteBatcher batcher, float deltaTime )
	{
		if ( m_timeToLive > 0 )
		{
			batcher.drawSprite( m_position.m_x + x, m_position.m_y + y, m_width, m_height, m_orientation, m_textureRegion );
		}
	}
}
