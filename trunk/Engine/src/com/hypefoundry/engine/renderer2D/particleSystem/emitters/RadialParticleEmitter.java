/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.emitters;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleEmitter;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * Emits the particles from a single point aroound.
 * 
 * @author Paksas
 *
 */
public class RadialParticleEmitter extends ParticleEmitter 
{
	private Vector3			m_position;
	private float			m_speed;
	
	private Vector3			m_tmpVelocity = new Vector3();
	
	
	/**
	 * Default constructor.
	 */
	public RadialParticleEmitter() 
	{
		m_position = new Vector3();
		m_speed = 0;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param position
	 * @param speed
	 */
	public RadialParticleEmitter( Vector3 position, float speed ) 
	{
		m_position = position;
		m_speed = speed;
	}

	@Override
	protected void initialize( int particleIdx, Particle particle )
	{
		particle.m_position.set( m_position );
		
		float angle = ( (float)particleIdx * 360.0f ) / (float)m_particlesCount;
		m_tmpVelocity.set( Vector3.EX ).rotateZ( angle ).scale( m_speed );
		particle.m_velocity.set( m_tmpVelocity );
	}

	@Override
	protected void onLoad( DataLoader loader ) 
	{
		m_position.load( "pos", loader );
		m_speed = loader.getFloatValue( "speed" );
	}
}
