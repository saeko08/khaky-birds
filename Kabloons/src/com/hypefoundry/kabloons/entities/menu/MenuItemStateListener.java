/**
 * 
 */
package com.hypefoundry.kabloons.entities.menu;

/**
 * Listens to the menu item's state changes.
 * 
 * @author Paksas
 */
public interface MenuItemStateListener 
{
	/**
	 * Called when the item's state changes.
	 * @param newState
	 */
	void onStateChanged( MenuItem.State newState );
}
