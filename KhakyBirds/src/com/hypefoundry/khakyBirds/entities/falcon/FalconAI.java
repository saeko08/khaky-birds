/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.falcon;

import java.util.List;
import java.util.Random;

import com.hypefoundry.khakyBirds.entities.bird.Bird;
import com.hypefoundry.khakyBirds.entities.hunter.Shot;
import com.hypefoundry.khakyBirds.entities.shock.Shocked;
import com.hypefoundry.engine.controllers.EntityController;
import com.hypefoundry.engine.controllers.fsm.FSMState;
import com.hypefoundry.engine.controllers.fsm.FiniteStateMachine;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityEvent;
import com.hypefoundry.engine.world.EntityEventListener;
import com.hypefoundry.engine.world.EventFactory;
import com.hypefoundry.engine.world.World;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.events.CollisionEvent;
import com.hypefoundry.engine.physics.events.OutOfWorldBounds;
import com.hypefoundry.engine.physics.locomotion.SteeringBehaviors;

/**
 * @author azagor
 *
 */
public class FalconAI extends FiniteStateMachine
{

	private World 				m_world;
	private Falcon				m_falcon;
	private Bird                m_bird          = null;
	private SteeringBehaviors	m_sb;
	
	
	
	/**
	 * Constructor.
	 * 
	 * @param world
	 * @param falcon			controlled falcon
	 */
	public FalconAI( World world, Entity falcon )
	{
		super( falcon );
		m_world = world;
		m_falcon = (Falcon)falcon;
		m_bird = (Bird) m_world.findEntity(Bird.class);
		m_sb = new SteeringBehaviors( m_falcon);
		
		//register states
		
		register( new Hunting() );
		register( new Chasing() );
		
		begin( Hunting.class );
		
		// define events the entity responds to
		m_falcon.registerEvent( Shocked.class, new EventFactory< Shocked >() { @Override public Shocked createObject() { return new Shocked (); } } );
		m_falcon.registerEvent( Shot.class, new EventFactory< Shot >() { @Override public Shot createObject() { return new Shot (); } } );
		m_falcon.registerEvent( OutOfWorldBounds.class, new EventFactory< OutOfWorldBounds >() { @Override public OutOfWorldBounds createObject() { return new OutOfWorldBounds (); } } );
		m_falcon.registerEvent( CollisionEvent.class, new EventFactory< CollisionEvent >() { @Override public CollisionEvent createObject() { return new CollisionEvent (); } } );
			
	}
	
	//states definitions:
	//-----------------------------------------------------------
	class Hunting extends FSMState implements EntityEventListener
	{
		private	Vector3 m_birdPos  					     = new Vector3();
		private Vector3 m_circlingPt					 = new Vector3();
		private	Vector3 m_falconPos 					 = new Vector3();
		private Random m_randChasingChance     			 = new Random();
		private float m_wait                   			 = 0;
		
		private final float m_radiusRandomizationPeriod  = 3;
		private final float m_maxChasingDist			 = 1;
		private final float m_maxCirclingRadius			 = 1;
		
		
		@Override
		public void activate()
		{
			m_falcon.m_state = Falcon.State.Hunting;
			m_circlingPt.set( m_world.getWidth()/2, m_world.getHeight()/2, 1);
			randomizeCirclingRange();			
		}
		
		@Override
		public void deactivate()
		{
			m_sb.clear();
		}
		
		@Override
		public void execute( float deltaTime )
		{
			// falcon start chasing bird if the bird exist and if its close close enough
			if (m_bird != null)
			{
				m_falconPos.set(m_falcon.getPosition());
				m_birdPos.set(m_bird.getPosition());
				if(m_bird.m_state == Bird.State.Flying || m_bird.m_state == Bird.State.Jumping || m_bird.m_state == Bird.State.Landing || m_bird.m_state == Bird.State.FlyingShitting)
				{
					m_falconPos.set(m_falcon.getPosition());
					m_birdPos.set(m_bird.getPosition());
					float distToBird = m_falconPos.dist2D(m_birdPos);
					
					if (distToBird <= m_maxChasingDist )
					{
						int chasingChance = m_randChasingChance.nextInt(2);
						
						if (chasingChance == 1)
						{
							transitionTo( Chasing.class );
						}
					}
				}
			}
			else
			{
				m_bird = (Bird) m_world.findEntity(Bird.class);
			}
			
			// changing circling radius every once in a while
			m_wait = m_wait + deltaTime;
			if(m_wait >= m_radiusRandomizationPeriod)
			{
				m_wait = 0;
				
				int chance = m_randChasingChance.nextInt(2);
				if (chance ==1)
				{
					randomizeCirclingRange();
				}
			}
			
		}
			
		void randomizeCirclingRange()
		{
			float radius = ((float) Math.random() * 0.5f + 0.5f) * m_maxCirclingRadius;
			m_sb.begin().circle(m_circlingPt, radius).faceMovementDirection();
		}
		
		@Override
		public void onEvent( EntityEvent event ) 
		{
			if ( event instanceof CollisionEvent )
			{
				// if it collides with another entity, it attempts eating it
				Entity collider = ( (CollisionEvent)event ).m_collider;
				collider.sendEvent( Eaten.class );

			}
		}
	}
//---------------------------------------------------------------------------------------------
	
	class Chasing extends FSMState implements EntityEventListener
	{
		
		@Override
		public void activate()
		{
			m_falcon.m_state = Falcon.State.Chasing;	
			m_sb.begin().pursuit(m_bird).faceMovementDirection();
		}
		
		@Override
		public void deactivate()
		{
			m_sb.clear();
		}
		
		@Override
		public void execute( float deltaTime )
		{
			if (m_bird.m_state == Bird.State.Idle || m_bird.m_state == Bird.State.Shitting)
			{
				transitionTo( Hunting.class );
			}
			
		}
		
		@Override
		public void onEvent( EntityEvent event ) 
		{
			if ( event instanceof Shocked || event instanceof Shot)
			{
				die();
			}
			if ( event instanceof CollisionEvent )
			{
				// if it collides with another entity, it attempts eating it
				Entity collider = ( (CollisionEvent)event ).m_collider;
				collider.sendEvent( Eaten.class );

			}
		}
	}
	
	@Override
	public void onUpdate( float deltaTime )
	{
		m_sb.update(deltaTime);
	}
	
	void die()
	{
		m_falcon.m_world.removeEntity( m_falcon );
	}

}
