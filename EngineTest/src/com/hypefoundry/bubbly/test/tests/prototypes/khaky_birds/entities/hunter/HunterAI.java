/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.hunter;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird.Bird;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.crap.Crapped;
import com.hypefoundry.engine.controllers.fsm.FSMState;
import com.hypefoundry.engine.controllers.fsm.FiniteStateMachine;
import com.hypefoundry.engine.math.MathLib;
import com.hypefoundry.engine.physics.locomotion.SteeringBehaviors;
import com.hypefoundry.engine.renderer2D.animation.AnimEvent;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityEvent;
import com.hypefoundry.engine.world.EntityEventListener;
import com.hypefoundry.engine.world.EventFactory;
import com.hypefoundry.engine.world.World;

/**
 * Hunter's AI.
 * 
 * @author azagor
 */
public class HunterAI extends FiniteStateMachine
{
	private Hunter				m_hunter;
	private SteeringBehaviors 	m_sb;
	private Bird				m_bird;
	
	private final float			MAX_AIM_TOLERANCE = 15.0f; // TODO: config
	private final float			MIN_AIM_TOLERANCE = 2.0f; // TODO: config
	
	
	// ------------------------------------------------------------------------
	
	class Aiming extends FSMState implements EntityEventListener
	{			
		@Override
		public void activate()
		{
			m_hunter.m_state = Hunter.State.Aiming;
			m_sb.clear();
			
			if ( m_bird != null )
			{
				// look at the bird, but with some tolerance
				m_sb.begin().lookAt( m_bird );
			}
		}
		
		@Override
		public void deactivate()
		{
			m_sb.clear();
		}
		
		@Override
		public void execute( float deltaTime )
		{
			if ( m_bird != null )
			{
				// look at the bird, but with some tolerance
				float angleDiff = MathLib.lookAtDiff( m_bird.getPosition(), m_hunter.getPosition(), m_hunter.getFacing() );
				if ( angleDiff < MIN_AIM_TOLERANCE )
				{
					transitionTo( Shooting.class );
				}
			}
		}

		@Override
		public void onEvent( EntityEvent event ) 
		{
			if ( event instanceof Crapped )
			{
				// a bird crapped on us
				transitionTo( Shitted.class );
			}
		}
	}
	
	// ------------------------------------------------------------------------
	
	class Shooting extends FSMState implements EntityEventListener
	{	
		@Override
		public void activate()
		{
			m_hunter.m_state = Hunter.State.Shooting;
			m_sb.clear();
		}
		
		@Override
		public void deactivate()
		{
			m_sb.clear();
		}
		
		@Override
		public void execute( float deltaTime )
		{
			if ( m_bird != null )
			{
				// keep monitoring the bird's position, because we may need to start aiming
				float angleDiff = MathLib.lookAtDiff( m_bird.getPosition(), m_hunter.getPosition(), m_hunter.getFacing() );
				
				if ( angleDiff > MAX_AIM_TOLERANCE )
				{
					transitionTo( Aiming.class );
				}
			}			
		}
		
		@Override
		public void onEvent( EntityEvent event ) 
		{
			if ( event instanceof Crapped )
			{
				// a bird crapped on us
				transitionTo( Shitted.class );
			}
			else if ( event instanceof AnimEvent )
			{
				AnimEvent animEvent = (AnimEvent)event;
				if ( animEvent.m_event instanceof Fire )
				{
					m_hunter.shoot();
				}
			}
		}
	}
	
	// ------------------------------------------------------------------------
	
	class Shitted extends FSMState
	{
		@Override
		public void activate()
		{
			m_hunter.m_state = Hunter.State.Shitted;
			// TODO: ma zejsc ze sceny
		}
		
		@Override
		public void deactivate()
		{
		}
		
		@Override
		public void execute( float deltaTime )
		{
			// TODO: jak wyjdzie za kamere, despawn
		}
	}
	
	// ------------------------------------------------------------------------
	
	/**
	 * Constructor.
	 * 
	 * @param world
	 * @param hunter			controlled hunter
	 */
	public HunterAI( World world, Entity hunter )
	{
		super( hunter );
		
		m_hunter = (Hunter)hunter;
		m_sb = new SteeringBehaviors( m_hunter );
		m_bird = (Bird) world.findEntity(Bird.class);
		
		// define events the entity responds to
		m_hunter.registerEvent( Crapped.class, new EventFactory< Crapped >() { @Override public Crapped createObject() { return new Crapped (); } } );
		m_hunter.registerEvent( AnimEvent.class, new EventFactory< AnimEvent >() { @Override public AnimEvent createObject() { return new AnimEvent (); } } );

		// setup the state machine
		register( new Aiming() );
		register( new Shooting() );
		register( new Shitted() );
		begin( Aiming.class );
	}
	
	@Override
	public void onUpdate( float deltaTime )
	{
		m_sb.update(deltaTime);
	}
}
