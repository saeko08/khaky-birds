/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.particles;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.renderer2D.animation.Animation;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticlesFactory;
import com.hypefoundry.engine.util.serialization.DataLoader;


/**
 * A factory of particles with an animated texture.
 * 
 * @author Paksas
 *
 */
public class AnimatedParticleFactory implements ParticlesFactory 
{
	private Animation		m_animation = null;
	

	@Override
	public Particle create()
	{
		return new AnimatedParticle( m_animation );
	}
	
	@Override
	public void load( DataLoader loader, ResourceManager resMgr ) 
	{
		if ( m_animation != null )
		{
			// particle is already initialized
			return;
		}
		
		String animPath = loader.getStringValue( "animPath" );
		m_animation = resMgr.getResource( Animation.class, animPath );
	}
}
