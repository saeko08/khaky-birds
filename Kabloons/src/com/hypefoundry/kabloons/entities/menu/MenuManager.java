/**
 * 
 */
package com.hypefoundry.kabloons.entities.menu;

import java.util.*;

import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public class MenuManager extends Entity
{
	List< MenuItem >		m_managedItems = new ArrayList< MenuItem >();

	/**
	 * Adds a new item that will be managed by this manager.
	 * 
	 * @param menuItem
	 */
	public void addItem( MenuItem menuItem ) 
	{
		m_managedItems.add( menuItem );
		
		// set the common initial position
		menuItem.setPosition( MenuManagerController.MENU_RING_CENTER );
	}

}
