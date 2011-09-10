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
	

	@Override
	public Particle create() 
	{
		return new TexturedParticle( m_textureRegion );
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
	}
}
