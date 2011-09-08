/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem;


/**
 * This factory creates particles of the specific type.
 * 
 * @author Paksas
 *
 */
public interface ParticlesFactory 
{
	/**
	 * Creates a particle.
	 * 
	 * @return
	 */
	Particle create();
}
