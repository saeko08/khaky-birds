package com.hypefoundry.engine.game;

/**
 * The view gets informed about the changes introduced to the world state.
 * 
 * @author paksas
 *
 */
public interface WorldView 
{
	/**
	 * Called whenever a new entity is added.
	 * 
	 * @param entity
	 */
	void onEntityAdded( Entity entity );
	
	/**
	 * Called whenever a new entity is removed.
	 * 
	 * @param entity
	 */
	void onEntityRemoved( Entity entity );
}
