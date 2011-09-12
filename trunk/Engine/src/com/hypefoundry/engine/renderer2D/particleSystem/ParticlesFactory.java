/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.util.serialization.DataLoader;


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
	
	/**
	 * Loads the particle definition from a stream.
	 * 
	 * @param loader
	 * @param resMgr
	 */
	void load( DataLoader loader, ResourceManager resMgr );
}
