/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.particles;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.renderer2D.Animation;
import com.hypefoundry.engine.renderer2D.AnimationPlayer;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * A particle with a static texture.
 * 
 * @author Paksas
 *
 */
public class TexturedParticle extends Particle 
{
	private TextureRegion	m_textureRegion = null;
	
	/**
	 * Default constructor.
	 */
	public TexturedParticle()
	{
		m_textureRegion = null;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param region
	 */
	public TexturedParticle( TextureRegion region )
	{
		m_textureRegion = region;
	}
	
	@Override
	protected void draw( SpriteBatcher batcher, float deltaTime )
	{
		if ( m_timeToLive > 0 )
		{
			batcher.drawSprite( m_position.m_x, m_position.m_y, m_width, m_height, m_orientation, m_textureRegion );
		}
	}
	
	@Override
	protected void load( DataLoader loader, ResourceManager resMgr ) 
	{
		if ( m_textureRegion != null )
		{
			// particle is already initialized
			return;
		}
		
		String texturePath = loader.getStringValue( "texturePath" );
		m_textureRegion = resMgr.getResource( TextureRegion.class, texturePath );
	}
}
