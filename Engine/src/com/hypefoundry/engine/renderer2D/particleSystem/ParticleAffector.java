/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem;

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
	 */
	void update( float deltaTime );
}
