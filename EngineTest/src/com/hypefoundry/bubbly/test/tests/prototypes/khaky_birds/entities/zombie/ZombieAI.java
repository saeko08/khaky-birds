/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.zombie;

import java.util.Random;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird.Bird;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.crap.Crapped;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.hunter.Shot;
import com.hypefoundry.engine.controllers.fsm.FSMState;
import com.hypefoundry.engine.controllers.fsm.FiniteStateMachine;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.physics.events.CollisionEvent;
import com.hypefoundry.engine.physics.events.OutOfWorldBounds;
import com.hypefoundry.engine.physics.locomotion.SteeringBehaviors;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityEvent;
import com.hypefoundry.engine.world.EntityEventListener;
import com.hypefoundry.engine.world.EventFactory;
import com.hypefoundry.engine.world.World;

/**
 * @author azagor
 *
 */
public class ZombieAI extends FiniteStateMachine

{

	private Zombie			m_zombie;
	private SteeringBehaviors 	m_sb;
	private World 				m_world;

	// ------------------------------------------------------------------------
	
	class Wander extends FSMState implements EntityEventListener
	{	
		private Vector3 m_tmpDirVec 		= new Vector3();
		private Random m_randObserveChnce   = new Random();
		private int m_toObserveChance       = 0;
		
		@Override
		public void activate()
		{
			m_zombie.m_state = Zombie.State.Wander;
			m_sb.begin().wander().faceMovementDirection();
		}
		
		@Override
		public void deactivate()
		{
			m_sb.clear();
		}
		
		@Override
		public void execute( float deltaTime )
		{
			//m_walkingTime     = m_walkingTime + deltaTime;
			m_toObserveChance = m_randObserveChnce.nextInt(10);
			
			if ( m_toObserveChance == 5 )
			{
				transitionTo( Observe.class );
			}
		}

		@Override
		public void onEvent( EntityEvent event ) 
		{
			if ( event instanceof Crapped )
			{
				// a bird crapped on us
				m_zombie.setHitWithShit( true );
				die();
			}
			else if ( event instanceof Shot)
			{
				die();
			}
			else if ( event instanceof CollisionEvent )
			{
				// if it collides with another entity, it attempts eating it
				Entity collider = ( (CollisionEvent)event ).m_collider;
				collider.sendEvent( Bite.class );
				transitionTo( Observe.class );
			}
			else if ( event instanceof OutOfWorldBounds )
			{					
				OutOfWorldBounds oowb = (OutOfWorldBounds)event;
				
				DynamicObject dynObject = m_zombie.query( DynamicObject.class );
				oowb.reflectVector( m_tmpDirVec, dynObject.m_velocity );
				m_tmpDirVec.normalize2D().add( m_zombie.getPosition() );
				
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
			m_zombie.m_state = Zombie.State.TurnAround;
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
			Vector3 currPos = m_zombie.getPosition();
			float distSqToGoal = currPos.distSq2D( m_safePos );
			if ( distSqToGoal < 1e-1 )
			{
				transitionTo( Wander.class );
			}				
		}
	}
	
	// ------------------------------------------------------------------------
	
	class Observe extends FSMState implements EntityEventListener
	{
		private Random m_randWaitTime   = new Random();
		private int m_waitTimer         = 0;
		private float 	m_wait 			= 0.f;
		
		@Override
		public void activate()
		{
			m_zombie.m_state = Zombie.State.Observe;
			m_wait = 6.0f;
			
			// register events listeners
			m_waitTimer = m_randWaitTime.nextInt(6);
		}
		
		@Override
		public void deactivate()
		{
		}
		
		@Override
		public void execute( float deltaTime )
		{
			m_wait -= deltaTime;
			if ( m_wait < m_waitTimer )
			{
				transitionTo( Wander.class );
			}				
		}
		
		@Override
		public void onEvent( EntityEvent event ) 
		{
			if ( event instanceof Crapped )
			{
				// a bird crapped on us
				m_zombie.setHitWithShit( true );
				die();
			}
			else if ( event instanceof Shot)
			{
				die();
			}
			else if ( event instanceof CollisionEvent )
			{
				// if it collides with another entity, it attempts eating it
				Entity collider = ( (CollisionEvent)event ).m_collider;
				collider.sendEvent( Bite.class );

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
	public ZombieAI( World world, Entity zombie )
	{
		super( zombie );
		
		m_world = world;
		m_zombie = (Zombie)zombie;
		m_sb = new SteeringBehaviors( m_zombie );
		
		// define events the entity responds to
		m_zombie.registerEvent( Crapped.class, new EventFactory< Crapped >() { @Override public Crapped createObject() { return new Crapped (); } } );
		m_zombie.registerEvent( OutOfWorldBounds.class, new EventFactory< OutOfWorldBounds >() { @Override public OutOfWorldBounds createObject() { return new OutOfWorldBounds (); } } );
		m_zombie.registerEvent( CollisionEvent.class, new EventFactory< CollisionEvent >() { @Override public CollisionEvent createObject() { return new CollisionEvent (); } } );
		m_zombie.registerEvent( Shot.class, new EventFactory< Shot >() { @Override public Shot createObject() { return new Shot (); } } );
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
	
	void die()
	{
		m_zombie.m_world.removeEntity( m_zombie );
	}

}
