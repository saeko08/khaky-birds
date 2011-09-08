/**
 * 
 */
package com.hypefoundry.engine.controllers.fsm;

import com.hypefoundry.engine.world.EntityEventListener;

/**
 * Definition of a state that's ran by a finite state machine.
 * 
 * @author Paksas
 *
 */
public abstract class FSMState 
{
	private FiniteStateMachine		m_fsm = null;
	
	/**
	 * Sets the host FSM that runs this state.
	 * 
	 * @param fsm
	 */
	public void setHostFSM( FiniteStateMachine fsm )
	{
		m_fsm = fsm;
	}
	
	/**
	 * Facility method allowing to set up state transitions from
	 * within the state code.
	 * 
	 * @param state
	 * @return 	state instance that will be activated
	 */
	protected < T extends FSMState > T transitionTonTo( Class< T > state )
	{
		if ( m_fsm != null )
		{
			return m_fsm.transitionTo( state );
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Called by the FSM when the state gets activated.
	 */
	void activateState()
	{
		if ( this instanceof EntityEventListener )
		{
			// attach self from listening events
			m_fsm.m_entity.attachEventListener( (EntityEventListener)this );
		}
		
		// notify the state implementation
		activate();
	}
	
	/**
	 * Called by the FSM when the state gets deactivated.
	 */
	void deactivateState()
	{
		if ( this instanceof EntityEventListener )
		{
			// detach self from listening events
			m_fsm.m_entity.detachEventListener( (EntityEventListener)this );
		}
		
		// notify the state implementation
		deactivate();
	}
	
	/**
	 * Called when the state is activated.
	 */
	protected void activate() {}
	
	/**
	 * Called when the state is deactivated.
	 */
	protected void deactivate() {}
	
	/**
	 * Called when the state is active and we want to execute its functionality.
	 * 
	 * @param deltaTime
	 */
	public void execute( float deltaTime ) {}
}
