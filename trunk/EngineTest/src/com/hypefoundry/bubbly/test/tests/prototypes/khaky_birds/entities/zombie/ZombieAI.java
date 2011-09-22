/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.zombie;

import java.util.Random;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.crap.Crappable;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.crap.Crapped;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.hunter.Shot;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.pedestrian.Pedestrian;
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
import com.hypefoundry.engine.world.WorldView;


/**
 * @author azagor
 *
 */
public class ZombieAI extends FiniteStateMachine implements WorldView, EntityEventListener
{
	private Zombie				m_zombie;
	private SteeringBehaviors 	m_sb;
	private final float			m_pedestrianLookoutRadius		= 1.2f;
	private float               m_eatingTime 					= 2.5f;
	
	// ------------------------------------------------------------------------
	// temp data
	private Vector3 			m_tmpDirVec 					= new Vector3();
	
	// ------------------------------------------------------------------------
	//blackboard
	private Biteable			m_noticedBiteableEntity	  = null;

	@Override
	public void onAttached(World world) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onDetached(World world) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onEntityAdded(Entity entity) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onEntityRemoved(Entity entity) 
	{
		if ( entity == m_noticedBiteableEntity )
		{
			m_noticedBiteableEntity = null;
		}
	}

	// ------------------------------------------------------------------------
	
	class Wander extends FSMState
	{	
		private Random m_randObserveChnce   		= new Random();
		private int m_toObserveChance       		= 0;
		
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
			m_noticedBiteableEntity = m_zombie.m_world.findNearestEntity( Biteable.class, 1.2f, m_zombie.getPosition() );
			
			if ( m_noticedBiteableEntity != null )
			{
				transitionTo( Chasing.class );
			}
			
			m_toObserveChance = m_randObserveChnce.nextInt(25);
			
			if ( m_toObserveChance == 5 )
			{
				transitionTo( Observe.class );
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
			m_noticedBiteableEntity	  = null;
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
	
	class Observe extends FSMState
	{
		private Random m_randWaitTime   = new Random();
		private int m_waitTimer         = 0;
		private float 	m_wait 			= 0.f;
		
		@Override
		public void activate()
		{
			m_zombie.m_state = Zombie.State.Observe;
			m_wait = 3.0f;
			
			m_waitTimer = m_randWaitTime.nextInt(3);
		}
		
		@Override
		public void deactivate()
		{
			//m_noticedPedestrian 	= null;
		}
		
		@Override
		public void execute( float deltaTime )
		{
			m_wait -= deltaTime;
			if ( m_wait < m_waitTimer )
			{
				transitionTo( Wander.class );
			}

			m_noticedBiteableEntity = m_zombie.m_world.findNearestEntity( Biteable.class, m_pedestrianLookoutRadius, m_zombie.getPosition() );
			
			if ( m_noticedBiteableEntity != null )
			{
				transitionTo( Chasing.class );
			}
		}
	}
	
	// ------------------------------------------------------------------------
	
	class Chasing extends FSMState
	{		
		@Override
		public void activate()
		{
			m_zombie.m_state = Zombie.State.Chasing;
			Entity pursuedEntity = (Entity)m_noticedBiteableEntity;
			if ( pursuedEntity != null )
			{
				m_sb.begin().pursuit( pursuedEntity ).faceMovementDirection();
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
			// testuje dynamiczne sprawdzanie nalbli�szych zombiech i rakcj� na to - byc moze do wywalenia
			m_noticedBiteableEntity = m_zombie.m_world.findNearestEntity( Biteable.class, 1.5f, m_zombie.getPosition() );
			if ( m_noticedBiteableEntity == null )
			{
				transitionTo( Wander.class );
			}
		}
	}
	
	// ------------------------------------------------------------------------
	
	class Eat extends FSMState
	{
		private float 	m_wait 			= 0.f;
		
		@Override
		public void activate()
		{
			m_zombie.m_state = Zombie.State.Eat;
			Entity pursuedEntity = (Entity)m_noticedBiteableEntity;
			if ( pursuedEntity != null )
			{
				m_sb.begin().arrive( pursuedEntity.getPosition(), 1.5f).faceMovementDirection();
			}
			
		}
		
		@Override
		public void deactivate()
		{
			m_wait 	= 0.f;
			m_sb.clear();
		}
		
		@Override
		public void execute( float deltaTime )
		{
			m_wait += deltaTime;
			if ( m_wait >= m_eatingTime )
			{
				transitionTo( Wander.class );
			}

		}
	}
		
	// ------------------------------------------------------------------------
		
	/**
	 * Constructor.
	 * 
	 * @param pedestrian			controlled pedestrian
	 */
	public ZombieAI( Entity zombie )
	{
		super( zombie );
		
		m_zombie = (Zombie)zombie;
		m_sb = new SteeringBehaviors( m_zombie );
		m_zombie.attachEventListener( this );
		
		// define events the entity responds to
		m_zombie.registerEvent( Crapped.class, new EventFactory< Crapped >() { @Override public Crapped createObject() { return new Crapped (); } } );
		m_zombie.registerEvent( OutOfWorldBounds.class, new EventFactory< OutOfWorldBounds >() { @Override public OutOfWorldBounds createObject() { return new OutOfWorldBounds (); } } );
		m_zombie.registerEvent( CollisionEvent.class, new EventFactory< CollisionEvent >() { @Override public CollisionEvent createObject() { return new CollisionEvent (); } } );
		m_zombie.registerEvent( Shot.class, new EventFactory< Shot >() { @Override public Shot createObject() { return new Shot (); } } );
		
		m_zombie.m_world.attachView( this );
		
		// setup the state machine
		register( new Wander() );
		register( new TurnAround() );
		register( new Observe() );
		register( new Chasing() );
		register( new Eat() );
		begin( Wander.class );
	}
	
	@Override
	public void onUpdate( float deltaTime )
	{
		m_sb.update(deltaTime);
	}
	
	void die()
	{
		m_zombie.detachEventListener( this );
		m_zombie.m_world.detachView( this );
		m_zombie.m_world.removeEntity( m_zombie );
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
		else if ( event instanceof Shot )
		{
			die();
		}
		else if ( event instanceof CollisionEvent )
		{
			// if it collides with another entity, it attempts eating it
			Entity collider = ( (CollisionEvent)event ).m_collider;
			if( collider instanceof Biteable )
			{
				collider.sendEvent( Bite.class );
				m_noticedBiteableEntity = (Biteable)collider;
				transitionTo( Eat.class );
			}
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
