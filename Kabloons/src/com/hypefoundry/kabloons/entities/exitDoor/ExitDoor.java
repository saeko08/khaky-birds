/**
 * 
 */
package com.hypefoundry.kabloons.entities.exitDoor;

import com.hypefoundry.engine.math.BoundingBox;
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
	
	String 				m_tag;
	State				m_state;
	BoundingBox			m_exitAreaBoundsWorldSpace;
	
	
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
	
	/**
	 * Returns the bounds of the exit area ( in world space ).
	 * 
	 * @return
	 */
	BoundingBox getExitAreaBounds()
	{
		return m_exitAreaBoundsWorldSpace;
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
		
		// transform the exit area bounds to the world space
		m_exitAreaBoundsWorldSpace = new BoundingBox( -0.69f, -0.2f, 0.59f, 0.67f );
		m_exitAreaBoundsWorldSpace.translate( getPosition() );
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
