/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.affectors;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleAffector;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * @author Paksas
 *
 */
public class AccelerationAffector implements ParticleAffector 
{
	private Vector3			m_acceleration = new Vector3();
	private Vector3			m_tmpVelocity = new Vector3();
	

	@Override
	public void update( float deltaTime, Particle particle ) 
	{
		m_tmpVelocity.set( m_acceleration ).scale( deltaTime );
		particle.m_velocity.add( m_tmpVelocity );
	}

	@Override
	public void load( DataLoader loader ) 
	{
		m_acceleration.load( "acceleration", loader );
	}

}
