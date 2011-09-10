/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.affectors;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleAffector;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * Affector that moves the particles in a single direction with the specified speed.
 * 
 * @author Paksas
 *
 */
public class LinearMovementAffector implements ParticleAffector
{
	private Vector3			m_velocity;
	private Vector3			m_tmpVelocity = new Vector3();
	
	/**
	 * Default constructor.
	 */
	public LinearMovementAffector()
	{
		m_velocity = new Vector3();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param velocity
	 */
	public LinearMovementAffector( Vector3 velocity )
	{
		m_velocity = velocity;
	}
	
	@Override
	public void update( float deltaTime, Particle particle ) 
	{
		m_tmpVelocity.set( m_velocity ).scale( deltaTime );
		particle.m_velocity.add( m_tmpVelocity );
	}

	@Override
	public void load( DataLoader loader ) 
	{
		m_velocity.load( "velocity", loader );
	}

}
