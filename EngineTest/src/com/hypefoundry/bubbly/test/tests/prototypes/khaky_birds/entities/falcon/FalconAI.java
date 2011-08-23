/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.falcon;

import java.util.List;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird.Bird;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.shock.Shocked;
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
	private float 				m_speed         = 55.f;
	private float 				m_speedChaser   = 40.f;
	private Bird                m_bird          = null;
	private SteeringBehaviors	m_sb;
	
	private Vector3 			m_direction = new Vector3();
	
	
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
		m_falcon.registerEvent( OutOfWorldBounds.class, new EventFactory< OutOfWorldBounds >() { @Override public OutOfWorldBounds createObject() { return new OutOfWorldBounds (); } } );
		m_falcon.registerEvent( CollisionEvent.class, new EventFactory< CollisionEvent >() { @Override public CollisionEvent createObject() { return new CollisionEvent (); } } );
			
	}
	
	//states definitions:
	//-----------------------------------------------------------
	class Hunting extends FSMState implements EntityEventListener
	{
		
		private	Vector3 m_goToPos  = new Vector3();
		
		@Override
		public void activate()
		{
			m_falcon.m_state = Falcon.State.Hunting;
			m_falcon.attachEventListener( this );
			
			if (m_falcon.m_isChasing == true)
			{
				transitionTo( Chasing.class );
			}
			
			else if (m_falcon.m_flyingFromLeft == true)
			{
				m_goToPos.set( m_falcon.getPosition() );
				m_goToPos.m_x = 6;
				m_sb.begin().seek( m_goToPos ).faceMovementDirection();
				
				
			}
			else
			{
				m_goToPos.set( m_falcon.getPosition() );
				m_goToPos.m_x = -1;
				m_sb.begin().seek( m_goToPos ).faceMovementDirection();
	
			}
		}
		
		@Override
		public void deactivate()
		{
			m_falcon.detachEventListener( this );
			m_sb.clear();
		}
		
		@Override
		public void onEvent( EntityEvent event ) 
		{
			if ( event instanceof OutOfWorldBounds)
			{
				float borderPos = m_falcon.getWorldBounds().m_minX;
				
				
				if ( m_falcon.m_flyingFromLeft == true && borderPos > 0)
				{
					// ok - we can remove the falcon - it went out of the visibility scope
					die();
				}
				else if ( m_falcon.m_flyingFromLeft == false && borderPos <= 0)
				{
					// ok - we can remove the falcon - it went out of the visibility scope
					die();
				}
				
			}
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
		
		private	Vector3 m_goToPos       = new Vector3();
		private boolean m_chasingActive = false;
		
		@Override
		public void activate()
		{
			m_falcon.m_state = Falcon.State.Chasing;
			m_falcon.attachEventListener( this );
				
		}
		
		@Override
		public void deactivate()
		{
			m_falcon.detachEventListener( this );
			m_sb.clear();
			m_chasingActive = false;
		}
		
		@Override
		public void execute( float deltaTime )
		{
			
			if (m_chasingActive == false)
			{
				m_chasingActive = true;
				m_goToPos.set( m_bird.getPosition() );
				m_sb.begin().seek( m_goToPos ).faceMovementDirection();
			}
			
			if (m_goToPos != m_bird.getPosition())
			{
				m_sb.clear();
				m_goToPos.set( m_bird.getPosition() );
				m_sb.begin().seek( m_goToPos ).faceMovementDirection();
			}

			
			
		}
		
		@Override
		public void onEvent( EntityEvent event ) 
		{
			if ( event instanceof Shocked )
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
	/* (non-Javadoc)
	 * @see com.hypefoundry.engine.game.Updatable#update(float)
	 */
	/*@Override
	public void update(float deltaTime) 
	{
		// start moving the falcon
		
		if (m_falcon.m_isChasing == true)
		{
			if ( m_bird != null )
			{
				m_direction.set( m_bird.getPosition() ); // direction = birdPos 
				m_direction.sub( m_falcon.getPosition() ); // direction -= falconPos
				m_direction.normalize2D();
				
				float speedForThisFrame = m_speedChaser * deltaTime;
				m_direction.scale( speedForThisFrame );
				
				m_falcon.translate( m_direction );
			}
		}
		
		else if (m_falcon.m_flyingFromLeft == true)
		{
			float translation = m_speed * deltaTime;
			m_falcon.translate( +translation, 0, 0  );
			
			// once it goes out of the view scope, destroy it
			float borderPos = m_falcon.getWorldBounds().m_minX;
			
			
			if ( borderPos >= 330.0f)
			{
				// ok - we can remove the falcon - it went out of the visibility scope
				m_world.removeEntity( m_falcon );
			}
		}
		else
		{
			float translation = m_speed * deltaTime;
			m_falcon.translate( -translation, 0, 0  );
			
			// once it goes out of the view scope, destroy it
			float borderPos = m_falcon.getWorldBounds().m_maxX;
			
			if ( borderPos <= -10.f )
			{
				// ok - we can remove the falcon - it went out of the visibility scope
				m_world.removeEntity( m_falcon );
				
			}
		}
				

	}*/
	
	@Override
	public void onUpdate( float deltaTime )
	{
		m_sb.update();
	}
	
	void die()
	{
		m_falcon.m_world.removeEntity( m_falcon );
	}

}
