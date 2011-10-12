/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.hunter;

import com.hypefoundry.khakyBirds.entities.bird.Bird;
import com.hypefoundry.khakyBirds.entities.crap.Crapped;
import com.hypefoundry.khakyBirds.entities.zombie.Bite;
import com.hypefoundry.khakyBirds.entities.zombie.Zombie;
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
import com.hypefoundry.engine.world.WorldView;

/**
 * Hunter's AI.
 * 
 * @author azagor
 */
public class HunterAI extends FiniteStateMachine implements WorldView
{
	private Hunter				m_hunter;
	private SteeringBehaviors 	m_sb;
	private Bird				m_bird;
	private float 				m_eatingTime				= 1.5f;
	private float 				m_blindTime				= 4.5f;
	
	private final float			MAX_AIM_TOLERANCE = 15.0f; // TODO: config
	private final float			MIN_AIM_TOLERANCE = 2.0f; // TODO: config
	private final float 		m_zombieLookoutRadiusFar 	= 3.5f;
	private final float 		m_zombieLookoutRadiusShort 	= 3f;
	private final float 		m_zombieLookoutRadiusNear 	= 0.3f;
	
	

	
	// ------------------------------------------------------------------------
	//blackboard
	private Zombie 					m_noticedZombie				= null;
	
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
	public void onEntityAdded( Entity entity ) 
	{
		if ( entity instanceof Bird )
		{
			m_bird = (Bird)entity;
		}
	}

	@Override
	public void onEntityRemoved(Entity entity) 
	{
		if ( entity == m_noticedZombie )
		{
			m_noticedZombie = null;
		}
		if ( entity == m_bird )
		{
			m_bird = null;
		}
	}
	
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
			m_noticedZombie = m_hunter.m_world.findNearestEntity( Zombie.class, m_zombieLookoutRadiusShort, m_hunter.getPosition() );
			if ( m_noticedZombie != null )
			{
				transitionTo( AimingZombie.class );
			}
			else if ( m_bird != null )
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
			else if ( event instanceof Bite )
			{
				transitionTo( Eaten.class );
			}
		}
	}
	
	// ------------------------------------------------------------------------
	
	class AimingZombie extends FSMState implements EntityEventListener
	{		
		
		
		@Override
		public void activate()
		{
			m_hunter.m_state = Hunter.State.AimingZombie;
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
				transitionTo( Aiming.class );
			}
			else 
			{
				float currAimingDistance = m_noticedZombie.getPosition().distSq2D(m_hunter.getPosition());
				if (currAimingDistance > m_zombieLookoutRadiusFar)
				{
					transitionTo( Aiming.class );
				}
				// look at the zombie, but with some tolerance
				else
				{
					float angleDiff = MathLib.lookAtDiff( m_noticedZombie.getPosition(), m_hunter.getPosition(), m_hunter.getFacing() );
					if ( angleDiff < MIN_AIM_TOLERANCE )
					{
						transitionTo( ShootingZombie.class );
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
			m_noticedZombie = m_hunter.m_world.findNearestEntity( Zombie.class, m_zombieLookoutRadiusShort, m_hunter.getPosition() );
			if ( m_noticedZombie != null )
			{
				transitionTo( AimingZombie.class );
			}
			
			else if ( m_bird != null )
			{
				// keep monitoring the bird's position, because we may need to start aiming
				float angleDiff = MathLib.lookAtDiff( m_bird.getPosition(), m_hunter.getPosition(), m_hunter.getFacing() );
				
				if ( angleDiff > MAX_AIM_TOLERANCE )
				{
					transitionTo( Aiming.class );
				}
			}
			else
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
			else if ( event instanceof Bite )
			{
				transitionTo( Eaten.class );
			}
		}
	}
	
	// ------------------------------------------------------------------------
	
	class ShootingZombie extends FSMState implements EntityEventListener
	{	
		@Override
		public void activate()
		{
			Entity aimingEntity = (Entity)m_noticedZombie;
			if ( aimingEntity != null )
			{
				m_hunter.m_state = Hunter.State.ShootingZombie;
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
				float angleDiff = MathLib.lookAtDiff( m_noticedZombie.getPosition(), m_hunter.getPosition(), m_hunter.getFacing() );
				float currAimingDistance = m_noticedZombie.getPosition().distSq2D(m_hunter.getPosition());
				
				if ( angleDiff > MAX_AIM_TOLERANCE || currAimingDistance > m_zombieLookoutRadiusFar)
				{
					transitionTo( Aiming.class );
				}
			}
			else
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
			else if ( event instanceof Bite )
			{
				transitionTo( Eaten.class );
			}
		}
	}
	
	// ------------------------------------------------------------------------
	
	class Shitted extends FSMState implements EntityEventListener
	{
		
		private float 	m_wait 			= 0.f;
		
		@Override
		public void activate()
		{
			m_hunter.m_state = Hunter.State.Shitted;
			// TODO: ma zejsc ze sceny
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
			if ( m_wait >= m_blindTime )
			{
				transitionTo( Aiming.class );
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
		class Eaten extends FSMState
		{
			private float 	m_wait 			= 0.f;
			
			@Override
			public void activate()
			{
				m_hunter.m_state = Hunter.State.Eaten;
				
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
					m_noticedZombie = m_hunter.m_world.findNearestEntity( Zombie.class, m_zombieLookoutRadiusNear, m_hunter.getPosition() );
					if (m_noticedZombie != null)
					{
						m_hunter.turnIntoZombie();
						die();
					}
					else
					{
						transitionTo( Aiming.class );
					}
				}

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
		
		// define events the entity responds to
		m_hunter.registerEvent( Crapped.class, new EventFactory< Crapped >() { @Override public Crapped createObject() { return new Crapped (); } } );
		m_hunter.registerEvent( Bite.class, new EventFactory< Bite >() { @Override public Bite createObject() { return new Bite (); } } );
		m_hunter.registerEvent( AnimEvent.class, new EventFactory< AnimEvent >() { @Override public AnimEvent createObject() { return new AnimEvent (); } } );

		m_hunter.m_world.attachView(this);
		
		// setup the state machine
		register( new Aiming() );
		register( new Shooting() );
		register( new Shitted() );
		register( new Eaten() );
		register( new AimingZombie() );
		register( new ShootingZombie() );
		begin( Aiming.class );
	}
	
	@Override
	public void onUpdate( float deltaTime )
	{
		m_sb.update(deltaTime);
		if (m_bird == null)
		{
			m_bird = (Bird) m_hunter.m_world.findEntity(Bird.class);
		}
	}
	
	void die()
	{
		m_hunter.m_world.detachView( this );
		m_hunter.m_world.removeEntity( m_hunter );
	}
}
