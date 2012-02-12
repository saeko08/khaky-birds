/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.particles;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.renderer2D.RenderState;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticlesFactory;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * @author Paksas
 *
 */
public class RandomlyTexturedParticleFactory implements ParticlesFactory 
{
	private TextureRegion	m_textureRegions[] = null;
	
	
	@Override
	public Particle create() 
	{
		if ( m_textureRegions == null )
		{
			return null;
		}
		
		int regionIdx = (int)(Math.random() * m_textureRegions.length);
		return new RandomlyTexturedParticle( m_textureRegions[regionIdx] );
	}


	@Override
	public void load( DataLoader loader, ResourceManager resMgr )
	{
		if ( m_textureRegions != null )
		{
			// particles are already initialized
			return;
		}
		
		DataLoader texturesNode = loader.getChild( "Textures" );
		if ( texturesNode == null )
		{
			return;
		}
		
		RenderState rs = new RenderState();
		rs.deserialize( resMgr, texturesNode );
		
		int count = texturesNode.getChildrenCount( "Texture" );
		m_textureRegions = new TextureRegion[count];
		
		int i = 0;
		for ( DataLoader regionNode = texturesNode.getChild( "Texture" ); regionNode != null; regionNode = regionNode.getSibling(), ++i )
		{
			m_textureRegions[i] = new TextureRegion( rs );
			m_textureRegions[i].deserializeCoordinates( regionNode );
		}
	}

}
