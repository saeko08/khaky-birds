/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.crap;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird.Bird;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.falcon.Eaten;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.falcon.Falcon;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.pedestrian.Pedestrian;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.shock.Shocked;
import com.hypefoundry.engine.controllers.EntityController;
import com.hypefoundry.engine.controllers.fsm.FSMState;
import com.hypefoundry.engine.controllers.fsm.FiniteStateMachine;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.events.CollisionEvent;
import com.hypefoundry.engine.physics.events.OutOfWorldBounds;
import com.hypefoundry.engine.physics.locomotion.SteeringBehaviors;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityEvent;
import com.hypefoundry.engine.world.EntityEventListener;
import com.hypefoundry.engine.world.EventFactory;
import com.hypefoundry.engine.world.World;

/**
 * Controls crap drop
 * 
 * @author azagor
 *
 */
public class CrapAI extends FiniteStateMachine
{
	private Crap				m_crap;
	private World 				m_world;
	private SteeringBehaviors	m_sb;
	private Pedestrian          m_pedestrian         = null;

	/**
	 * Constructor
	 * @param entity
	 */
	public CrapAI(World world,Entity entity) 
	{
		super(entity);
		
		m_crap = (Crap)entity;
		m_world = world;
		m_sb = new SteeringBehaviors( m_crap);
		
		//register states
		
		register( new Falling() );
		register( new Hitting() );
		register( new Splat() );
		
		begin( Falling.class );
		
		// define events the entity responds to
	
		m_crap.registerEvent( CollisionEvent.class, new EventFactory< CollisionEvent >() { @Override public CollisionEvent createObject() { return new CollisionEvent (); } } );
			
	}
	
	//----------------------------------------------------------------------------
	
	class Hitting extends FSMState implements EntityEventListener
	{
		
		private float m_fallingTime     = 0;
		
		
		@Override
		public void activate()
		{
			m_crap.m_state = Crap.State.Hitting;
			m_crap.attachEventListener( this );
		
		}
		
		@Override
		public void deactivate()
		{
			m_crap.detachEventListener( this );
			m_fallingTime     = 0;
		}
		
		@Override
		public void execute( float deltaTime )
		{
			m_fallingTime     = m_fallingTime + deltaTime;
			if ( m_fallingTime  >= 0.1f )
			{
				// ok - we can remove the crap - it went out of the visibility scope
				die();
			}
			
		}
		
		@Override
		public void onEvent( EntityEvent event ) 
		{
			if ( event instanceof CollisionEvent )
			{
				// if it collides with another entity, it attempts eating it
				Entity collider = ( (CollisionEvent)event ).m_collider;
				collider.sendEvent( Crapped.class );
				transitionTo( Splat.class );

			}
		}
	}
	//--------------------------------------------------------------------------------
	class Falling extends FSMState
	{
		private float m_fallingTime     = 0;
	
		
		@Override
		public void activate()
		{
			m_crap.m_state = Crap.State.Falling;
				
		}
		
		@Override
		public void deactivate()
		{
			m_fallingTime     = 0;
		}
		
		@Override
		public void execute( float deltaTime )
		{
			m_fallingTime     = m_fallingTime + deltaTime;
			
			if ( m_fallingTime >= 1.5f )
			{
				transitionTo( Hitting.class );
			}
			
		}
	}
	//--------------------------------------------------------------------------------
	
	class Splat extends FSMState
	{
		private float m_fallingTime     = 0;
	
		
		@Override
		public void activate()
		{
			m_crap.m_state = Crap.State.Splat;
				
		}
		
		@Override
		public void deactivate()
		{
			m_fallingTime     = 0;
		}
		
		@Override
		public void execute( float deltaTime )
		{
			m_fallingTime     = m_fallingTime + deltaTime;
			
			if ( m_fallingTime >= 0.2f )
			{
				// ok - we can remove the crap - it went out of the visibility scope
				die();
			}
			
		}
	}
	void die()
	{
		m_world.removeEntity( m_crap );
	}
	

	@Override
	public void onUpdate( float deltaTime )
	{
		m_sb.update(deltaTime);
	}

}
