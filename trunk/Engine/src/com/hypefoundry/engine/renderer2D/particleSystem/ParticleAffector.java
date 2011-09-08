/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem;

import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * Affector changes the way the particles move around and behave.
 * 
 * @author Paksas
 *
 */
public interface ParticleAffector 
{
	/**
	 * Moves the particles around.
	 * 
	 * @param deltaTime
	 * @param particles			particles to animate
	 */
	void update( float deltaTime, Particle particle );
	
	/**
	 * Loads the affector definition from a stream.
	 * 
	 * @param loader
	 */
	void load( DataLoader loader );
}
