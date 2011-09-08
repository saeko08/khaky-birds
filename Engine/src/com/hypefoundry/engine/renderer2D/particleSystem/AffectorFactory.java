/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem;

/**
 *A factory for creating particle affectors.
 * 
 * @author Paksas
 */
public interface AffectorFactory 
{
	/**
	 * Creates a particle affector.
	 * 
	 * @return
	 */
	ParticleAffector create();
}
