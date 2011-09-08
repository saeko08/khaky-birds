/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem;


/**
 * A factory for creating particle emitters.
 * 
 * @author Paksas
 *
 */
public interface EmitterFactory 
{
	/**
	 * Creates a particle emitter.
	 * 
	 * @return
	 */
	ParticleEmitter create();
}
