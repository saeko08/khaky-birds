/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.particles;

import com.hypefoundry.engine.core.GLGraphics;
import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.renderer2D.Color;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticlesFactory;
import com.hypefoundry.engine.util.serialization.DataLoader;


/**
 * @author Paksas
 *
 */
public class LightShaftParticleFactory implements ParticlesFactory 
{
	private GLGraphics 		m_graphics;
	private Color			m_color = new Color();
	private float			m_brightnessRange;
	private float			m_widthRange;
	private float			m_minWidth;
	
	/**
	 * Constructor.
	 * 
	 * @param graphics
	 */
	public LightShaftParticleFactory( GLGraphics graphics )
	{
		m_graphics = graphics;
	}
	
	@Override
	public Particle create() 
	{
		float width = m_minWidth + (float)Math.random() * m_widthRange;
		
		Color color = new Color( m_color );
		color.brighter( (float)Math.random() * m_brightnessRange );
		
		return new LightShaftParticle( m_graphics, color, width );
	}

	@Override
	public void load( DataLoader loader, ResourceManager resMgr ) 
	{
		DataLoader shaftNode = loader.getChild( "LightShaft" );
		if ( shaftNode != null )
		{
			DataLoader colorNode = shaftNode.getChild( "color" );
			if ( colorNode != null )
			{
				m_color.deserialize( colorNode );
			}
			
			m_brightnessRange = shaftNode.getFloatValue( "brightnessRange", 0.0f );
			m_widthRange = shaftNode.getFloatValue( "widthRange", 0.0f );
			m_minWidth = shaftNode.getFloatValue( "minWidth", 0.5f );
		}

	}

}
