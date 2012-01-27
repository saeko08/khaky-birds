/**
 * 
 */
package com.hypefoundry.kabloons;

import com.hypefoundry.engine.core.Input;
import com.hypefoundry.engine.game.Game;
import com.hypefoundry.engine.game.Screen;
import com.hypefoundry.engine.hud.ButtonListener;
import com.hypefoundry.engine.hud.Hud;
import com.hypefoundry.engine.hud.HudLayout;
import com.hypefoundry.engine.hud.HudRenderer;
import com.hypefoundry.engine.hud.widgets.checkbox.CheckboxWidget;


/**
 * @author Paksas
 *
 */
public class MainMenu extends Screen implements ButtonListener
{
	Input								m_input;
	HudRenderer							m_hudRenderer;
	HudLayout 							m_hudLayout = null;
	
	// settings
	boolean								m_playSounds = true;
	
	/**
	 * Constructor.
	 * 
	 * @param game
	 */
	public MainMenu( Game game ) 
	{
		super( game );
		
		m_input = game.getInput();
		
		// load the HUD
		Hud hud = m_resourceManager.getResource( Hud.class, "hud/menu/mainMenuHudDefinition.xml" );
		if ( hud == null )
		{
			throw new RuntimeException( "No HUD definition" );
		}
		
		m_hudRenderer = new HudRenderer( game, hud );
		registerInputHandler( m_hudRenderer );
		
		// load the menu layout
		if ( m_hudLayout == null )
		{
			m_hudLayout = m_resourceManager.getResource( HudLayout.class, "hud/menu/mainMenu.xml" );
			m_hudLayout.attachRenderer( m_hudRenderer ); 
			m_hudLayout.attachButtonListener( this );
		}
	}

	// ------------------------------------------------------------------------
	// Screen implementation
	// ------------------------------------------------------------------------
	@Override
	public void present( float deltaTime ) 
	{			
		// draw the hud contents
		m_hudRenderer.draw( deltaTime );
	}

	@Override
	public void pause() 
	{
		m_resourceManager.releaseResources();
	}

	@Override
	public void resume() 
	{		
		m_resourceManager.loadResources();
	}

	@Override
	public void dispose() 
	{
	}

	// ------------------------------------------------------------------------
	// ButtonListener implementation
	// ------------------------------------------------------------------------
	@Override
	public void onButtonPressed( String id ) 
	{
		if ( id.equals( "Toggle Sound" ) )
		{
			CheckboxWidget toggleSoundCheckbox = m_hudLayout.getWidget( CheckboxWidget.class, "ToggleSoundIcon" );
			if ( toggleSoundCheckbox != null )
			{
				// toggle sounds playing
				m_playSounds = toggleSoundCheckbox.isChecked();
			}
		}
		else if ( id.equals( "StartGame" ) )
		{
			m_hudLayout.detachButtonListener( this );
			m_hudLayout = m_resourceManager.getResource( HudLayout.class, "hud/menu/missionSelectionMenu.xml" );
			m_hudLayout.attachRenderer( m_hudRenderer );
			m_hudLayout.attachButtonListener( this );
		}
		else if ( id.equals( "MainMenu" ) )
		{
			m_hudLayout.detachButtonListener( this );
			m_hudLayout = m_resourceManager.getResource( HudLayout.class, "hud/menu/mainMenu.xml" );
			m_hudLayout.attachRenderer( m_hudRenderer );
			m_hudLayout.attachButtonListener( this );
		}
		else if ( id.equals( "Exit" ) )
		{
			m_game.closeGame();
		}
		else
		{
			// check if it's an integer number - if so, then the user selected a level to play
			try
			{
				
				int levelIdx = Integer.parseInt( id );
				m_game.setScreen( new GameScreen( m_game, levelIdx ) );
			}
			catch( NumberFormatException ex )
			{
				System.out.print( ex.toString() );
			}
		}
	}
}
