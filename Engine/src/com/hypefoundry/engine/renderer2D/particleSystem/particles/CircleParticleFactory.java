/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.particles;

import com.hypefoundry.engine.core.GLGraphics;
import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.renderer2D.Color;
import com.hypefoundry.engine.renderer2D.RenderState;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticlesFactory;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * @author Paksas
 *
 */
public class CircleParticleFactory implements ParticlesFactory 
{
	private GLGraphics		m_graphics;
	private RenderState		m_renderState = new RenderState();
	private Color			m_color = new Color();
	private float			m_radius = 0.5f;
	private int				m_segmentsCount = 16;
	
	/**
	 * Constructor.
	 * 
	 * @param graphics
	 */
	public CircleParticleFactory( GLGraphics graphics )
	{
		m_graphics = graphics;
	}
	
	@Override
	public Particle create() 
	{
		return new CircleParticle( m_graphics, m_renderState, m_color, m_radius, m_segmentsCount );
	}

	@Override
	public void load( DataLoader loader, ResourceManager resMgr ) 
	{
		DataLoader circleParticleNode = loader.getChild( "CircleParticle" );
		if ( circleParticleNode != null )
		{
			m_renderState.deserialize( resMgr, circleParticleNode );
			
			DataLoader colorNode = circleParticleNode.getChild( "color" );
			if ( colorNode != null )
			{
				m_color.deserialize( colorNode );
			}
			
			m_radius = circleParticleNode.getFloatValue( "radius", 0.5f );
			m_segmentsCount = circleParticleNode.getIntValue( "segments", 16 );
		}
	}
}
