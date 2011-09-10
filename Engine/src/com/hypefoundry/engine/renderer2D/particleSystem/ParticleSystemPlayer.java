/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;

/**
 * Runs a particle system simulation.
 * 
 * @author Paksas
 *
 */
public class ParticleSystemPlayer 
{
	private ParticleSystem		m_particleSystem;
	private Particle[]			m_particles;
	private int[]				m_emitterIndices;
	private Vector3				m_tmpVelocity	= new Vector3();
	private boolean				m_looped;
	
	/**
	 * Constructor.
	 * 
	 * @param particleSystem
	 * @param looped
	 */
	public ParticleSystemPlayer( ParticleSystem particleSystem, boolean looped )
	{
		m_particleSystem = particleSystem;
		m_looped = looped;
		
		// allocate space for particles
		m_particles = new Particle[ m_particleSystem.m_maxParticles ];
		m_emitterIndices = new int[ m_particleSystem.m_emitters.length + 1 ];
		
		// initialize the particles
		int startIdx = 0;
		int addedParticlesCount = 0;
		for ( int i = 0; i < m_particleSystem.m_emitters.length; ++i )
		{
			m_emitterIndices[i] = startIdx;
			addedParticlesCount = m_particleSystem.m_emitters[i].onPlayerAttached( m_particles, startIdx );
			startIdx += addedParticlesCount;
		}
		m_emitterIndices[ m_particleSystem.m_emitters.length ] = startIdx;
	}
	
	/**
	 * Returns the number of currently spawned particles.
	 * 
	 * @return
	 */
	public int getActiveParticles( Particle[] particles )
	{
		int count = 0;
		for ( int i = 0; i < m_particles.length; ++i )
		{
			if ( m_particles[i] != null && m_particles[i].m_timeToLive > 0 )
			{
				particles[count] = m_particles[i];
				++count;
			}
		}
		return count;
	}
	
	/**
	 * A method that allows the emitters to register the particles they create with the system.
	 */
	void addParticle( Particle particle )
	{
		for ( int i = 0; i < m_particles.length; ++i )
		{
			if ( m_particles[i] == null )
			{
				m_particles[i] = particle;
				break;
			}
		}
	}
	
	/**
	 * Simulates the particle system.
	 * 
	 * @param deltaTime
	 */
	public void simulate( float deltaTime ) 
	{
		// update the affectors
		Particle particle = null;
		for ( int j = 0; j < m_particles.length; ++j )
		{
			particle = m_particles[j];
			if ( particle != null && particle.m_timeToLive > 0 )
			{
				for ( int i = 0; i < m_particleSystem.m_affectors.length; ++i )
				{
					m_particleSystem.m_affectors[i].update( deltaTime, particle );
				}
			}
		}
		
		// move the particles around and update their lifetime timer
		for ( int i = 0; i < m_particles.length; ++i )
		{
			particle = m_particles[i];
			
			if ( particle != null && particle.m_timeToLive > 0 )
			{
				particle.m_timeToLive -= deltaTime;
				m_tmpVelocity.set( particle.m_velocity ).scale( deltaTime );
				particle.m_position.add( m_tmpVelocity );
			}
		}
				
		// update the emitters
		int startIdx, endIdx;
		for ( int i = 0; i < m_particleSystem.m_emitters.length; ++i )
		{
			startIdx = m_emitterIndices[i];
			endIdx = m_emitterIndices[i + 1];
			m_particleSystem.m_emitters[i].update( deltaTime, m_particles, startIdx, endIdx );
			
			if ( m_looped == false )
			{
				// see how many of those were already initialized, and reduce the indices accordingly
				int numInitialized = 0;
				for ( int j = startIdx; j < endIdx; ++j )
				{
					particle = m_particles[j];
					if ( particle != null && particle.m_timeToLive > 0 )
					{
						++numInitialized; 
					}
				}
				
				m_emitterIndices[i] += numInitialized;
			}
		}
	}

	/**
	 * Renders the particle system.
	 * 
	 * @param x				position in the world
	 * @param y				position in the world
	 * @param batcher
	 * @param deltaTime
	 */
	public void draw( float x, float y, SpriteBatcher batcher, float deltaTime ) 
	{
		simulate( deltaTime );
		
		// render the particles
		Particle particle = null;
		for ( int i = 0; i < m_particles.length; ++i )
		{
			particle = m_particles[i];
			if ( particle != null && particle.m_timeToLive > 0 )
			{
				particle.draw( x, y, batcher, deltaTime );
			}
		}
	}
}
