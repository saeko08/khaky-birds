/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.crap;

import com.hypefoundry.engine.controllers.fsm.FSMState;
import com.hypefoundry.engine.controllers.fsm.FiniteStateMachine;
import com.hypefoundry.engine.physics.events.CollisionEvent;
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
public class GranadeCrapAI extends FiniteStateMachine
{
	private GranadeCrap				m_crap;
	private World 					m_world;
	private SteeringBehaviors		m_sb;

	/**
	 * Constructor
	 * @param entity
	 */
	public GranadeCrapAI(World world,Entity entity) 
	{
		super(entity);
		
		m_crap = (GranadeCrap)entity;
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
		@Override
		public void activate()
		{
			m_crap.m_state = GranadeCrap.State.Hitting;
		
		}
		
		@Override
		public void execute( float deltaTime )
		{
			if (m_crap.m_canHit == false)
			{
				die();
			}
			
		}
		
		@Override
		public void onEvent( EntityEvent event ) 
		{
			if ( event instanceof CollisionEvent )
			{
				// if it collides with Pedestrian it splats
				Entity collider = ( (CollisionEvent)event ).m_collider;
				if(collider instanceof Crappable)
				{
					collider.sendEvent( Crapped.class );
					transitionTo( Splat.class );
				}

			}
		}
	}
	//--------------------------------------------------------------------------------
	class Falling extends FSMState
	{
		@Override
		public void activate()
		{
			m_crap.m_state = GranadeCrap.State.Falling;		
		}
		
		@Override
		public void execute( float deltaTime )
		{
			if (m_crap.m_canHit == true)
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
			m_crap.m_state = GranadeCrap.State.Splat;
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
			
			if ( m_fallingTime >= 0.25f )
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
