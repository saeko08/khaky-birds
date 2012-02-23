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
	private float[]				m_particleTimeMultipliers;
	private int[]				m_emitterIndices;
	private Vector3				m_tmpVelocity	= new Vector3();
	private boolean				m_looped;
	private boolean				m_emittersActive = true;
	
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
		m_particleTimeMultipliers = new float[ m_particleSystem.m_maxParticles ];
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
	 * Enables/disables particle emitters at runtime.
	 * 
	 * @param enable
	 */
	public void enableEmitters( boolean enable )
	{
		m_emittersActive = enable;
	}
	
	/**
	 * Simulates the particle system.
	 * 
	 * @param deltaTime
	 */
	public void simulate( float deltaTime ) 
	{
		Particle particle = null;
		for ( int j = 0; j < m_particles.length; ++j )
		{
			float particleDeltaTime = m_particleTimeMultipliers[j] * deltaTime;
	
			particle = m_particles[j];
			if ( particle != null && particle.m_timeToLive > 0 )
			{
				// update the affectors
				for ( int i = 0; i < m_particleSystem.m_affectors.length; ++i )
				{
					m_particleSystem.m_affectors[i].update( particleDeltaTime, particle );
				}

				// move the particles around and update their lifetime timer			
				particle.m_timeToLive -= particleDeltaTime;
				m_tmpVelocity.set( particle.m_velocity ).scale( particleDeltaTime );
				particle.m_position.add( m_tmpVelocity );
			}
		}
		
		// reset the time multiplier for the particles
		for ( int i = 0; i < m_particles.length; ++i )
		{
			m_particleTimeMultipliers[i] = 1.0f;
		}
				
		// update the emitters
		if ( m_emittersActive )
		{
			int startIdx, endIdx;
			for ( int i = 0; i < m_particleSystem.m_emitters.length; ++i )
			{		
				startIdx = m_emitterIndices[i];
				endIdx = m_emitterIndices[i + 1];
				m_particleSystem.m_emitters[i].update( deltaTime, m_particles, m_particleTimeMultipliers, startIdx, endIdx );
				
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
