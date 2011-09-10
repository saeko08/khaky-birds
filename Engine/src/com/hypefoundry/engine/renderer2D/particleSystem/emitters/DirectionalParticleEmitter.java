/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.emitters;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleEmitter;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * Emits the particles in the specified direction.
 * 
 * @author Paksas
 */
public class DirectionalParticleEmitter extends ParticleEmitter 
{
	private Vector3 	m_position;
	private Vector3 	m_velocity;
	
	
	/**
	 * Default constructor.
	 */
	public DirectionalParticleEmitter()
	{
		m_position = new Vector3();
		m_velocity = new Vector3();
	}
	
	/**
	 * Constuctor.
	 * 
	 * @param position
	 * @param velocity
	 */
	public DirectionalParticleEmitter( Vector3 position, Vector3 velocity )
	{
		m_position = position;
		m_velocity = velocity;
	}
	
	@Override
	protected void initialize( int particleIdx, Particle particle ) 
	{
		particle.m_position.set( m_position );
		particle.m_velocity.set( m_velocity );
	}

	@Override
	protected void onLoad( DataLoader loader ) 
	{
		m_position.load( "pos", loader );
		m_velocity.load( "velocity", loader );
	}

}
