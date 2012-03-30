/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.affectors;

import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleAffector;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * @author Paksas
 *
 */
public class BubblesAffector implements ParticleAffector
{
	float 			totalTime = 0.0f;

	@Override
	public void update( float deltaTime, Particle particle ) 
	{
		totalTime += deltaTime;
		float particleUniquenessFactor = (float)particle.hashCode() / 10000.0f;
		float bobbingFactor = (float)Math.sin( totalTime + particleUniquenessFactor );
		
		// modify the velocity, making the bullbe bob up and down
		particle.m_velocity.m_y = bobbingFactor * 0.3f;
		
		// modify the scale as well - make it smaller as it goes higher
		particle.m_scale = 0.8f + Math.abs( bobbingFactor ) * 0.2f;
	}

	@Override
	public void load( DataLoader loader ) 
	{
	}

}
