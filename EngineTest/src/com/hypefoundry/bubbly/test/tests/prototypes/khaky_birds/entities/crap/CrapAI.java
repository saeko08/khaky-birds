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
		
		begin( Falling.class );
		
		// define events the entity responds to
	
		m_crap.registerEvent( CollisionEvent.class, new EventFactory< CollisionEvent >() { @Override public CollisionEvent createObject() { return new CollisionEvent (); } } );
			
	}
	
	//----------------------------------------------------------------------------
	
	class Falling extends FSMState implements EntityEventListener
	{
		
		private	Vector3 m_goToPos       = new Vector3();
		
		@Override
		public void activate()
		{
			m_crap.m_state = Crap.State.Falling;
			m_crap.attachEventListener( this );
			m_goToPos = m_crap.getPosition();
			m_goToPos.m_z = 90;
			
			m_sb.begin().seek( m_goToPos );
				
		}
		
		@Override
		public void deactivate()
		{
			m_crap.detachEventListener( this );
			m_sb.clear();
		}
		
		@Override
		public void execute( float deltaTime )
		{
			// once it goes out of the view scope, destroy it
			float bottomPos = m_crap.getWorldBounds().m_maxZ;
			
			if ( bottomPos >= 82.0f )
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
				transitionTo( Hitting.class );

			}
		}
	}
	//--------------------------------------------------------------------------------
	class Hitting extends FSMState
	{
		
		private	Vector3 m_goToPos       = new Vector3();
		
		@Override
		public void activate()
		{
			m_crap.m_state = Crap.State.Hitting;
			m_goToPos = m_crap.getPosition();
			m_goToPos.m_z = 90;
			
			m_sb.begin().seek( m_goToPos );
				
		}
		
		@Override
		public void deactivate()
		{
			m_sb.clear();
		}
		
		@Override
		public void execute( float deltaTime )
		{
			// once it goes out of the view scope, destroy it
			float bottomPos = m_crap.getWorldBounds().m_maxZ;
			
			if ( bottomPos >= 85.0f )
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
