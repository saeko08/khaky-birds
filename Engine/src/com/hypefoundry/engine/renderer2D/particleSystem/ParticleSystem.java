/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem;

import com.hypefoundry.engine.game.Updatable;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.util.Arrays;

/**
 * A tool for creating visual effects that involve may small bitmaps moving around.
 * 
 * @author Paksas
 *
 */
public class ParticleSystem
{
	private final int 			m_maxParticles;
	
	private ParticleEmitter[] 	m_emitters 		= new ParticleEmitter[0];
	private ParticleAffector[] 	m_affectors 	= new ParticleAffector[0];
	Particle[]					m_particles;
	
	/**
	 * Constructor.
	 * 
	 * @param maxParticles
	 */
	public ParticleSystem( int maxParticles )
	{
		m_maxParticles = maxParticles;
		m_particles = new Particle[m_maxParticles];
	}
	
	/**
	 * Adds a new particle emitter.
	 * 
	 * @param emitter
	 */
	public void addEmitter( ParticleEmitter emitter )
	{
		if ( emitter != null )
		{
			m_emitters = Arrays.append( m_emitters, emitter );
		}
	}
	
	/**
	 * Adds a new particle emitter.
	 * 
	 * @param emitter
	 */
	public void addAffector( ParticleAffector affector )
	{
		if ( affector != null )
		{
			m_affectors = Arrays.append( m_affectors, affector );
		}
	}
	
	/**
	 * Returns the number of currently spawned particles.
	 * 
	 * @return
	 */
	public int getParticlesCount()
	{
		int count = 0;
		for ( int i = 0; i < m_particles.length; ++i )
		{
			if ( m_particles[i] != null )
			{
				++count;
			}
		}
		return count;
	}
	
	/**
	 * Simulates the particle system.
	 * 
	 * @param deltaTime
	 */
	public void simulate( float deltaTime ) 
	{
		// update the emitters
		for ( int i = 0; i < m_emitters.length; ++i )
		{
			m_emitters[i].update( deltaTime );
		}
		
		// update the affectors
		for ( int i = 0; i < m_emitters.length; ++i )
		{
			m_emitters[i].update( deltaTime );
		}
	}

	/**
	 * Renders the particle system.
	 * 
	 * @param batcher
	 * @param deltaTime
	 */
	public void draw( SpriteBatcher batcher, float deltaTime ) 
	{
		simulate( deltaTime );
		
		// render the particles
		Particle particle = null;
		for ( int i = 0; i < m_particles.length; ++i )
		{
			particle = m_particles[i];
			if ( particle != null )
			{
				batcher.drawSprite( particle.m_position.m_x, particle.m_position.m_y, particle.m_width, particle.m_height, particle.m_orientation, particle.m_textureRegion );
			}
		}
	}
}
