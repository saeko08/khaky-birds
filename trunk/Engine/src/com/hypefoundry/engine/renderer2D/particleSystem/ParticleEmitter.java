/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * Emitter spawns new particles and manages their life time.
 * 
 * @author Paksas
 *
 */
public abstract class ParticleEmitter 
{
	private float 			m_emissionFrequency;
	private float			m_timeToLive;
	private int				m_numEmittedEachTick;
	
	private Particle[]		m_particles;
	private float			m_timeElapsed;
	
	/**
	 * Constructor.
	 */
	public ParticleEmitter( ) 
	{
		m_emissionFrequency = 0;
		m_timeToLive = 0;
		m_numEmittedEachTick = 0;
		m_timeElapsed = 0;
	}
	
	/**
	 * Sets how long each particle should live.
	 * 
	 * @param val
	 */
	public ParticleEmitter setTimeToLive( float time )
	{
		m_timeToLive = time;
		return this;
	}
	
	/**
	 * Sets the amount of particles this emitter can emit at most.
	 * 
	 * @param val
	 */
	public ParticleEmitter setParticlesCount( int maxParticles, ParticlesFactory factory )
	{
		m_particles = new Particle[maxParticles];
		for ( int i = 0; i < m_particles.length; ++i )
		{
			m_particles[i] = new Particle();
		}
		return this;
	}
	
	/**
	 * Sets the wait period between subsequent particle emissions.
	 * 
	 * @param val
	 */
	public ParticleEmitter setEmissionFrequency( float emissionFrequency )
	{
		m_emissionFrequency = emissionFrequency;
		return this;
	}
	
	/**
	 * Sets the amount of particles that should be emitted in each tick.
	 * 
	 * @param val
	 */
	public ParticleEmitter setAmountEmittedEachTick( int val )
	{
		m_numEmittedEachTick = val;
		return this;
	}

	/**
	 * Called when the emitter is added to the particle system
	 * 
	 * @param ps
	 */
	void onAttached( ParticleSystem ps )
	{
		for ( int i = 0; i < m_particles.length; ++i )
		{
			ps.addParticle( m_particles[i] );
		}
	}

	/**
	 * Manages the particles, spawning new ones and removing the old ones.
	 * 
	 * @param deltaTime
	 */
	void update( float deltaTime )
	{			
		int toBeReborn = 0;
		m_timeElapsed += deltaTime;
		while( m_timeElapsed >= m_emissionFrequency )
		{
			toBeReborn += m_numEmittedEachTick;
			m_timeElapsed -= m_emissionFrequency;
		}
		
		// change the remaining life time of the particles
		for ( int i = 0; i < m_particles.length; ++i )
		{
			m_particles[i].m_timeToLive -= deltaTime;
			if ( m_particles[i].m_timeToLive < 0 && toBeReborn > 0 )
			{
				m_particles[i].m_timeToLive = m_timeToLive;
				--toBeReborn;
				
				initialize( m_particles[i] );
			}
		}
	}
	
	/**
	 * Initializes the particle.
	 * 
	 * @param particle
	 * @param particle
	 */
	protected abstract void initialize( Particle particle );
	
	/**
	 * Loads the emitter definition from a stream.
	 * 
	 * @param loader
	 * @param resMgr
	 */
	void load( DataLoader loader, ResourceManager resMgr )
	{
		// load the basic parameters
		m_emissionFrequency 			= loader.getFloatValue( "frequency" );
		m_timeToLive 					= loader.getFloatValue( "timeToLive" );
		m_numEmittedEachTick			= loader.getIntValue( "amountEmittedEachTick" );
		int particlesCount				= loader.getIntValue( "totalAmount" );
		String particleType				= loader.getStringValue( "particleType" );
		ParticlesFactory factory		= ParticleSystem.findParticleFactory( particleType );
		setParticlesCount( particlesCount, factory );
		
		// initialize the particles
		for ( int i = 0; i < m_particles.length; ++i )
		{
			m_particles[i].load( loader, resMgr );
		}
		
		// load the implementation specific data
		onLoad( loader );
	}
	
	/**
	 * Loads the implementation specific emitter definition from a stream.
	 * 
	 * @param loader
	 */
	protected abstract void onLoad( DataLoader loader );
}
