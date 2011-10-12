/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.crap;

import java.util.ArrayList;

import com.hypefoundry.khakyBirds.entities.crap.CrapAI.Falling;
import com.hypefoundry.khakyBirds.entities.crap.CrapAI.Hitting;
import com.hypefoundry.khakyBirds.entities.crap.CrapAI.Splat;
import com.hypefoundry.khakyBirds.entities.hideout.Hideout;
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
public class DemolisheCrapAI extends FiniteStateMachine
{
	private DemolisheCrap		m_crap;
	private World 				m_world;
	private SteeringBehaviors	m_sb;
	ArrayList<Crappable> 		m_victims;

	/**
	 * Constructor
	 * @param entity
	 */
	public DemolisheCrapAI(World world,Entity entity) 
	{
		super(entity);
		
		m_crap = (DemolisheCrap)entity;
		m_world = world;
		m_sb = new SteeringBehaviors( m_crap);
		
		m_victims = new ArrayList<Crappable>();
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
			m_crap.m_state = DemolisheCrap.State.Hitting;
			
			m_world.findEntitiesInRange(Crappable.class, 1.0f, m_crap.getPosition(), m_victims);
			if (m_victims.isEmpty()== false)
			{
				for ( int i = 0 ; i < m_victims.size(); ++i )
				{
					Entity victim =  (Entity) m_victims.get(i);
					victim.sendEvent( Crapped.class );
				}
				
			}
			transitionTo( Splat.class );
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
				if(collider instanceof  Hideout)
				{
					collider.sendEvent( Demolishe.class );
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
			m_crap.m_state = DemolisheCrap.State.Falling;		
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
			m_crap.m_state = DemolisheCrap.State.Splat;
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
