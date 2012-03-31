/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem;

/**
 * @author Paksas
 *
 */
public interface ParticleSystemListener 
{
	/**
	 * Called when the system the listener observes gets initialized.
	 * And indication that it's time to create the corresponding runtime data representation.
	 */
	void onSystemInitialized();
	
	/**
	 * Called when the data managed by the observed particle system
	 * is released, and is a sign that the observer should also
	 * reset its temporary data. 
	 */
	void onSystemReleased();
}
