/**
 * 
 */
package com.hypefoundry.kabloons.entities.player;

import java.util.*;

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
import com.hypefoundry.kabloons.entities.background.AnimatedBackground;
import com.hypefoundry.kabloons.entities.baloon.Baloon;
import com.hypefoundry.kabloons.entities.fan.Fan;
import com.hypefoundry.kabloons.utils.AssetsFactory;

/**
 * Manages a group of checkboxes of which only one should be checked at any time.
 * 
 * @author Paksas
 */
class RadioGroup implements ButtonListener
{
	private List< CheckboxWidget > 		m_widgets 				= new ArrayList< CheckboxWidget >();	
	
	// currently selected checkbox
	CheckboxWidget 						m_selectedCheckbox; 
	
			
	/**
	 * Adds a new checkbox to the radio group
	 *  
	 * @param checkbox
	 */
	void addCheckbox( CheckboxWidget checkbox )
	{
		if ( checkbox != null )
		{
			m_widgets.add( checkbox );
			checkbox.setChecked( false );
		}
	}

	@Override
	public void onButtonPressed( String id ) 
	{
		// if one of the registered checkboxes was clicked,
		// make sure it stays checked, and all the other checkbox
		// become unchecked
		int count = m_widgets.size();
		for ( int i = 0; i < count; ++i )
		{
			CheckboxWidget checkbox = m_widgets.get( i );
			
			if ( checkbox.m_id == id )
			{
				// yup - it's one of ours
				m_selectedCheckbox = checkbox;
				break;
			}
		}
		
		// if it was one of ours, disable the rest and make this the only checked box
		if ( m_selectedCheckbox != null )
		{
			for ( int i = 0; i < count; ++i )
			{
				m_widgets.get( i ).setChecked( false );
			}
			
			m_selectedCheckbox.setChecked( true );
		}
	}
}

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
	
	enum EditorMode
	{
		DoNothing,
		AddLeftFan,
		AddRightFan,
		RemoveFan
	}
	
	private EditorMode 		m_editMode = EditorMode.DoNothing;
	
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
		private RadioGroup			m_radioGroup;
		
		
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
				setupRadioGroup();
			}
			
			m_screen.registerInputHandler( this );
		}
		
		@Override
		public void deactivate()
		{
			m_radioGroup = null;
			
			m_hudLayout.detachButtonListener( m_radioGroup );
			m_hudLayout.detachButtonListener( this );
			m_hudLayout.detachRenderer( m_screen.m_hudRenderer ); 
			m_hudLayout = null;
			
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
			switch( m_editMode )
			{
				case AddLeftFan:
				{
					addFan( input, Fan.Direction.Left );
					break;
				}
				
				case AddRightFan:
				{
					addFan( input, Fan.Direction.Right );
					break;
				}
				
				case RemoveFan:
				{
					removeFan( input );
					break;
				}
			}
			
			return false;
		}
		
		/**
		 * Places a new fan in the world in response to the user's input.
		 * 
		 * @param input
		 * @param direction
		 */
		private void addFan( Input input, Fan.Direction direction )
		{	
			List< TouchEvent > inputEvents = input.getTouchEvents();
			
			int count = inputEvents.size();
			for ( int i = 0 ; i < count; ++i )
			{	
				// don't bother if we don't have any fans that blow in the specified direction left
				if ( m_player.m_fansLeft[direction.m_idx] <= 0 )
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
					m_player.m_fansLeft[direction.m_idx]--;
					
					// place the fan
					Fan fan = new Fan( m_touchPos );
					m_assetsFactory.initializeFan( fan, direction );
					m_world.addEntity( fan );
					
					// play proper effects
					playFanEditionEffect( fan );
					
					// update the hud
					updateFanCounters();
					
					break;
				}
			}
		}
		
		/**
		 * Removes a previously placed fan from the world in response to the user's input.
		 * 
		 * @param input
		 * @param direction
		 */
		private void removeFan( Input input )
		{	
			List< TouchEvent > inputEvents = input.getTouchEvents();
			int count = inputEvents.size();
			for ( int i = 0 ; i < count; ++i )
			{					
				TouchEvent lastEvent = inputEvents.get(i);
				if ( lastEvent.type == TouchEvent.TOUCH_DOWN )
				{
					// change the gesture direction from screen to model space
					m_touchPos.set( lastEvent.x, lastEvent.y, 0 );
					m_camera.screenPosToWorld( m_touchPos, m_touchPos );
					
					// get the entity that's located on the clicked location
					Fan clickedFan = m_world.findNearestEntity( Fan.class, 0.2f, m_touchPos );
					if ( clickedFan != null && clickedFan.m_wasCreatedByUser )
					{
						m_player.m_fansLeft[clickedFan.getBlowDirection().m_idx]++;
						m_world.removeEntity( clickedFan );
						
						// play proper effects
						playFanEditionEffect( clickedFan );
					}
					
					// update the hud
					updateFanCounters();
					
					break;
				}
			}
		}
		
		
		@Override
		public void onButtonPressed( String buttonId ) 
		{
			if ( buttonId.equals( "LeftFan" ) )
			{	
				m_editMode = EditorMode.AddLeftFan;
			}
			else if ( buttonId.equals( "RightFan" ) )
			{
				m_editMode = EditorMode.AddRightFan;
			}
			else if ( buttonId.equals( "RemoveFan" ) )
			{
				m_editMode = EditorMode.RemoveFan;
			}
			else if ( buttonId.equals( "ReleaseBaloon" ) )
			{
				if ( m_baloon == null )
				{
					// release a single baloon
					m_baloon = m_assetsFactory.createRandomBaloon( m_baloonReleasePos );
					m_screen.m_world.addEntity( m_baloon );
				}
				
				// keep the button checked no matter how many times you click it
				CheckboxWidget checkbox = m_hudLayout.getWidget( CheckboxWidget.class, "ReleaseBaloon" );
				checkbox.setChecked( true );
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
		
		/**
		 * Sets up a radio group of checkboxes.
		 */
		private void setupRadioGroup()
		{
			m_radioGroup = new RadioGroup();
			
			m_radioGroup.addCheckbox( m_hudLayout.getWidget( CheckboxWidget.class, "LeftFan" ) );
			m_radioGroup.addCheckbox( m_hudLayout.getWidget( CheckboxWidget.class, "RightFan" ) );
			m_radioGroup.addCheckbox( m_hudLayout.getWidget( CheckboxWidget.class, "RemoveFan" ) );
			
			m_hudLayout.attachButtonListener( m_radioGroup );
		}
		
		/**
		 * Plays the effects associated with a fan appearing or disappearing from the screen
		 */
		private void playFanEditionEffect( Fan fan )
		{
			// spawn the puff effect
			AnimatedBackground puffEffect = new AnimatedBackground();
			m_assetsFactory.initializePuff( puffEffect, fan.getPosition() );
			m_world.addEntity( puffEffect );
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
			
			// unlock the next level
			m_screen.unlockNextLevel();
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
