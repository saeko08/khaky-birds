/**
 * 
 */
package com.hypefoundry.kabloons.entities.player;

import java.util.List;

import com.hypefoundry.engine.controllers.fsm.FSMState;
import com.hypefoundry.engine.controllers.fsm.FiniteStateMachine;
import com.hypefoundry.engine.core.Input;
import com.hypefoundry.engine.core.Input.TouchEvent;
import com.hypefoundry.engine.game.InputHandler;
import com.hypefoundry.engine.hud.ButtonListener;
import com.hypefoundry.engine.hud.HudLayout;
import com.hypefoundry.engine.hud.widgets.checkbox.CheckboxWidget;
import com.hypefoundry.engine.hud.widgets.image.ImageWidget;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.World;
import com.hypefoundry.kabloons.GameScreen;
import com.hypefoundry.kabloons.entities.baloon.Baloon;
import com.hypefoundry.kabloons.entities.fan.Fan;
import com.hypefoundry.kabloons.utils.AssetsFactory;


/**
 * @author Paksas
 *
 */
public class PlayerController extends FiniteStateMachine
{
	private Player				m_player;
	private GameScreen 			m_screen;
	private World				m_world;
	private Camera2D			m_camera;
	private AssetsFactory 		m_assetsFactory;
	
	private Fan.Direction 		m_fanBlowDirection = Fan.Direction.None;
	
	// ------------------------------------------------------------------------
	// States
	// ------------------------------------------------------------------------
	
	/**
	 * Main gameplay mode.
	 * 
	 * @author Paksas
	 */
	class Gameplay extends FSMState implements InputHandler, ButtonListener
	{
		// baloon related data
		private Vector3				m_baloonReleasePos = new Vector3( 2.4f, -0.2f, 0.0f );
		private Baloon				m_baloon;
		
		private Vector3				m_touchPos = new Vector3();
		private HudLayout			m_hudLayout;
		
		@Override
		public void activate()
		{
			// create a hud
			if ( m_hudLayout == null )
			{
				m_hudLayout = m_screen.getResourceManager().getResource( HudLayout.class, "hud/gameplay/gameHud.xml" );
				m_hudLayout.attachRenderer( m_screen.m_hudRenderer ); 
				m_hudLayout.attachButtonListener( this );
				
				updateFanCounters();
			}
			
			m_screen.registerInputHandler( this );
		}
		
		@Override
		public void deactivate()
		{
			m_hudLayout.detachButtonListener( this );
			m_hudLayout.detachRenderer( m_screen.m_hudRenderer ); 
			m_screen.unregisterInputHandler( this );
		}
		
		@Override
		public void execute( float deltaTime )
		{
			if ( m_baloon == null )
			{
				// baloon hasn't been released yet
				return;
			}
			
			// monitor baloon's state
			if ( m_baloon.isAlive() == false )
			{
				// baloon was destroyed
				transitionTo( Failure.class );
			}
			else if ( m_baloon.isSafe() == true )
			{
				// baloon reached safety
				transitionTo( Success.class );
			}
		}
		
		@Override
		public boolean handleInput( Input input, float deltaTime ) 
		{	
			List< TouchEvent > inputEvents = input.getTouchEvents();
			
			// first check if we received a double tap event - if so, discard the rest
			int count = inputEvents.size();
			for ( int i = 0 ; i < count; ++i )
			{	
				// don't bother if we don't have any fans that blow in the specified direction left
				if ( m_player.m_fansLeft[m_fanBlowDirection.m_idx] <= 0 )
				{
					break;
				}
				
				TouchEvent lastEvent = inputEvents.get(i);
				if ( lastEvent.type == TouchEvent.TOUCH_DOWN )
				{
					// change the gesture direction from screen to model space
					m_touchPos.set( lastEvent.x, lastEvent.y, 0 );
					m_camera.screenPosToWorld( m_touchPos, m_touchPos );
					
					// decrease the number of fans we've left to place
					m_player.m_fansLeft[m_fanBlowDirection.m_idx]--;
					
					// place the fan
					Fan fan = new Fan( m_touchPos );
					m_assetsFactory.initializeFan( fan, m_fanBlowDirection );
					m_world.addEntity( fan );
					
					// update the hud
					updateFanCounters();
				}
			}
			
			return false;
		}
		
		@Override
		public void onButtonPressed( String buttonId ) 
		{
			if ( buttonId.equals( "LeftFan" ) )
			{	
				// disable the other button
				CheckboxWidget rightFanCheckbox = m_hudLayout.getWidget( CheckboxWidget.class, "RightFan" );
				rightFanCheckbox.setChecked( false );
				
				// set the respective fan direction
				m_fanBlowDirection = Fan.Direction.Left;
			}
			else if ( buttonId.equals( "RightFan" ) )
			{
				// disable the other button
				CheckboxWidget leftFanCheckbox = m_hudLayout.getWidget( CheckboxWidget.class, "LeftFan" );
				leftFanCheckbox.setChecked( false );
				
				// set the respective fan direction
				m_fanBlowDirection = Fan.Direction.Right;
			}
			else if (buttonId.equals( "ReleaseBaloon" ) )
			{
				// release a single baloon
				m_baloon = m_assetsFactory.createRandomBaloon( m_baloonReleasePos );
				m_screen.m_world.addEntity( m_baloon );
			}
		}
		
		/**
		 * A helper method that updates the fan counters on the hud
		 */
		private void updateFanCounters()
		{
			ImageWidget rightFansCounter = m_hudLayout.getWidget( ImageWidget.class, "RightFansCounter" );
			rightFansCounter.m_caption = Integer.toString( m_player.m_fansLeft[ Fan.Direction.Right.m_idx ] );
					
			ImageWidget leftFansCounter = m_hudLayout.getWidget( ImageWidget.class, "LeftFansCounter" );
			leftFansCounter.m_caption = Integer.toString( m_player.m_fansLeft[ Fan.Direction.Left.m_idx ] );
		}
	}
	
	/**
	 * Player completed the level successfully.
	 * 
	 * @author Paksas
	 */
	class Success extends FSMState implements ButtonListener
	{
		// baloon related data
		private HudLayout			m_hudLayout;
		
		@Override
		public void activate()
		{
			// create a hud
			if ( m_hudLayout == null )
			{
				m_hudLayout = m_screen.getResourceManager().getResource( HudLayout.class, "hud/gameplay/winnerHud.xml" );
				m_hudLayout.attachRenderer( m_screen.m_hudRenderer ); 
				m_hudLayout.attachButtonListener( this );
			}
		}

		@Override
		public void onButtonPressed( String buttonId ) 
		{
			if ( buttonId.equals( "ExitToMenu" ) )
			{	
				m_screen.exitToMenu();
			}
			else if ( buttonId.equals( "NextLevel" ) )
			{
				m_screen.loadNextLevel();
			}
		}	
	}
	
	/**
	 * Player failed to complete the level.
	 * 
	 * @author Paksas
	 */
	class Failure extends FSMState implements ButtonListener
	{
		// baloon related data
		private HudLayout			m_hudLayout;
		
		@Override
		public void activate()
		{
			// create a hud
			if ( m_hudLayout == null )
			{
				m_hudLayout = m_screen.getResourceManager().getResource( HudLayout.class, "hud/gameplay/looserHud.xml" );
				m_hudLayout.attachRenderer( m_screen.m_hudRenderer ); 
				m_hudLayout.attachButtonListener( this );
			}
		}

		@Override
		public void onButtonPressed( String buttonId ) 
		{
			if ( buttonId.equals( "ExitToMenu" ) )
			{	
				m_screen.exitToMenu();
			}
			else if ( buttonId.equals( "RetryLevel" ) )
			{
				m_screen.reloadLevel();
			}
		}	
	}
	
	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------
	
	/**
	 * Constructor.
	 * 
	 * @param screen
	 */
	public PlayerController( GameScreen screen, Entity playerEntity ) 
	{
		super( playerEntity );
		
		m_screen = screen;
		m_player = (Player)playerEntity;
		m_world = screen.m_world;
		m_camera = screen.m_worldRenderer.getCamera();
		m_assetsFactory = screen.m_assetsFactory;
		
			
		// register states
		register( new Gameplay() );
		register( new Success() );
		register( new Failure() );
		begin( Gameplay.class );
	}
}
