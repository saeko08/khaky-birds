/**
 * 
 */
package com.hypefoundry.engine.world;

/**
 * Entity event listener interface.
 * @author Paksas
 *
 */
public interface EntityEventListener
{
	/**
	 * Called when the even should be processed by the listener.
	 * 
	 * @param event
	 */
	void onEvent( EntityEvent event );
}
