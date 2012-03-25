/**
 * 
 */
package com.hypefoundry.kabloons.entities.buzzSaw;

import com.hypefoundry.engine.physics.events.CollisionEvent;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityEvent;
import com.hypefoundry.engine.world.EntityEventListener;
import com.hypefoundry.kabloons.entities.toggle.Toggled;

/**
 * @author Paksas
 *
 */
public class BuzzSaw extends Entity implements EntityEventListener, Toggled
{
	enum State
	{
		Running,
		SwitchedOff
	}
	
	public String			m_animPath;
	public String			m_fxPath;
	String					m_tag;
	State					m_state;
	
	/**
	 * Constructor.
	 * 
	 * @param assetsFactory
	 */
	public BuzzSaw()
	{		
		m_state = State.Running;
		
		// attach an event listener
		attachEventListener( this );
	}
	
	@Override
	public void onLoad( DataLoader loader )
	{
		m_tag = loader.getStringValue( "tag" );
		m_animPath = loader.getStringValue( "animPath" );
		m_fxPath = loader.getStringValue( "fx" );
	}
	
	@Override
	public void onEvent( EntityEvent event ) 
	{
		if ( event instanceof CollisionEvent && m_state == State.Running )
		{
			((CollisionEvent)event).m_collider.sendEvent( Destroy.class );
		}
	}
	
	@Override
	public boolean isSwitchedOn() 
	{
		return m_state == State.Running;
	}

	@Override
	public void toggle() 
	{
		switch( m_state )
		{
			case Running:
			{
				m_state = State.SwitchedOff;
				break;
			}
			
			case SwitchedOff:
			{
				m_state = State.Running;
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