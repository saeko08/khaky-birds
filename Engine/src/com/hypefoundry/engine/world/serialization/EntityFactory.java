/**
 * 
 */
package com.hypefoundry.engine.world.serialization;

import com.hypefoundry.engine.world.Entity;

/**
 * A factory that will instantiate entities when a world representation
 * is laded from a file.
 * 
 * @author piotr.trochim
 *
 */
public interface EntityFactory
{
	/**
	 * Creates an entity.
	 * 
	 * @return
	 */
	Entity create();
}
