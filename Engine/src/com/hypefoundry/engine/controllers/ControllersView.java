/**
 * 
 */
package com.hypefoundry.engine.controllers;

import java.util.*;

import android.util.Log;

import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.game.UpdatesManager;
import com.hypefoundry.engine.world.World;
import com.hypefoundry.engine.world.WorldView;
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
			
			// memorize the new controller
			m_controllers.add( controller );
			
			// add it to the update manager
			m_updatesMgr.addUpdatable( controller );
		}
		catch( IndexOutOfBoundsException e )
		{
			// ups... - no controller is defined - notify about it
			Log.d( "ControllersView", "Controller not defined for entity '" + entity.getClass().getName() + "'" );
		}
	}

	@Override
	public void onEntityRemoved( Entity entity ) 
	{
		EntityController controller = findControllerFor( entity );
		if ( controller != null )
		{
			// remove it from the update manager
			m_updatesMgr.removeUpdatable( controller );
			
			// remove it from the collection
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
		int count = m_controllers.size();
		for ( int i = 0; i < count; ++i )
		{
			EntityController controller = m_controllers.get(i);
			if ( controller.isControllerOf( entity ) )
			{
				return controller;
			}
		}
		
		return null;
	}

	@Override
	public void onAttached( World world ) 
	{
		// nothing to do here	
	}

	@Override
	public void onDetached( World world ) 
	{
		// nothing to do here
	}
}
