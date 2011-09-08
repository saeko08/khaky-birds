/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem;

/**
 * Emitter spawns new particles and manages their life time.
 * 
 * @author Paksas
 *
 */
public interface ParticleEmitter 
{
	/**
	 * Manages the particles, spawning new ones and removing the old ones.
	 * 
	 * @param deltaTime
	 */
	void update( float deltaTime );
}
