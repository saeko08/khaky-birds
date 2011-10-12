/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.pedestrian;


import java.util.Random;

import com.hypefoundry.khakyBirds.entities.crap.Crapped;
import com.hypefoundry.khakyBirds.entities.hideout.Hideout;
import com.hypefoundry.khakyBirds.entities.hideout.NotWalkAble;
import com.hypefoundry.khakyBirds.entities.zombie.Bite;
import com.hypefoundry.khakyBirds.entities.zombie.Zombie;
import com.hypefoundry.engine.controllers.fsm.FiniteStateMachine;
import com.hypefoundry.engine.controllers.fsm.FSMState;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.physics.locomotion.SteeringBehaviors;
import com.hypefoundry.engine.physics.events.CollisionEvent;
import com.hypefoundry.engine.physics.events.OutOfWorldBounds;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityEvent;
import com.hypefoundry.engine.world.EntityEventListener;
import com.hypefoundry.engine.world.EventFactory;
import com.hypefoundry.engine.world.World;
import com.hypefoundry.engine.world.WorldView;


/**
 * AI of a pedestrian.
 * 
 * @author paksas
 *
 */
public class PedestrianAI extends FiniteStateMachine implements WorldView
{
	private Pedestrian				m_pedestrian;
	private SteeringBehaviors 		m_sb;
	private final float 			m_zombieLookoutRadiusNear	= 0.3f;
	private final float 			m_zombieLookoutRadiusShort 	= 1.8f;
	private final float 			m_zombieLookoutRadiusFar 	= 2.0f;
	private float 					m_eatingTime				= 1.5f;
	private final float 			m_hideoutLookoutRadiusFar	= 10f;
	private final float 			m_hideoutLookoutRadiusShort	= 3.5f;
	
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
			m_pedestrian.m_state = Pedestrian.State.Wander;
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

			m_noticedZombie = m_pedestrian.m_world.findNearestEntity( Zombie.class, m_zombieLookoutRadiusShort, m_pedestrian.getPosition() );
			if ( m_noticedZombie != null )
			{
				transitionTo( Evade.class ).setEvadingTarget( m_noticedZombie );
			}
		}

		@Override
		public void onEvent( EntityEvent event ) 
		{
			if ( event instanceof Crapped )
			{
				// a bird crapped on us
				m_pedestrian.setHitWithShit( true );
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
				
				DynamicObject dynObject = m_pedestrian.query( DynamicObject.class );
				oowb.reflectVector( m_tmpDirVec, dynObject.m_velocity );
				m_tmpDirVec.normalize2D().add( m_pedestrian.getPosition() );
				
				transitionTo( TurnAround.class ).m_safePos.set( m_tmpDirVec );
				
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
				
				m_pedestrian.m_state = Pedestrian.State.Hiding;
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
				Vector3 currPos = m_pedestrian.getPosition();
				float distSqToGoal = currPos.distSq2D( m_hideoutPos );
				if ( distSqToGoal < 2e-1 )
				{
					if (m_pedestrian.m_hitWithShit == false)
					{
						m_hideoutEntity.m_pedestrians +=1;
					}
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
			if ( event instanceof Crapped )
			{
				// a bird crapped on us
				m_pedestrian.setHitWithShit( true );
				transitionTo( Shitted.class );
			}
			else if ( event instanceof Bite )
			{
				transitionTo( Eaten.class );
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
	
	class TurnAround extends FSMState implements EntityEventListener
	{	
		private Vector3 m_safePos 		= new Vector3();
		
		
		@Override
		public void activate()
		{
			m_noticedZombie				= null;
			m_pedestrian.m_state = Pedestrian.State.TurnAround;
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
			m_pedestrian.m_state = Pedestrian.State.Avoid;
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
			Vector3 currPos = m_pedestrian.getPosition();
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
				m_pedestrian.setHitWithShit( true );
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
			m_pedestrian.m_state = Pedestrian.State.Observe;
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
			m_wait -= deltaTime;
			if ( m_wait < m_waitTimer )
			{
				transitionTo( Wander.class );
			}	
			
			m_noticedZombie = m_pedestrian.m_world.findNearestEntity( Zombie.class, m_zombieLookoutRadiusShort, m_pedestrian.getPosition() );
			if ( m_noticedZombie != null )
			{
				transitionTo( Evade.class ).setEvadingTarget( m_noticedZombie );
			}
		}
		
		@Override
		public void onEvent( EntityEvent event ) 
		{
			if ( event instanceof Crapped )
			{
				// a bird crapped on us
				m_pedestrian.setHitWithShit( true );
				transitionTo( Shitted.class );
			}
			else if ( event instanceof Bite )
			{
				transitionTo( Eaten.class );
			}
			
		}

	}
	
	// ------------------------------------------------------------------------
	
	class Evade extends FSMState implements EntityEventListener
	{
		private Vector3 m_tmpDirVec 		= new Vector3();
		
		@Override
		public void activate()
		{
			m_pedestrian.m_state = Pedestrian.State.Evade;	
			Entity evadeEntity = (Entity)m_noticedZombie;
			if ( evadeEntity != null )
			{
				m_sb.begin().evade(m_noticedZombie).faceMovementDirection();
			}
		}
		
		@Override
		public void deactivate()
		{
			m_sb.clear();
			m_noticedZombie = null;
		}
		
		@Override
		public void execute( float deltaTime )
		{
			//testuje dynamiczne sprawdzanie nalbli¿szych zombiech i rakcjê na to - byc moze do wywalenia
			//m_noticedZombie = (Zombie) m_pedestrian.m_world.findNearestEntity(Zombie.class, 1.5f, m_pedestrian.getPosition());
			
			if (m_noticedZombie == null )
			{
				transitionTo( Wander.class );
			}
			else
			{
				float currZombieDistance =  m_noticedZombie.getPosition().distSq2D(m_pedestrian.getPosition());
				if (currZombieDistance > m_zombieLookoutRadiusFar)
				{
					transitionTo( Wander.class );
				}
				else
				{
					m_hideoutEntity = m_pedestrian.m_world.findNearestEntity( Hideout.class, m_hideoutLookoutRadiusShort, m_pedestrian.getPosition() );
					if (m_hideoutEntity != null)
					{
						transitionTo( Hiding.class );
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
				m_pedestrian.setHitWithShit( true );
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
				
				DynamicObject dynObject = m_pedestrian.query( DynamicObject.class );
				oowb.reflectVector( m_tmpDirVec, dynObject.m_velocity );
				m_tmpDirVec.normalize2D().add( m_pedestrian.getPosition() );
				
				transitionTo( TurnAround.class ).m_safePos.set( m_tmpDirVec );
				
			}
		}
		
		public void setEvadingTarget(Zombie noticedZombie)
		{
			m_noticedZombie = noticedZombie;
		}
	}
	
	// ------------------------------------------------------------------------
	class Eaten extends FSMState
	{
		private float 	m_wait 			= 0.f;
		
		@Override
		public void activate()
		{
			m_pedestrian.m_state = Pedestrian.State.Eaten;
			
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
					m_noticedZombie = m_pedestrian.m_world.findNearestEntity( Zombie.class, m_zombieLookoutRadiusNear, m_pedestrian.getPosition() );
					if (m_noticedZombie != null)
					{
						m_pedestrian.turnIntoZombie();
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
			m_pedestrian.m_state = Pedestrian.State.Shitted;
			m_wait = 3.0f;
			
			m_waitTimer = m_randWaitTime.nextInt(3);
		}
		
		@Override
		public void deactivate()
		{

		}
		
		@Override
		public void execute( float deltaTime )
		{
			m_noticedZombie = m_pedestrian.m_world.findNearestEntity( Zombie.class, m_zombieLookoutRadiusFar, m_pedestrian.getPosition() );
			if ( m_noticedZombie != null )
			{
				transitionTo( Evade.class ).setEvadingTarget( m_noticedZombie );
			}
			
			m_wait -= deltaTime;
			if ( m_wait < m_waitTimer )
			{
				m_hideoutEntity = m_pedestrian.m_world.findNearestEntity( Hideout.class, m_hideoutLookoutRadiusFar, m_pedestrian.getPosition() );
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
			if ( event instanceof Crapped )
			{
				// a bird crapped on us
				m_pedestrian.setHitWithShit( true );
				transitionTo( Shitted.class );
			}
			else if ( event instanceof Bite )
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
	public PedestrianAI( Entity pedestrian )
	{
		super( pedestrian );
		
		m_pedestrian = (Pedestrian)pedestrian;
		m_sb = new SteeringBehaviors( m_pedestrian );
		
		// define events the entity responds to
		m_pedestrian.registerEvent( Crapped.class, new EventFactory< Crapped >() { @Override public Crapped createObject() { return new Crapped (); } } );
		m_pedestrian.registerEvent( OutOfWorldBounds.class, new EventFactory< OutOfWorldBounds >() { @Override public OutOfWorldBounds createObject() { return new OutOfWorldBounds (); } } );
		m_pedestrian.registerEvent( Bite.class, new EventFactory< Bite >() { @Override public Bite createObject() { return new Bite (); } } );
		m_pedestrian.registerEvent( CollisionEvent.class, new EventFactory< CollisionEvent >() { @Override public CollisionEvent createObject() { return new CollisionEvent (); } } );
	
		m_pedestrian.m_world.attachView(this);

		// setup the state machine
		register( new Wander() );
		register( new TurnAround() );
		register( new Observe() );
		register( new Evade() );
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
		m_pedestrian.m_world.detachView( this );
		m_pedestrian.m_world.removeEntity( m_pedestrian );
	}
}
