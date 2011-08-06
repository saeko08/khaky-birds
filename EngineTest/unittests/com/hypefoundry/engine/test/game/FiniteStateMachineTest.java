package com.hypefoundry.engine.test.game;

import com.hypefoundry.engine.controllers.fsm.FSMState;
import com.hypefoundry.engine.controllers.fsm.FiniteStateMachine;
import com.hypefoundry.engine.world.Entity;

import android.test.AndroidTestCase;


public class FiniteStateMachineTest  extends AndroidTestCase 
{
	
	class EntityMock extends Entity
	{
	}
	
	// ------------------------------------------------------------------------

	public void testStateExecution()
	{
		// setup
		EntityMock entity = new EntityMock();
		FiniteStateMachine stateMachine = new FiniteStateMachine( entity );
		StringBuilder log = new StringBuilder();
		
		class MockState extends FSMState
		{
			StringBuilder 			m_log;
			
			MockState( StringBuilder log ) 
			{
				m_log = log;
			}
			
			@Override
			public void activate()
			{
				m_log.append( "activating;" );
			}
			
			@Override
			public void deactivate()
			{
				m_log.append( "deactivating;" );
			}
			
			@Override
			public void execute( float deltaTime )
			{
				m_log.append( "executing;" );
			}
		}
		
		stateMachine.register( new MockState( log ) );
		stateMachine.begin( MockState.class );
		String result = log.toString();
		assertTrue( result.equals( "" ) );
		
		// run the machine
		stateMachine.update(0);
		result = log.toString();
		assertTrue( result.equals( "activating;executing;" ) );
		
	}
	
	public void testStateSwitching()
	{
		// setup
		EntityMock entity = new EntityMock();
		final FiniteStateMachine stateMachine = new FiniteStateMachine( entity );
		StringBuilder log = new StringBuilder();
		
		class WanderState extends FSMState
		{
			StringBuilder 			m_log;
			
			WanderState( StringBuilder log ) 
			{
				m_log = log;
			}
			
			@Override
			public void activate()
			{
				m_log.append( "activating wandering;" );
			}
			
			@Override
			public void deactivate()
			{
				m_log.append( "deactivating wandering;" );
			}
			
			@Override
			public void execute( float deltaTime )
			{
				m_log.append( "wandering;" );
			}
		}
		
		class ObserveState extends FSMState
		{
			StringBuilder 			m_log;
			
			ObserveState( StringBuilder log ) 
			{
				m_log = log;
			}
			
			@Override
			public void activate()
			{
				m_log.append( "activating observing;" );
			}
			
			@Override
			public void deactivate()
			{
				m_log.append( "deactivating observing;" );
			}
			
			@Override
			public void execute( float deltaTime )
			{
				m_log.append( "observing;" );
				stateMachine.transitionTo( WanderState.class );
			}
		}
		
		stateMachine.register( new WanderState( log ) );
		stateMachine.register( new ObserveState( log ) );
		stateMachine.begin( ObserveState.class );
		
		// run the machine
		stateMachine.update(0);
		String result = log.toString();
		assertTrue( result.equals( "activating observing;observing;" ) );

		stateMachine.update(0);
		result = log.toString();
		assertTrue( result.equals( "activating observing;observing;deactivating observing;activating wandering;wandering;" ) );
		
	}
}
