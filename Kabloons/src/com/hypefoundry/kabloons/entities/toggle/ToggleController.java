/**
 * 
 */
package com.hypefoundry.kabloons.entities.toggle;

import com.hypefoundry.engine.controllers.EntityController;
import com.hypefoundry.engine.physics.events.CollisionEvent;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityEvent;
import com.hypefoundry.engine.world.EntityEventListener;
import com.hypefoundry.kabloons.entities.baloon.Baloon;

/**
 * @author Paksas
 *
 */
public class ToggleController extends EntityController implements EntityEventListener
{
	Toggle			m_toggle;
	boolean			m_canToggle;
	
	/**
	 * Constructor.
	 * 
	 * @param entity
	 */
	public ToggleController( Entity entity ) 
	{
		super( entity );
		
		m_toggle = (Toggle)entity;
		m_toggle.attachEventListener( this );
		
		m_canToggle = true;
	}


	@Override
	public void update( float deltaTime ) 
	{
		// nothing to do here
	}


	@Override
	public void onEvent( EntityEvent event ) 
	{
		if ( m_canToggle == false || m_toggle.m_controlledEntity == null )
		{
			return;
		}
		
		if ( event instanceof CollisionEvent && ((CollisionEvent)event).m_collider instanceof Baloon )
		{
			m_toggle.m_controlledEntity.toggle();
				
			// make sure the toggle can be switched only once
			m_canToggle = false;
		}
	}
}

