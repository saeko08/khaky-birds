/**
 * 
 */
package com.hypefoundry.kabloons.entities.tutorial;

import com.hypefoundry.engine.controllers.fsm.FSMState;
import com.hypefoundry.engine.controllers.fsm.FiniteStateMachine;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.World;
import com.hypefoundry.engine.world.WorldView;
import com.hypefoundry.kabloons.entities.baloon.Baloon;
import com.hypefoundry.kabloons.entities.fan.Fan;
import com.hypefoundry.kabloons.entities.player.Player;

/**
 * @author Paksas
 *
 */
public class StartTutorialController extends FiniteStateMachine
{
	private StartTutorial			m_tutorial;
	private World					m_world;
	private Player					m_player;
	
	// tells where the first fan was actually placed
	private Vector3					m_firstFanPosition;
	
	
	// ------------------------------------------------------------------------
	// States
	// ------------------------------------------------------------------------
	
	class TutorialStart extends FSMState implements WorldView
	{		
		@Override
		public void activate()
		{
			m_tutorial.m_state = StartTutorial.State.NOTHING;
			m_world.attachView( this );
		}
		
		@Override
		public void deactivate()
		{
			m_tutorial.m_state = StartTutorial.State.NOTHING;
			m_world.detachView( this );
		}
		
		@Override
		public void onAttached(World world) {}

		@Override
		public void onDetached(World world) {}

		@Override
		public void onEntityAdded(Entity entity) 
		{
			if ( entity instanceof Player )
			{
				m_player = (Player)entity;
				transitionTo( PlaceFirstFan.class );
			}
		}

		@Override
		public void onEntityRemoved(Entity entity) {}
	}
	
	// ------------------------------------------------------------------------
	
	class PlaceFirstFan extends FSMState implements WorldView
	{
		private boolean	 		m_fanPlaced;
		
		@Override
		public void activate()
		{
			m_tutorial.m_state = StartTutorial.State.PLACE_FIRST_FAN;
			m_tutorial.m_gesturePos.set( 2.4f, 4.0f, 0.0f );
			m_world.attachView( this );
			
			m_fanPlaced = false;

			// give the player a single fan - the one that's supposed to be placed,
			// and prevent the ghost release
			m_player.setFansCount( 1, 0 );
			m_player.enableGhostRelease( false );
		}
		
		@Override
		public void deactivate()
		{
			m_tutorial.m_state = StartTutorial.State.NOTHING;
			m_world.detachView( this );
		}
		
		@Override
		public void execute( float deltaTime )
		{
			if ( m_fanPlaced )
			{
				transitionTo( RemoveFan.class );
			}
		}

		@Override
		public void onAttached(World world) {}

		@Override
		public void onDetached(World world) {}

		@Override
		public void onEntityAdded(Entity entity) 
		{
			if ( entity instanceof Fan && !m_fanPlaced )
			{
				m_firstFanPosition = ((Fan)entity).getPosition();
				m_fanPlaced = true;
			}
		}

		@Override
		public void onEntityRemoved(Entity entity) {}
	}
	
	// ------------------------------------------------------------------------
	
	class RemoveFan extends FSMState implements WorldView
	{
		private boolean			m_fanRemoved;
		
		@Override
		public void activate()
		{
			m_tutorial.m_state = StartTutorial.State.REMOVE_FAN;
			
			// place the gesture exactly over the placed fan
			m_tutorial.m_gesturePos.set( m_firstFanPosition );
			
			m_world.attachView( this );
			
			m_fanRemoved = false;
			
			// take all the fans away, excluding the one already set
			// and prevent the ghost release
			m_player.setFansCount( 0, 0 );
			m_player.enableGhostRelease( false );
		}
		
		@Override
		public void deactivate()
		{
			m_tutorial.m_state = StartTutorial.State.NOTHING;
			m_world.detachView( this );
		}
		
		@Override
		public void execute( float deltaTime )
		{
			if ( m_fanRemoved )
			{
				transitionTo( PlaceSecondFan.class );
			}
		}
		
		@Override
		public void onAttached(World world) {}

		@Override
		public void onDetached(World world) {}

		@Override
		public void onEntityAdded(Entity entity) {}

		@Override
		public void onEntityRemoved(Entity entity) 
		{
			m_fanRemoved |= ( entity instanceof Fan );
		}
	}
	
	// ------------------------------------------------------------------------
	
	class PlaceSecondFan extends FSMState implements WorldView
	{
		private boolean	 		m_fanPlaced;
		
		@Override
		public void activate()
		{
			m_tutorial.m_state = StartTutorial.State.PLACE_SECOND_FAN;
			
			// place the fan so that it can counter the effects of the wind
			m_tutorial.m_gesturePos.set( 0.7f, 2.0f, 0.0f );
			
			m_world.attachView( this );
			
			m_fanPlaced = false;
			
			// give the player a single fan - the one that's supposed to be placed,
			// and prevent the release of a ghost
			m_player.setFansCount( 0, 1 );
			m_player.enableGhostRelease( false );
		}
		
		@Override
		public void deactivate()
		{
			m_tutorial.m_state = StartTutorial.State.NOTHING;
			m_world.detachView( this );
		}
		
		@Override
		public void execute( float deltaTime )
		{
			if ( m_fanPlaced )
			{
				transitionTo( ReleaseGhost.class );
			}
		}
		
		@Override
		public void onAttached(World world) {}

		@Override
		public void onDetached(World world) {}

		@Override
		public void onEntityAdded(Entity entity) 
		{
			m_fanPlaced |= ( entity instanceof Fan );
		}

		@Override
		public void onEntityRemoved(Entity entity) {}
	}
	
	// ------------------------------------------------------------------------
	
	class ReleaseGhost extends FSMState implements WorldView
	{
		private boolean			m_ghostReleased;
		
		@Override
		public void activate()
		{
			m_tutorial.m_state = StartTutorial.State.RELEASE_GHOST;
			m_tutorial.m_gesturePos.set( 2.4f, 4.0f, 0.0f );
			
			m_world.attachView( this );
			
			m_ghostReleased = false;
			
			// take all the fans away, excluding the one already set,
			// and allow to release of a ghost
			m_player.enableGhostRelease( true );
		}
		
		@Override
		public void deactivate()
		{
			m_tutorial.m_state = StartTutorial.State.NOTHING;
			m_world.detachView( this );
		}
		
		@Override
		public void execute( float deltaTime )
		{
			if ( m_ghostReleased )
			{
				transitionTo( TutorialEnd.class );
			}
		}
		
		@Override
		public void onAttached(World world) {}

		@Override
		public void onDetached(World world) {}

		@Override
		public void onEntityAdded(Entity entity) 
		{
			m_ghostReleased |= ( entity instanceof Baloon );
		}

		@Override
		public void onEntityRemoved(Entity entity) {}
	}
	
	// ------------------------------------------------------------------------
	
	class TutorialEnd extends FSMState
	{
		@Override
		public void activate()
		{
			m_tutorial.m_state = StartTutorial.State.NOTHING;
			
			// give back the missing fan ( the left one, 'cause the right
			// one is either already set on the screen, or is in the inventory )
			// and allow to release of a ghost
			m_player.changeFansCount( 1, 0 );
			m_player.enableGhostRelease( true );
		}
		
		@Override
		public void deactivate()
		{
			m_tutorial.m_state = StartTutorial.State.NOTHING;
		}
	}
	
	// ------------------------------------------------------------------------
	// API
	// ------------------------------------------------------------------------
	
	/**
	 * Constructor.
	 * 
	 * @param world
	 * @param entity
	 */
	public StartTutorialController( World world, Entity entity ) 
	{
		super( entity );
		
		m_tutorial = (StartTutorial)entity;
		
		m_world = world;
		
		register( new TutorialStart() );
		register( new PlaceFirstFan() );
		register( new RemoveFan() );
		register( new PlaceSecondFan() );
		register( new ReleaseGhost() );
		register( new TutorialEnd() );
		begin( TutorialStart.class );

	}
}

