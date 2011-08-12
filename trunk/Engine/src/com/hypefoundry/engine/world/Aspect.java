/**
 * 
 */
package com.hypefoundry.engine.world;

import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.util.serialization.DataSaver;


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
	 * @param loader	loader in which to look for the data
	 */
	void load( DataLoader loader );
	
	/**
	 * Saves the aspect configuration.
	 * 
	 * @param saver
	 */
	void save( DataSaver saver );
}

