/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.particles;

import com.hypefoundry.engine.renderer2D.Animation;
import com.hypefoundry.engine.renderer2D.AnimationPlayer;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;

/**
 * A particle with an animated texture.
 * @author Paksas
 *
 */
public class AnimatedParticle extends Particle 
{
	private AnimationPlayer		m_player;
	
	/**
	 * Constructor.
	 * 
	 * @param animation
	 */
	public AnimatedParticle( Animation animation )
	{
		m_player = new AnimationPlayer();
		int animIdx = m_player.addAnimation( animation );
		m_player.select( animIdx );
	}
	
	@Override
	protected void draw( float x, float y, SpriteBatcher batcher, float deltaTime )
	{
		if ( m_timeToLive > 0 && m_player != null )
		{
			batcher.drawSprite( m_position.m_x + x, m_position.m_y + y, m_width, m_height, m_orientation, m_player.getTextureRegion( deltaTime ) );
		}
	}
}
