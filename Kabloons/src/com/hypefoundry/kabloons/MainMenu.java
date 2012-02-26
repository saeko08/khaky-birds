/**
 * 
 */
package com.hypefoundry.kabloons;

import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

import com.hypefoundry.engine.controllers.ControllersView;
import com.hypefoundry.engine.core.Input;
import com.hypefoundry.engine.game.Game;
import com.hypefoundry.engine.game.Screen;
import com.hypefoundry.engine.hud.ButtonListener;
import com.hypefoundry.engine.hud.Hud;
import com.hypefoundry.engine.hud.HudLayout;
import com.hypefoundry.engine.hud.HudRenderer;
import com.hypefoundry.engine.hud.widgets.checkbox.CheckboxWidget;
import com.hypefoundry.engine.physics.PhysicsView;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.EntityVisualFactory;
import com.hypefoundry.engine.renderer2D.Renderer2D;
import com.hypefoundry.engine.util.serialization.xml.XMLDataLoader;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.World;
import com.hypefoundry.engine.world.serialization.EntityFactory;
import com.hypefoundry.kabloons.entities.background.Background;
import com.hypefoundry.kabloons.entities.background.BackgroundVisual;
import com.hypefoundry.kabloons.entities.background.FXBackground;
import com.hypefoundry.kabloons.entities.background.FXBackgroundVisual;


/**
 * @author Paksas
 *
 */
public class MainMenu extends Screen implements ButtonListener
{
	enum MenuScreen
	{
		MS_Main( "hud/menu/mainScreen/world.xml" ),
		MS_Level_Selection( "hud/menu/levelSelection/world.xml" );
		
		public String m_worldPath;
		MenuScreen( String worldPath )
		{
			m_worldPath = worldPath;
		}
	}
	
	Input								m_input;
	
	public World						m_world;
	public Renderer2D					m_worldRenderer;
	ControllersView						m_controllersView;
	PhysicsView							m_physicsView;
	
	HudRenderer							m_hudRenderer;
	HudLayout 							m_hudLayout = null;
	
	// settings
	boolean								m_playSounds = true;
	
	/**
	 * Constructor.
	 * 
	 * @param game
	 * @param menuScreenId
	 */
	public MainMenu( Game game, MenuScreen menuScreenId ) 
	{
		super( game );
		
		m_input = game.getInput();
		
		// create the game world
		m_world = new World();
				
		// serialization support
		m_world.registerEntity( Background.class, new EntityFactory() { @Override public Entity create() { return new Background(); } } );
		m_world.registerEntity( FXBackground.class, new EntityFactory() { @Override public Entity create() { return new FXBackground(); } } );
		
		
		// load the world
		try 
		{
			InputStream worldFileStream = game.getFileIO().readAsset( menuScreenId.m_worldPath );
			m_world.load( XMLDataLoader.parse( worldFileStream, "World" ), m_resourceManager );
		} 
		catch ( IOException e ) 
		{
			Log.d( "Game", "Error while loading menu world" );
			throw new RuntimeException( e );
		}
				
		// create the views
		m_worldRenderer = new Renderer2D( game );
		m_physicsView = new PhysicsView( 2.0f ); // TODO: configure cell size
		m_controllersView = new ControllersView( this );
		
		// register visuals
		m_world.attachView( m_worldRenderer );
		m_worldRenderer.register( Background.class, new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new BackgroundVisual( m_resourceManager, parentEntity ); } } );
		m_worldRenderer.register( FXBackground.class, new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new FXBackgroundVisual( m_resourceManager, parentEntity ); } } );
		
		// register controllers
		m_world.attachView( m_controllersView );
			
		// register physics
		m_world.attachView( m_physicsView );

		// register the updatables
		addUpdatable( m_world );
		addUpdatable( m_physicsView );
		
		
		// load the HUD
		Hud hud = m_resourceManager.getResource( Hud.class, "hud/menu/mainMenuHudDefinition.xml" );
		if ( hud != null )
		{
			m_hudRenderer = new HudRenderer( game, hud );
			registerInputHandler( m_hudRenderer );
		}
	}

	// ------------------------------------------------------------------------
	// Screen implementation
	// ------------------------------------------------------------------------
	@Override
	public void present( float deltaTime ) 
	{			
		// draw the world contents ( but don't update the animations etc.
		// if the game is paused - wa want to have a still-frame effect )
		m_worldRenderer.draw( m_running ? deltaTime : 0 );
				
		// draw the hud contents
		if ( m_hudRenderer != null )
		{
			m_hudRenderer.draw( deltaTime );
		}
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
