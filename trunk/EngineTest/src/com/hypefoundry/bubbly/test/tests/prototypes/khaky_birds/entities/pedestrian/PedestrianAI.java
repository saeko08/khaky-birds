/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.pedestrian;


import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.crap.Crapped;
import com.hypefoundry.engine.controllers.fsm.FiniteStateMachine;
import com.hypefoundry.engine.controllers.fsm.FSMState;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.physics.locomotion.SteeringBehaviors;
import com.hypefoundry.engine.physics.events.OutOfWorldBounds;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityEvent;
import com.hypefoundry.engine.world.EntityEventListener;
import com.hypefoundry.engine.world.EventFactory;


/**
 * AI of a pedestrian.
 * 
 * @author paksas
 *
 */
public class PedestrianAI extends FiniteStateMachine
{
	private Pedestrian			m_pedestrian;
	private SteeringBehaviors 	m_sb;

	// ------------------------------------------------------------------------
	
	class Wander extends FSMState implements EntityEventListener
	{	
		private Vector3 m_tmpDirVec 		= new Vector3();
		
		@Override
		public void activate()
		{
			// register events listeners
			m_pedestrian.attachEventListener( this );
			
			m_sb.begin().wander().faceMovementDirection();

		}
		
		@Override
		public void deactivate()
		{
			m_sb.clear();
			
			// remove events listeners
			m_pedestrian.detachEventListener( this );
		}

		@Override
		public void onEvent( EntityEvent event ) 
		{
			if ( event instanceof Crapped )
			{
				// a bird crapped on us
				m_pedestrian.setHitWithShit( true );
				transitionTo( Observe.class );
			}
			else if ( event instanceof OutOfWorldBounds )
			{					
				OutOfWorldBounds oowb = (OutOfWorldBounds)event;
				
				DynamicObject dynObject = m_pedestrian.query( DynamicObject.class );
				oowb.reflectVector( m_tmpDirVec, dynObject.m_velocity );
				m_tmpDirVec.normalize2D().add( m_pedestrian.getPosition() );
				
				transitionTo( TurnAround.class ).m_safePos.set( m_tmpDirVec );
				
			}
		}
	}
	
	// ------------------------------------------------------------------------
	
	class TurnAround extends FSMState
	{	
		private Vector3 m_safePos 		= new Vector3();
		
		
		@Override
		public void activate()
		{
			m_sb.begin().seek( m_safePos ).faceMovementDirection();
		}
		
		@Override
		public void deactivate()
		{
			m_sb.clear();
		}
		
		@Override
		public void execute( float deltaTime )
		{
			Vector3 currPos = m_pedestrian.getPosition();
			float distSqToGoal = currPos.distSq2D( m_safePos );
			if ( distSqToGoal < 1e-1 )
			{
				transitionTo( Wander.class );
			}				
		}
	}
	
	// ------------------------------------------------------------------------
	
	class Observe extends FSMState
	{
		private float 				m_wait 			= 0.f;
		
		@Override
		public void activate()
		{
			m_wait = 10.0f;
		}
		
		@Override
		public void execute( float deltaTime )
		{
			m_wait -= deltaTime;
			if ( m_wait > 0 )
			{
				transitionTo( Wander.class );
			}				
		}
	}
	
	// ------------------------------------------------------------------------
	
	/**
	 * Constructor.
	 * 
	 * @param world
	 * @param pedestrian			controlled pedestrian
	 */
	public PedestrianAI( Entity pedestrian )
	{
		super( pedestrian );
		
		m_pedestrian = (Pedestrian)pedestrian;
		m_sb = new SteeringBehaviors( m_pedestrian );
		
		// define events the entity responds to
		m_pedestrian.registerEvent( Crapped.class, new EventFactory< Crapped >() { @Override public Crapped createObject() { return new Crapped (); } } );
		m_pedestrian.registerEvent( OutOfWorldBounds.class, new EventFactory< OutOfWorldBounds >() { @Override public OutOfWorldBounds createObject() { return new OutOfWorldBounds (); } } );

		// setup the state machine
		register( new Wander() );
		register( new TurnAround() );
		register( new Observe() );
		begin( Wander.class );
	}
	
	@Override
	public void onUpdate( float deltaTime )
	{
		m_sb.update(deltaTime);
	}
}
