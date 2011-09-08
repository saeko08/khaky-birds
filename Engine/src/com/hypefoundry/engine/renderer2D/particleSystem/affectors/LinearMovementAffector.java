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
	private Vector3			m_speed;
	private Vector3			m_tmpSpeed = new Vector3();
	
	/**
	 * Default constructor.
	 */
	public LinearMovementAffector()
	{
		m_speed = new Vector3();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param speed
	 */
	public LinearMovementAffector( Vector3 speed )
	{
		m_speed = speed;
	}
	
	@Override
	public void update( float deltaTime, Particle particle ) 
	{
		m_tmpSpeed.set( m_speed ).scale( deltaTime );
		particle.m_position.add( m_tmpSpeed );
	}

	@Override
	public void load( DataLoader loader ) 
	{
		m_speed.load( "speed", loader );
	}

}
