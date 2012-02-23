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
	private float		m_dispersionAngle;
	
	
	/**
	 * Default constructor.
	 */
	public DirectionalParticleEmitter()
	{
		m_position = new Vector3();
		m_velocity = new Vector3();
		m_dispersionAngle = 0.0f;
	}
	
	/**
	 * Constuctor.
	 * 
	 * @param position
	 * @param velocity
	 * @param dispersionAngle
	 */
	public DirectionalParticleEmitter( Vector3 position, Vector3 velocity, float dispersionAngle )
	{
		m_position = position;
		m_velocity = velocity;
		m_dispersionAngle = dispersionAngle;
	}
	
	@Override
	protected void initialize( int particleIdx, Particle particle ) 
	{
		particle.m_position.set( m_position );
		particle.m_velocity.set( m_velocity );
		
		// randomize the velocity within the half angle
		float randDispersionAngle = (float)( Math.random() - 0.5 ) * m_dispersionAngle;
		particle.m_velocity.rotateZ( randDispersionAngle );	
	}

	@Override
	protected void onLoad( DataLoader loader ) 
	{
		m_position.load( "position", loader );
		
		m_velocity.load( "direction", loader );
		m_velocity.normalize2D();
		
		float force = loader.getFloatValue( "force" );
		m_velocity.scale( force );
		
		m_dispersionAngle = loader.getFloatValue( "dispersionAngle", 0.0f );
	}

}
