/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.hideout;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.crap.Demolishe;
import com.hypefoundry.engine.controllers.fsm.FSMState;
import com.hypefoundry.engine.controllers.fsm.FiniteStateMachine;
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
public class HideoutAI extends FiniteStateMachine
{
	private World 					m_world;
	private Hideout					m_hideout;
	private SteeringBehaviors 		m_sb;
	private  float 					m_pedestrianRespawnTime		= 10f;
	private float					m_pedestrianCounter			= 0f;
	private float 					m_perkPedestrianRespawnTime	= 10f;
	private float					m_perkPedestrianCounter		= 0f;
	
	private final float 			m_panicTime	= 4f;

	
	
	//----------------------------------------------------------------------------
	
	class Default extends FSMState implements EntityEventListener
	{
		@Override
		public void activate()
		{
			m_hideout.m_state = Hideout.State.Default;
			m_pedestrianRespawnTime		= 10f;
			m_perkPedestrianRespawnTime	= 10f;
		
		}
		@Override
		public void deactivate()
		{
			m_pedestrianCounter = 0;
			m_perkPedestrianCounter = 0;
		}
		
		@Override
		public void execute( float deltaTime )
		{
			m_pedestrianCounter += deltaTime;
			if (m_pedestrianCounter >= m_pedestrianRespawnTime )
			{
				if(m_hideout.m_pedestrians > 0)
				{
					m_hideout.goOut();
					m_hideout.m_pedestrians -= 1;
					m_pedestrianCounter = 0;
					
				}
				else
				{
					m_pedestrianCounter = 0;
				}
			}
			if(m_hideout.m_maxPerkPedestrianNumber > 0)
			{
				m_perkPedestrianCounter += deltaTime;
				
				if (m_perkPedestrianCounter >= m_perkPedestrianRespawnTime )
				{
					
					if(m_hideout.m_perkPedestrians < m_hideout.m_maxPerkPedestrianNumber)
					{
						m_hideout.perkPedestrianGoOut();
						m_perkPedestrianCounter = 0;
						
					}
					else
					{
						m_perkPedestrianCounter = 0;
					}
				}
			}
			
		}
		@Override
		public void onEvent(EntityEvent event) 
		{
			if ( event instanceof Demolishe )
			{
				transitionTo( Bombed.class );
				m_hideout.bombed();
			}
			
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	class Bombed extends FSMState
	{
		private float 	m_wait = 0;
		
		
		@Override
		public void activate()
		{
			m_hideout.m_state = Hideout.State.Bombed;
			m_pedestrianRespawnTime		= 0.5f;
			m_perkPedestrianRespawnTime	= 0.5f;
		
		}
		
		@Override
		public void deactivate()
		{
			m_wait = 0;
			m_pedestrianCounter = 0;
			m_perkPedestrianCounter = 0;
		}
		
		@Override
		public void execute( float deltaTime )
		{
			m_wait += deltaTime;
			if (m_wait < m_panicTime || m_hideout.m_pedestrians > 0 && m_hideout.m_perkPedestrians > 0 )
			{
				m_pedestrianCounter += deltaTime;
				if (m_pedestrianCounter >= m_pedestrianRespawnTime )
				{
					if(m_hideout.m_pedestrians > 0)
					{
						m_hideout.goOut();
						m_hideout.m_pedestrians -= 1;
						m_pedestrianCounter = 0;
						
					}
					else
					{
						m_pedestrianCounter = 0;
					}
				}
				if(m_hideout.m_maxPerkPedestrianNumber > 0)
				{
					m_perkPedestrianCounter += deltaTime;
					
					if (m_perkPedestrianCounter >= m_perkPedestrianRespawnTime )
					{
						
						if(m_hideout.m_perkPedestrians < m_hideout.m_maxPerkPedestrianNumber)
						{
							m_hideout.perkPedestrianGoOut();
							m_perkPedestrianCounter = 0;
							
						}
						else
						{
							m_perkPedestrianCounter = 0;
						}
					}
				}
			}
			
			else
			{
				transitionTo( Default.class );
			}
			
		}
	}
	

	/**
	 * Constructor
	 * @param entity
	 */
	public HideoutAI( World world, Entity hideout ) 
	{
		super(hideout);
		
		m_hideout = (Hideout)hideout;
		m_world = world;
		m_sb = new SteeringBehaviors( m_hideout);
		
		// define events the entity responds to
		m_hideout.registerEvent( Demolishe.class, new EventFactory< Demolishe >() { @Override public Demolishe createObject() { return new Demolishe (); } } );
		
		register (new Default());
		register (new Bombed ());
	
		begin(Default.class);
		
	}
	
	@Override
	public void onUpdate(float deltaTime) 
	{
		m_sb.update(deltaTime);
	}


}
