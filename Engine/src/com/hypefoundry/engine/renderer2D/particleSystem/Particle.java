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
	public float			m_width = 0;
	public float			m_height = 0;
	protected float			m_timeToLive = 0;
	
	// movement parameters
	public Vector3			m_velocity = new Vector3();
	
	/**
	 * Draws the particle.
	 * 
	 * @param x				position in the world
	 * @param y				position in the world
	 * @param batcher
	 * @param deltaTime
	 */
	protected void draw( float x, float y, SpriteBatcher batcher, float deltaTime ) {}
}
