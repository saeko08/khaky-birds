/**
 * 
 */
package com.hypefoundry.engine.world;

import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * Entity event marker interface.
 * 
 * @author Paksas
 *
 */
public interface EntityEvent 
{
	/**
	 * Loads the event definition from a data stream.
	 * 
	 * @param loader
	 */
	void deserialize( DataLoader loader );
}
