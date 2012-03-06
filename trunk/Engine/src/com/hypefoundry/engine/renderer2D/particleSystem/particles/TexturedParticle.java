/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.particles;

import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;


/**
 * A particle with a static texture.
 * 
 * @author Paksas
 *
 */
public class TexturedParticle extends Particle 
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
	public TexturedParticle( TextureRegion region, float width, float height )
	{
		m_textureRegion = region;
		m_width = width;
		m_height = height;
	}
	
	@Override
	protected void draw( float x, float y, SpriteBatcher batcher, float deltaTime )
	{
		if ( m_timeToLive > 0 )
		{
			batcher.drawSprite( m_position.m_x + x, m_position.m_y + y, m_width * m_scale, m_height * m_scale, m_orientation, m_textureRegion, m_color );
		}
	}
}
