/**
 * 
 */
package com.hypefoundry.kabloons.entities.exitDoor;

import com.hypefoundry.engine.physics.events.CollisionEvent;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityEvent;
import com.hypefoundry.engine.world.EntityEventListener;


/**
 * @author Paksas
 *
 */
public class ExitDoor extends Entity implements EntityEventListener
{
	String			m_path;
	
	/**
	 * Constructor.
	 */
	public ExitDoor()
	{
		// attach an event listener
		attachEventListener( this );
	}

	@Override
	public void onLoad( DataLoader loader ) 
	{
		m_path = loader.getStringValue( "path" );
	}

	@Override
	public void onEvent( EntityEvent event ) 
	{
		if ( event instanceof CollisionEvent )
		{
			((CollisionEvent)event).m_collider.sendEvent( SavedEvent.class );
		}
	}
}
