/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.emitters;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleEmitter;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * Emits the particles from a single point
 * @author Paksas
 *
 */
public class PointParticleEmitter extends ParticleEmitter 
{
	private Vector3			m_position;

	/**
	 * Default constructor.
	 */
	public PointParticleEmitter() 
	{
		m_position = new Vector3();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param position
	 */
	public PointParticleEmitter( Vector3 position ) 
	{
		m_position = position;
	}

	@Override
	protected void initialize( Particle particle )
	{			
		particle.m_position.set( m_position );
	}

	@Override
	protected void onLoad( DataLoader loader ) 
	{
		m_position.load( "pos", loader );
	}
}
