/**
 * 
 */
package com.hypefoundry.engine.world;

import com.hypefoundry.engine.world.serialization.WorldFileLoader;

/**
 * An aspect contains additional informations about an entity's state.
 * 
 * I.e. a DynamicObject aspect contains state info such as entity's
 * velocity and acceleration.
 * 
 * @author Paksas
 *
 */
public interface Aspect 
{
	// ------------------------------------------------------------------------
	// Serialization support
	// ------------------------------------------------------------------------
	/**
	 * Configures the vector based on the config file node's contents.
	 * 
	 * @param parentNode	parent node in which to look for the data
	 */
	void load( WorldFileLoader parentNode );
}

