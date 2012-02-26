/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.particles;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticlesFactory;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * A factory for creating particles with static textures.
 * 
 * @author Paksas
 */
public class TexturedParticleFactory implements ParticlesFactory 
{

	private TextureRegion	m_textureRegion = null;
	private float			m_width;
	private float 			m_height;

	@Override
	public Particle create() 
	{
		return new TexturedParticle( m_textureRegion, m_width, m_height );
	}
	
	@Override
	public void load( DataLoader loader, ResourceManager resMgr ) 
	{
		if ( m_textureRegion != null )
		{
			// particle is already initialized
			return;
		}
		
		String texturePath = loader.getStringValue( "texturePath" );
		m_textureRegion = resMgr.getResource( TextureRegion.class, texturePath );
		
		m_width					= loader.getFloatValue( "width" );
		m_height				= loader.getFloatValue( "height" );
	}
}
