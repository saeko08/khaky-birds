/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;

/**
 * A single particle.
 * 
 * @author Paksas
 *
 */
public class Particle 
{
	public Vector3			m_position = new Vector3();
	public float			m_orientation = 0;
	private float 			m_initialTimeToLive = 0;
	protected float			m_timeToLive = 0;
	public float			m_scale = 1.0f;
	
	// movement parameters
	public Vector3			m_velocity = new Vector3();
	
	/**
	 * Informs a particle that it's jst been initialized. 
	 */
	public void onInitialized() {}
	
	/**
	 * Draws the particle.
	 * 
	 * @param x				position in the world
	 * @param y				position in the world
	 * @param batcher
	 * @param deltaTime
	 */
	protected void draw( float x, float y, SpriteBatcher batcher, float deltaTime ) {}
	
	/**
	 * Initializes particle's lifetime timer.
	 * 
	 * @param time
	 */
	public void setTimeToLive( float time )
	{
		m_initialTimeToLive = time;
		m_timeToLive = time;
	}
	
	/**
	 * Returns the current lifetime expressed in [0..1] range, where 1 represents a newborn particle,
	 * and 0 stands for a particle that's about to die.
	 *  
	 * @return
	 */
	public final float getLifetimePercent()
	{
		if ( m_initialTimeToLive <= 0.0f )
		{
			return 0.0f;
		}
		else
		{
			return m_timeToLive / m_initialTimeToLive;
		}
	}
}
