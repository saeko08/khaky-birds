/**
 * 
 */
package com.hypefoundry.kabloons.entities.baloon;

import com.hypefoundry.engine.controllers.fsm.FSMState;
import com.hypefoundry.engine.controllers.fsm.FiniteStateMachine;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.physics.events.OutOfWorldBounds;
import com.hypefoundry.engine.physics.locomotion.SteeringBehaviors;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityEvent;
import com.hypefoundry.engine.world.EntityEventListener;
import com.hypefoundry.engine.world.EventFactory;
import com.hypefoundry.kabloons.entities.buzzSaw.Destroy;
import com.hypefoundry.kabloons.entities.exitDoor.SavedEvent;


/**
 * @author Paksas
 *
 */
public class BaloonController extends FiniteStateMachine 
{

	private Baloon 				m_baloon;
	private SteeringBehaviors	m_sb;
	
	// ------------------------------------------------------------------------
	// States
	// ------------------------------------------------------------------------
	
	/**
	 * Baloon is flying, driven by physical forces
	 * @author Paksas
	 *
	 */
	class Flying extends FSMState implements EntityEventListener
	{		
		private Vector3 	m_tmpFlightGoal = new Vector3();
		
		@Override
		public void activate()
		{
			// update baloon's state
			m_baloon.m_state = Baloon.State.Flying;
		}
		
		@Override
		public void deactivate()
		{
			m_sb.clear();
		}
		
		@Override
		public void execute( float deltaTime )
		{
			// always fly up
			m_tmpFlightGoal.set( m_baloon.getPosition() );
			m_tmpFlightGoal.add( 0, 1.0f, 0 );
			m_sb.begin().seek( m_tmpFlightGoal );
		}

		@Override
		public void onEvent( EntityEvent event ) 
		{
			if ( event instanceof SavedEvent )
			{
				transitionTo( Safe.class );
			}
			else if ( event instanceof Destroy )
			{
				transitionTo( Dead.class );
			}
			else if ( event instanceof OutOfWorldBounds )
			{
				if ( m_baloon.getPosition().m_y > 1.0f )
				{
					// baloon flew out of world bounds - it's a goner
					transitionTo( Dead.class );
				}
			}
		}
	}
	
	/**
	 * Baloon exploded.
	 * @author Paksas
	 *
	 */
	class Dead extends FSMState
	{		
		@Override
		public void activate()
		{
			// update baloon's state
			m_baloon.m_state = Baloon.State.Dead;
			
			// disable baloon's movement
			m_baloon.query( DynamicObject.class ).m_linearSpeed = 0.0f;
		}
		
		@Override
		public void deactivate()
		{
		}
		
		@Override
		public void execute( float deltaTime )
		{
		}
	}
	
	/**
	 * Baloon got to safety.
	 * @author Paksas
	 *
	 */
	class Safe extends FSMState
	{			
		@Override
		public void activate()
		{
			// update baloon's state
			m_baloon.m_state = Baloon.State.Safe;
			
			// disable baloon's movement
			m_baloon.query( DynamicObject.class ).m_linearSpeed = 0.0f;
		}
	}
	
	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------
		
	/**
	 * Constructor.
	 * 
	 * @param baloonEntity
	 */
	public BaloonController( Entity baloonEntity ) 
	{
		super( baloonEntity );
		
		m_baloon = (Baloon)baloonEntity;
		m_sb = new SteeringBehaviors( m_baloon );
		
		// register states
		register( new Flying() );
		register( new Dead() );
		register( new Safe() );
		begin( Flying.class );
		
		// register events we're interested in
		m_baloon.registerEvent( SavedEvent.class, new EventFactory< SavedEvent >() { @Override public SavedEvent createObject() { return new SavedEvent(); } } );
		m_baloon.registerEvent( OutOfWorldBounds.class, new EventFactory< OutOfWorldBounds >() { @Override public OutOfWorldBounds createObject() { return new OutOfWorldBounds(); } } );
		m_baloon.registerEvent( Destroy.class, new EventFactory< Destroy >() { @Override public Destroy createObject() { return new Destroy(); } } );
	}

	@Override
	public void onUpdate( float deltaTime )
	{
		m_sb.update(deltaTime);
	}
}
