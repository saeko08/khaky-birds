package com.hypefoundry.engine.world;

/**
 * The view gets informed about the changes introduced to the world state.
 * 
 * @author paksas
 *
 */
public interface WorldView 
{
	/**
	 * Called when the view is attached to the world.
	 *  
	 * @param world
	 */
	void onAttached( World world );
	
	/**
	 * Called when the view is detached to the world.
	 *  
	 * @param world
	 */
	void onDetached( World world );
	
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
