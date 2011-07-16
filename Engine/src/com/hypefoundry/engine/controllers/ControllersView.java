/**
 * 
 */
package com.hypefoundry.engine.controllers;

import java.util.*;

import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.game.UpdatesManager;
import com.hypefoundry.engine.game.WorldView;
import com.hypefoundry.engine.util.GenericFactory;

/**
 * This view will automatically instantiate controllers
 * for particular entities populating to the world.
 * 
 * @author paksas
 *
 */
public class ControllersView extends GenericFactory< Entity, EntityController > implements WorldView 
{
	private UpdatesManager 		m_updatesMgr;
	List< EntityController >	m_controllers;
	
	/**
	 * Constructor.
	 */
	public ControllersView( UpdatesManager updatesMgr ) 
	{
		m_updatesMgr = updatesMgr;
		
		m_controllers = new ArrayList< EntityController >();
	}

	@Override
	public void onEntityAdded( Entity entity ) 
	{
		EntityController controller = findControllerFor( entity );
		if ( controller != null )
		{
			// the entity already has a controller assigned
			return;
		}
		
		try
		{
			controller = create( entity );
		}
		catch( IndexOutOfBoundsException e )
		{
			// ups... - no controller is defined - notify about it
			throw new RuntimeException( "Controller not defined for entity '" + entity.getClass().getName() + "'" );
		}

		// memorize the new controller
		m_controllers.add( controller );
	}

	@Override
	public void onEntityRemoved( Entity entity ) 
	{
		EntityController controller = findControllerFor( entity );
		if ( controller != null )
		{
			m_controllers.remove( controller );
		}
	}
	
	/**
	 * Looks for a registered controller for the specified entity
	 * 
	 * @param entity
	 * @return
	 */
	private EntityController findControllerFor( Entity entity )
	{
		for ( EntityController controller : m_controllers )
		{
			if ( controller.isControllerOf( entity ) )
			{
				return controller;
			}
		}
		
		return null;
	}
}
