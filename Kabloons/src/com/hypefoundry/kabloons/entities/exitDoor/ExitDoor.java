/**
 * 
 */
package com.hypefoundry.kabloons.entities.exitDoor;

import com.hypefoundry.engine.physics.events.CollisionEvent;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityEvent;
import com.hypefoundry.engine.world.EntityEventListener;
import com.hypefoundry.kabloons.entities.toggle.Toggled;
import com.hypefoundry.kabloons.utils.AssetsFactory;


/**
 * @author Paksas
 *
 */
public class ExitDoor extends Entity implements EntityEventListener, Toggled
{
	enum State
	{
		Open,
		Closed
	}
	
	public String		m_openDoorTexturePath;
	public String		m_closedDoorTexturePath;
	String 				m_tag;
	State				m_state;
	
	/**
	 * Constructor.
	 * 
	 * @param assetsFactory
	 */
	public ExitDoor( AssetsFactory assetsFactory )
	{
		// attach an event listener
		attachEventListener( this );
		
		m_state = State.Open;
	
		assetsFactory.initializeDoor( this );
	}

	@Override
	public void onLoad( DataLoader loader ) 
	{		
		m_tag = loader.getStringValue( "tag" );
		
		String stateStr = loader.getStringValue( "state" );
		if ( stateStr.length() > 0 )
		{
			m_state = State.valueOf( stateStr );		
		}
	}

	@Override
	public void onEvent( EntityEvent event ) 
	{
		if ( event instanceof CollisionEvent && m_state == State.Open )
		{
			((CollisionEvent)event).m_collider.sendEvent( SavedEvent.class );
		}
	}

	@Override
	public boolean isSwitchedOn() 
	{
		return m_state == State.Open;
	}

	@Override
	public void toggle() 
	{
		switch( m_state )
		{
			case Open: 
			{
				m_state = State.Closed; 
				break; 
			}
			
			case Closed: 
			{
				m_state = State.Open; 
				break; 
			}
		}
	}
	
	@Override
	public String getTag()
	{
		return m_tag;
	}
}
