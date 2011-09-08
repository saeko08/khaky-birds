/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.emitters;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleEmitter;

/**
 * Emits the particles from a single point
 * @author Paksas
 *
 */
public class PointParticleEmitter implements ParticleEmitter 
{
	private int 			m_maxAliveParticles;
	private float 			m_emissionFrequency;
	private Vector3			m_position;
	
	private Particle[]		m_particles;
	private int				m_nextParticleIdx;
	private float			m_timeElapsed;
	
	/**
	 * Constructor.
	 * 
	 * @param maxAliveParticles
	 * @param emissionFrequency
	 * @param position
	 */
	public PointParticleEmitter( int maxAliveParticles, float emissionFrequency, Vector3 position ) 
	{
		m_maxAliveParticles = maxAliveParticles;
		m_emissionFrequency = emissionFrequency;
		m_position = position;
		
		m_particles = new Particle[m_maxAliveParticles];
		for ( int i = 0; i < m_particles.length; ++i )
		{
			m_particles[i] = new Particle();
		}
		
		m_nextParticleIdx = 0; 
		m_timeElapsed = 0;
	}


	@Override
	public void update( float deltaTime )
	{
		m_timeElapsed += deltaTime;
		
		// wait for the right moment to spawn particles.
		// Since more time could have passed since the last time
		// this method was called, make sure we spawn as many particles
		// as we need to
		while( m_timeElapsed >= 0.0f )
		{
			m_particles[m_nextParticleIdx].m_position.set( m_position );
			
			m_timeElapsed -= m_emissionFrequency;
		}
		
	}

}

// TODO: zanikanie - zmniejszanie sie, alpha ( ale blendy musza byc wlaczone ) 