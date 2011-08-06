/**
 * 
 */
package com.hypefoundry.engine.controllers.fsm;

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
	protected < T extends FSMState > T transitTo( Class< T > state )
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
	 * Called when the state is activated.
	 */
	public void activate() {}
	
	/**
	 * Called when the state is deactivated.
	 */
	public void deactivate() {}
	
	/**
	 * Called when the state is active and we want to execute its functionality.
	 * 
	 * @param deltaTime
	 */
	public void execute( float deltaTime ) {}
}
