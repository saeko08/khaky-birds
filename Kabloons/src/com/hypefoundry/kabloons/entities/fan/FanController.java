/**
 * 
 */
package com.hypefoundry.kabloons.entities.fan;

import com.hypefoundry.engine.controllers.fsm.FSMState;
import com.hypefoundry.engine.controllers.fsm.FiniteStateMachine;
import com.hypefoundry.engine.physics.events.CollisionEvent;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityEvent;
import com.hypefoundry.engine.world.EntityEventListener;
import com.hypefoundry.kabloons.entities.baloon.Baloon;


/**
 * @author Paksas
 *
 */
public class FanController extends FiniteStateMachine 
{
	private Fan			m_fan;
	
	// ------------------------------------------------------------------------
	// States
	// ------------------------------------------------------------------------
	
	/**
	 * Fan is switched on.
	 * @author Paksas
	 *
	 */
	class Active extends FSMState implements EntityEventListener
	{			
		
		@Override
		public void onEvent( EntityEvent event ) 
		{
			if ( event instanceof CollisionEvent )
			{
				if ( ((CollisionEvent)event).m_collider instanceof Baloon )
				{
					Baloon baloon = (Baloon)((CollisionEvent)event).m_collider;
					baloon.applyForce( m_fan.m_blowForce );
				}
			}
		}
	}
	
	
	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------
			
	/**
	 * Constructor.
	 * 
	 * @param fanEntity
	 */
	public FanController( Entity fanEntity ) 
	{
		super( fanEntity );

		m_fan = (Fan)fanEntity;
		
		// register states
		register( new Active() );
		begin( Active.class );
	}

}
