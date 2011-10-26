/**
 * 
 */
package com.hypefoundry.engine.game;


/**
 * Utility capable of updating updatable objects.
 * 
 * @author paksas
 *
 */
public interface UpdatesManager 
{
	/**
	 * Adds a new updatable object.
	 * 
	 * @param updatable
	 */
	void addUpdatable( Updatable updatable );
	
	/**
	 * Removes an updatable object.
	 * 
	 * @param updatable
	 */
	void removeUpdatable( Updatable updatable );
	
	/**
	 * Pauses/unpauses the manager.
	 * 
	 * @param enable
	 */
	void pause( boolean enable );
}
