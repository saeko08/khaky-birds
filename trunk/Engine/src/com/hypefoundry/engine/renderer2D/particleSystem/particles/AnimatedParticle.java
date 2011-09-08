/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.particles;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.renderer2D.Animation;
import com.hypefoundry.engine.renderer2D.AnimationPlayer;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * A particle with an animated texture.
 * 
 * @author Paksas
 *
 */
public class AnimatedParticle extends Particle 
{
	private AnimationPlayer		m_player;
	
	/**
	 * Default constructor.
	 */
	public AnimatedParticle()
	{
		m_player = null;
	}
	
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
	protected void draw( SpriteBatcher batcher, float deltaTime )
	{
		if ( m_timeToLive > 0 && m_player != null )
		{
			batcher.drawSprite( m_position.m_x, m_position.m_y, m_width, m_height, m_orientation, m_player.getTextureRegion( deltaTime ) );
		}
	}
	
	@Override
	protected void load( DataLoader loader, ResourceManager resMgr ) 
	{
		if ( m_player != null )
		{
			// particle is already initialized
			return;
		}
		
		String animPath = loader.getStringValue( "animPath" );
		Animation animation = resMgr.getResource( Animation.class, animPath );
		
		m_player = new AnimationPlayer();
		int animIdx = m_player.addAnimation( animation );
		m_player.select( animIdx );
	}
}
