/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.perkPedestrian;

import java.util.Random;

import com.hypefoundry.khakyBirds.entities.bird.Bird;
import com.hypefoundry.khakyBirds.entities.crap.Crapped;
import com.hypefoundry.khakyBirds.entities.hideout.Hideout;
import com.hypefoundry.khakyBirds.entities.hideout.NotWalkAble;
import com.hypefoundry.khakyBirds.entities.hunter.Fire;
import com.hypefoundry.khakyBirds.entities.zombie.Bite;
import com.hypefoundry.khakyBirds.entities.zombie.Zombie;
import com.hypefoundry.engine.controllers.fsm.FSMState;
import com.hypefoundry.engine.controllers.fsm.FiniteStateMachine;
import com.hypefoundry.engine.math.MathLib;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.physics.events.CollisionEvent;
import com.hypefoundry.engine.physics.events.OutOfWorldBounds;
import com.hypefoundry.engine.physics.locomotion.SteeringBehaviors;
import com.hypefoundry.engine.renderer2D.animation.AnimEvent;
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
public class PerkPedestrianAI extends FiniteStateMachine implements WorldView
{
	private PerkPedestrian			m_perkPedestrian;
	private SteeringBehaviors 		m_sb;
	private Bird					m_bird;
	private final float 			m_zombieLookoutRadiusShort 	= 2f;
	private final float 			m_zombieLookoutRadiusNear 	= 0.3f;
	private final float 			m_zombieLookoutRadiusFar 	= 3f;
	private float 					m_eatingTime				= 1.5f;
	private final float 			m_hideoutLookoutRadiusFar	= 10f;
	private final float 			m_hideoutLookoutRadiusShort	= 3.5f;
	
	private final float			MAX_AIM_TOLERANCE = 15.0f; // TODO: config
	private final float			MIN_AIM_TOLERANCE = 2.0f; // TODO: config
	
	// ------------------------------------------------------------------------
	//blackboard
	private Zombie 					m_noticedZombie				= null;
	private Hideout					m_hideoutEntity				= null;
	
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
		if ( entity == m_noticedZombie )
		{
			m_noticedZombie = null;
		}
		if ( entity == m_hideoutEntity )
		{
			m_hideoutEntity = null;
		}
		
	}
	

	// ------------------------------------------------------------------------
	
	class Wander extends FSMState implements EntityEventListener
	{	
		private Vector3 m_tmpDirVec 		= new Vector3();
		private Random m_randObserveChnce   = new Random();
		private int 	m_toObserveChance       = 0;

		
		@Override
		public void activate()
		{
			m_perkPedestrian.m_state = PerkPedestrian.State.Wander;
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

			m_noticedZombie = m_perkPedestrian.m_world.findNearestEntity( Zombie.class, m_zombieLookoutRadiusShort, m_perkPedestrian.getPosition() );
			if ( m_noticedZombie != null )
			{
				transitionTo( Aiming.class );
			}
		}

		@Override
		public void onEvent( EntityEvent event ) 
		{
			if ( event instanceof Crapped )
			{
				// a bird crapped on us
				m_perkPedestrian.setHitWithShit( true );
				transitionTo( Shitted.class );
			}
			else if ( event instanceof Bite )
			{
				transitionTo( Eaten.class );
			}
			else if ( event instanceof CollisionEvent )
			{
				Entity collider = ( (CollisionEvent)event ).m_collider;
				
				if ( collider instanceof NotWalkAble)
					{
						m_tmpDirVec.set(collider.getPosition());
						transitionTo( Avoid.class ).m_safePos.set( m_tmpDirVec );
					}
			}
			else if ( event instanceof OutOfWorldBounds )
			{					
				OutOfWorldBounds oowb = (OutOfWorldBounds)event;
				
				DynamicObject dynObject = m_perkPedestrian.query( DynamicObject.class );
				oowb.reflectVector( m_tmpDirVec, dynObject.m_velocity );
				m_tmpDirVec.normalize2D().add( m_perkPedestrian.getPosition() );
				
				transitionTo( TurnAround.class ).m_safePos.set( m_tmpDirVec );
				
			}
		}
	}
	
	// ------------------------------------------------------------------------
	
	class Aiming extends FSMState implements EntityEventListener
	{		
		
		
		@Override
		public void activate()
		{
			m_perkPedestrian.m_state = PerkPedestrian.State.Aiming;
			m_sb.clear();
			
			Entity aimingEntity = (Entity)m_noticedZombie;
			if ( aimingEntity != null )
			{
				// look at the zombie, but with some tolerance
				m_sb.begin().lookAt( m_noticedZombie );
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
			//to byæ mo¿e nie bêdzie potrzebne
			//m_noticedZombie = m_hunter.m_world.findNearestEntity( Zombie.class, m_zombieLookoutRadiusFar, m_hunter.getPosition() );
			if ( m_noticedZombie == null )
			{
				transitionTo( Wander.class );
			}
			else 
			{
				float currAimingDistance = m_noticedZombie.getPosition().distSq2D(m_perkPedestrian.getPosition());
				if (currAimingDistance > m_zombieLookoutRadiusFar )
				{
					transitionTo( Observe.class );
				}
				// look at the zombie, but with some tolerance
				else
				{
					float angleDiff = MathLib.lookAtDiff( m_noticedZombie.getPosition(), m_perkPedestrian.getPosition(), m_perkPedestrian.getFacing() );
					if ( angleDiff < MIN_AIM_TOLERANCE )
					{
						transitionTo( Shooting.class );
					}
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
			else if ( event instanceof Bite )
			{
				transitionTo( Eaten.class );
			}
		}
	}
	
	// ------------------------------------------------------------------------
	
	class Shooting extends FSMState implements EntityEventListener
	{	
		@Override
		public void activate()
		{
			Entity aimingEntity = (Entity)m_noticedZombie;
			if ( aimingEntity != null )
			{
				m_perkPedestrian.m_state = PerkPedestrian.State.Shooting;
				m_sb.clear();
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
			//m_noticedZombie = m_hunter.m_world.findNearestEntity( Zombie.class, m_zombieLookoutRadiusFar, m_hunter.getPosition() );
			if ( m_noticedZombie != null )
			{
				
				// keep monitoring the bird's position, because we may need to start aiming
				float angleDiff = MathLib.lookAtDiff( m_noticedZombie.getPosition(), m_perkPedestrian.getPosition(), m_perkPedestrian.getFacing() );
				float currAimingDistance = m_noticedZombie.getPosition().distSq2D(m_perkPedestrian.getPosition());
				
				if ( angleDiff > MAX_AIM_TOLERANCE || currAimingDistance > m_zombieLookoutRadiusFar)
				{
					transitionTo( Wander.class );
				}
			}
			else
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
				transitionTo( Shitted.class );
			}
			else if ( event instanceof AnimEvent )
			{
				AnimEvent animEvent = (AnimEvent)event;
				if ( animEvent.m_event instanceof Fire )
				{
					m_perkPedestrian.shoot();
				}
			}
			else if ( event instanceof Bite )
			{
				transitionTo( Eaten.class );
			}
		}
	}
	
	// ------------------------------------------------------------------------
	
	class Hiding extends FSMState implements EntityEventListener
	{	
		private Vector3 m_tmpDirVec 		= new Vector3();
		
		private Vector3 m_hideoutPos 		= new Vector3();
	

		
		@Override
		public void activate()
		{
			Entity hideoutEntity = (Entity)m_hideoutEntity;
			if ( hideoutEntity != null )
			{
				m_hideoutPos.set(m_hideoutEntity.getPosition());
				
				m_perkPedestrian.m_state = PerkPedestrian.State.Hiding;
				m_sb.begin().arrive( m_hideoutPos, 0.5f).faceMovementDirection();
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
			if (m_hideoutEntity != null)
			{
				Vector3 currPos = m_perkPedestrian.getPosition();
				float distSqToGoal = currPos.distSq2D( m_hideoutPos );
				if ( distSqToGoal < 2e-1 )
				{
					die();
				}	
			}
			else
			{
				transitionTo( Wander.class );
			}
		}

		@Override
		public void onEvent( EntityEvent event ) 
		{
			if ( event instanceof Bite )
			{
				transitionTo( Eaten.class );
			}
			
			else if ( event instanceof OutOfWorldBounds )
			{					
				OutOfWorldBounds oowb = (OutOfWorldBounds)event;
				
				DynamicObject dynObject = m_perkPedestrian.query( DynamicObject.class );
				oowb.reflectVector( m_tmpDirVec, dynObject.m_velocity );
				m_tmpDirVec.normalize2D().add( m_perkPedestrian.getPosition() );
				
				transitionTo( TurnAround.class ).m_safePos.set( m_tmpDirVec );
				
			}
		}
	}
	
	// ------------------------------------------------------------------------
	
	class TurnAround extends FSMState implements EntityEventListener
	{	
		private Vector3 m_safePos 		= new Vector3();
		
		
		@Override
		public void activate()
		{
			m_noticedZombie				= null;
			m_perkPedestrian.m_state = PerkPedestrian.State.TurnAround;
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
			Vector3 currPos = m_perkPedestrian.getPosition();
			float distSqToGoal = currPos.distSq2D( m_safePos );
			if ( distSqToGoal < 1e-1 )
			{
				transitionTo( Wander.class );
			}				
		}

		@Override
		public void onEvent(EntityEvent event) 
		{
			if ( event instanceof Bite )
			{
				transitionTo( Eaten.class );
			}
			
		}
	}
	
	// ------------------------------------------------------------------------
	class Avoid extends FSMState implements EntityEventListener
	{	
		private Vector3 m_safePos 		= new Vector3();
		
		
		@Override
		public void activate()
		{
			m_perkPedestrian.m_state = PerkPedestrian.State.Avoid;
			m_sb.begin().flee( m_safePos ).faceMovementDirection();
		}
		
		@Override
		public void deactivate()
		{
			m_sb.clear();
		}
		
		@Override
		public void execute( float deltaTime )
		{
			Vector3 currPos = m_perkPedestrian.getPosition();
			float distSqToGoal = currPos.distSq2D( m_safePos );
			if ( distSqToGoal > 7e-1 )
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
				m_perkPedestrian.setHitWithShit( true );
				transitionTo(Shitted.class );
			}
			else if ( event instanceof Bite )
			{
				transitionTo( Eaten.class );
			}
			
		}
	}
	
	// ------------------------------------------------------------------------
	
	
	class Observe extends FSMState implements EntityEventListener
	{
		private Random m_randWaitTime   	= new Random();
		private int m_waitTimer        		= 0;
		private float 	m_wait 				= 0.f;
		
		@Override
		public void activate()
		{
			m_perkPedestrian.m_state = PerkPedestrian.State.Observe;
			m_wait = 3.0f;
			
			// register events listeners
			m_waitTimer = m_randWaitTime.nextInt(3);
		}
		
		@Override
		public void deactivate()
		{

		}
		
		@Override
		public void execute( float deltaTime )
		{
			m_noticedZombie = m_perkPedestrian.m_world.findNearestEntity( Zombie.class, m_zombieLookoutRadiusShort, m_perkPedestrian.getPosition() );
			if ( m_noticedZombie != null )
			{
				transitionTo( Aiming.class );
			}
			
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
				m_perkPedestrian.setHitWithShit( true );
				transitionTo( Shitted.class );
			}
			else if ( event instanceof Bite )
			{
				transitionTo( Eaten.class );
			}
			
		}

	}
	
	// ------------------------------------------------------------------------
	
	class Eaten extends FSMState
	{
		private float 	m_wait 			= 0.f;
		
		@Override
		public void activate()
		{
			m_perkPedestrian.m_state = PerkPedestrian.State.Eaten;
			
		}
		
		@Override
		public void deactivate()
		{
			m_wait 			= 0.f;
		}
		
		@Override
		public void execute( float deltaTime )
		{
			m_wait += deltaTime;
			if ( m_wait >= m_eatingTime )
			{
					m_noticedZombie = m_perkPedestrian.m_world.findNearestEntity( Zombie.class, m_zombieLookoutRadiusNear, m_perkPedestrian.getPosition() );
					if (m_noticedZombie != null)
					{
						m_perkPedestrian.turnIntoZombie();
						die();
					}
					else
					{
						transitionTo( Wander.class );
					}
			}

		}

	}
	
// ------------------------------------------------------------------------
	class Shitted extends FSMState implements EntityEventListener
	{
		private Random m_randWaitTime   	= new Random();
		private int m_waitTimer        		= 0;
		private float 	m_wait 				= 0.f;
		
		@Override
		public void activate()
		{
			m_perkPedestrian.m_state = PerkPedestrian.State.Shitted;
			m_wait = 3.0f;
			
			m_waitTimer = m_randWaitTime.nextInt(3);
			
			if (m_bird != null)
			{
				m_bird.setSpecialCrap();
			}
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
				m_hideoutEntity = m_perkPedestrian.m_world.findNearestEntity( Hideout.class, m_hideoutLookoutRadiusFar, m_perkPedestrian.getPosition() );
				if (m_hideoutEntity != null)
				{
					transitionTo( Hiding.class );
				}
				else
				{
					transitionTo( Wander.class );
				}
				
			}	
			
		}
		
		@Override
		public void onEvent( EntityEvent event ) 
		{
		 if ( event instanceof Bite )
			{
				transitionTo( Eaten.class );
			}
			
		}

	}
	
	// ------------------------------------------------------------------------
	/**
	 * Constructor.
	 * 
	 * @param pedestrian			controlled pedestrian
	 */
	public PerkPedestrianAI(World world, Entity perkPedestrian )
	{
		super( perkPedestrian );
		
		m_perkPedestrian = (PerkPedestrian)perkPedestrian;
		m_sb = new SteeringBehaviors( m_perkPedestrian );
		m_bird = (Bird) world.findEntity(Bird.class);
		
		// define events the entity responds to
		m_perkPedestrian.registerEvent( Crapped.class, new EventFactory< Crapped >() { @Override public Crapped createObject() { return new Crapped (); } } );
		m_perkPedestrian.registerEvent( OutOfWorldBounds.class, new EventFactory< OutOfWorldBounds >() { @Override public OutOfWorldBounds createObject() { return new OutOfWorldBounds (); } } );
		m_perkPedestrian.registerEvent( Bite.class, new EventFactory< Bite >() { @Override public Bite createObject() { return new Bite (); } } );
		m_perkPedestrian.registerEvent( CollisionEvent.class, new EventFactory< CollisionEvent >() { @Override public CollisionEvent createObject() { return new CollisionEvent (); } } );
		m_perkPedestrian.registerEvent( AnimEvent.class, new EventFactory< AnimEvent >() { @Override public AnimEvent createObject() { return new AnimEvent (); } } );
	
		m_perkPedestrian.m_world.attachView(this);

		// setup the state machine
		register( new Wander() );
		register( new TurnAround() );
		register( new Observe() );
		register( new Aiming() );
		register( new Shooting() );
		register( new Eaten() );
		register( new Avoid() );
		register( new Hiding() );
		register( new Shitted() );
		begin( Wander.class );
	}
	
	@Override
	public void onUpdate( float deltaTime )
	{
		m_sb.update(deltaTime);
	}
	
	void die()
	{
		m_perkPedestrian.m_world.detachView( this );
		m_perkPedestrian.m_world.removeEntity( m_perkPedestrian );
	}
}
