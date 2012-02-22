/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.particles;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.renderer2D.Color;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticlesFactory;
import com.hypefoundry.engine.util.serialization.DataLoader;


/**
 * @author Paksas
 *
 */
public class TracerParticleFactory implements ParticlesFactory
{
	private Color[]		m_colors;
	private float		m_linesWidth;
	
	
	@Override
	public Particle create() 
	{
		int colorIdx = (int)( Math.random() * m_colors.length );
		return new TracerParticle( m_colors[colorIdx], m_linesWidth );
	}

	@Override
	public void load( DataLoader loader, ResourceManager resMgr ) 
	{
		DataLoader colorsNode = loader.getChild( "ParticleColors" );
		if ( colorsNode == null )
		{
			return;
		}
				
		int count = colorsNode.getChildrenCount( "Color" );
		m_colors = new Color[count];
		
		int i = 0;
		for ( DataLoader colorNode = colorsNode.getChild( "Color" ); colorNode != null; colorNode = colorNode.getSibling(), ++i )
		{
			m_colors[i] = new Color();
			m_colors[i].deserialize( colorNode );
		}
		
		m_linesWidth = loader.getFloatValue( "particleWidth" );
	}

}
