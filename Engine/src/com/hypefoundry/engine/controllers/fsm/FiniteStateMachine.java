/**
 * 
 */
package com.hypefoundry.engine.controllers.fsm;

import java.util.*;

import com.hypefoundry.engine.controllers.EntityController;
import com.hypefoundry.engine.world.Entity;

/**
 * Finite state machine controller.
 * 
 * @author Paksas
 *
 */
public class FiniteStateMachine extends EntityController
{
	private List< FSMState >		m_states;
	FSMState						m_stateToActivate;
	FSMState						m_activeState;
	
	/**
	 * Constructor.
	 * 
	 * @param entity		controlled entity
	 */
	public FiniteStateMachine( Entity entity ) 
	{
		super( entity );
		
		m_states = new ArrayList< FSMState >();
		m_stateToActivate = null;
		m_activeState = null;
	}

	@Override
	public void update( float deltaTime )
	{
		if ( m_stateToActivate != null )
		{
			// we want to activate a state - deactivate the old state
			if ( m_activeState != null )
			{
				m_activeState.deactivate();
			}
			
			// activate the new state
			m_activeState = m_stateToActivate;
			if ( m_activeState != null )
			{
				m_activeState.activate();
			}
		}
		
		// execute the state's functionality
		if ( m_activeState != null )
		{
			m_activeState.execute( deltaTime );
		}
		
		onUpdate( deltaTime );
	}

	/**
	 * Registers a new state for the machine to operate on.
	 *   
	 * @param newState
	 */
	public final void register( FSMState newState ) 
	{
		// make sure the state doesn't exist already - if it does, replace it
		int count = m_states.size();
		for( int i = 0; i < count; ++i )
		{
			FSMState state = m_states.get(i);
			if ( state.getClass().isInstance(  newState ) )
			{
				// ok - a state like that exists, replace it
				m_states.set( i, newState );
				return;
			}
		}
		
		// this is a completely new kind of state - add it
		m_states.add( newState );
	}
	
	/**
	 * Defines an entry point to the machine - a state the machine 
	 * should start processing first.
	 * 
	 * @param 	state
	 * @return 	state instance that will be activated 
	 */
	@SuppressWarnings("unchecked")
	public final < T extends FSMState  > T begin( Class< T > state )
	{
		m_stateToActivate = findState( state );
		return (T)m_stateToActivate;
	}
	
	/**
	 * Transitions the machine to the specified state.
	 * 
	 * @param 	state
	 * @return 	state instance that will be activated
	 */
	@SuppressWarnings("unchecked")
	public < T extends FSMState  > T transitionTo( Class< T  > state ) 
	{
		FSMState newState = findState( state );
		if ( newState != null )
		{
			// transition only if the specified state exists
			m_stateToActivate = newState;
			return (T)m_stateToActivate;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Looks for the specified state.
	 * 
	 * @param state
	 * @return
	 */
	private FSMState findState( Class< ? extends FSMState > state )
	{
		for( FSMState s : m_states )
		{
			if ( state.isInstance( s ) )
			{
				return s;
			}
		}
		
		return null;
	}
	
	// ------------------------------------------------------------------------
	// Notifications
	// ------------------------------------------------------------------------
	
	/**
	 * Called during the update. Allows you to do some of your own update stuff.
	 * 
	 * @param deltaTime
	 */
	public void onUpdate( float deltaTime ) {}
}

