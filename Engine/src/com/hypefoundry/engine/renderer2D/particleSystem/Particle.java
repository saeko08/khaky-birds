/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.util.serialization.DataLoader;

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
	
	/**
	 * Draws the particle.
	 * 
	 * @param batcher
	 * @param deltaTime
	 */
	protected void draw( SpriteBatcher batcher, float deltaTime ) {}
	
	/**
	 * Loads the particle definition from a stream.
	 * 
	 * @param loader
	 * @param resMgr
	 */
	protected void load( DataLoader loader, ResourceManager resMgr ) {}
}
