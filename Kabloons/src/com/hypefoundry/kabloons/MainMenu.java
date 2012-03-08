/**
 * 
 */
package com.hypefoundry.kabloons;

import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

import com.hypefoundry.engine.controllers.ControllersView;
import com.hypefoundry.engine.controllers.EntityController;
import com.hypefoundry.engine.controllers.EntityControllerFactory;
import com.hypefoundry.engine.core.Input;
import com.hypefoundry.engine.game.Game;
import com.hypefoundry.engine.game.Screen;
import com.hypefoundry.engine.physics.PhysicalBody;
import com.hypefoundry.engine.physics.PhysicalBodyFactory;
import com.hypefoundry.engine.physics.PhysicsView;
import com.hypefoundry.engine.physics.bodies.CollisionBody;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.EntityVisualFactory;
import com.hypefoundry.engine.renderer2D.Renderer2D;
import com.hypefoundry.engine.util.serialization.xml.XMLDataLoader;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.World;
import com.hypefoundry.engine.world.serialization.EntityFactory;
import com.hypefoundry.kabloons.entities.background.AnimatedBackground;
import com.hypefoundry.kabloons.entities.background.AnimatedBackgroundVisual;
import com.hypefoundry.kabloons.entities.background.Background;
import com.hypefoundry.kabloons.entities.background.BackgroundVisual;
import com.hypefoundry.kabloons.entities.background.FXBackground;
import com.hypefoundry.kabloons.entities.background.FXBackgroundVisual;
import com.hypefoundry.kabloons.entities.levelsSelector.FogOfWar;
import com.hypefoundry.kabloons.entities.levelsSelector.FogOfWarVisual;
import com.hypefoundry.kabloons.entities.levelsSelector.LevelItem;
import com.hypefoundry.kabloons.entities.levelsSelector.LevelItemVisual;
import com.hypefoundry.kabloons.entities.levelsSelector.LevelSelectionManager;
import com.hypefoundry.kabloons.entities.levelsSelector.LevelSelectionManagerController;
import com.hypefoundry.kabloons.entities.menu.MenuItem;
import com.hypefoundry.kabloons.entities.menu.MenuItemController;
import com.hypefoundry.kabloons.entities.menu.MenuItemVisual;
import com.hypefoundry.kabloons.entities.menu.MenuManager;
import com.hypefoundry.kabloons.entities.menu.MenuManagerController;


/**
 * @author Paksas
 *
 */
public class MainMenu extends Screen
{
	enum MenuScreen
	{
		MS_Main( "hud/menu/mainScreen/world.xml" ),
		MS_Level_Selection( "hud/menu/levelSelection/world.xml" ),
		MS_Help( "hud/menu/help/world.xml" );
		
		public String m_worldPath;
		MenuScreen( String worldPath )
		{
			m_worldPath = worldPath;
		}
	}
	
	MenuScreen 							m_currentMenuScreenId;
	Input								m_input;
	
	public World						m_world;
	public Renderer2D					m_worldRenderer;
	ControllersView						m_controllersView;
	PhysicsView							m_physicsView;
		
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
		
		m_currentMenuScreenId = menuScreenId;
		final MainMenu menuScreen = this;
		
		m_input = game.getInput();
		
		// create the game world
		m_world = new World();
				
		// serialization support
		m_world.registerEntity( Background.class, new EntityFactory() { @Override public Entity create() { return new Background(); } } );
		m_world.registerEntity( AnimatedBackground.class, new EntityFactory() { @Override public Entity create() { return new AnimatedBackground(); } } );
		m_world.registerEntity( FXBackground.class, new EntityFactory() { @Override public Entity create() { return new FXBackground(); } } );
		m_world.registerEntity( MenuManager.class, new EntityFactory() { @Override public Entity create() { return new MenuManager(); } } );
		m_world.registerEntity( MenuItem.class, new EntityFactory() { @Override public Entity create() { return new MenuItem( menuScreen ); } } );
		m_world.registerEntity( LevelSelectionManager.class, new EntityFactory() { @Override public Entity create() { return new LevelSelectionManager( m_game.getFileIO() ); } } );
		m_world.registerEntity( LevelItem.class, new EntityFactory() { @Override public Entity create() { return new LevelItem( menuScreen ); } } );
		m_world.registerEntity( FogOfWar.class, new EntityFactory() { @Override public Entity create() { return new FogOfWar( m_resourceManager ); } } );
		
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
		m_worldRenderer.register( AnimatedBackground.class, new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new AnimatedBackgroundVisual( m_world, m_resourceManager, parentEntity ); } } );
		m_worldRenderer.register( FXBackground.class, new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new FXBackgroundVisual( m_resourceManager, parentEntity ); } } );
		m_worldRenderer.register( MenuItem.class, new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new MenuItemVisual( m_resourceManager, parentEntity ); } } );
		m_worldRenderer.register( LevelItem.class, new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new LevelItemVisual( m_resourceManager, parentEntity ); } } );
		m_worldRenderer.register( FogOfWar.class, new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new FogOfWarVisual( parentEntity ); } } );

		// register controllers
		m_world.attachView( m_controllersView );
		m_controllersView.register( MenuManager.class, new EntityControllerFactory() { @Override public EntityController instantiate( Entity parentEntity ) { return new MenuManagerController( parentEntity, m_world, menuScreen, m_worldRenderer.getCamera() ); } } );
		m_controllersView.register( MenuItem.class, new EntityControllerFactory() { @Override public EntityController instantiate( Entity parentEntity ) { return new MenuItemController( parentEntity ); } } );
		m_controllersView.register( LevelSelectionManager.class, new EntityControllerFactory() { @Override public EntityController instantiate( Entity parentEntity ) { return new LevelSelectionManagerController( parentEntity, m_world, menuScreen, m_worldRenderer.getCamera() ); } } );
		
		// register physics
		m_world.attachView( m_physicsView );
		m_physicsView.register( MenuItem.class , new PhysicalBodyFactory() { @Override public PhysicalBody instantiate( Entity parentEntity ) { return new CollisionBody( parentEntity, true ); } } );
		
		// register the updatables
		addUpdatable( m_world );
		addUpdatable( m_physicsView );
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


	/**
	 * Executes an option associated with a particular menu item
	 * 
	 * @param menuOption
	 */
	public void onMenuItemClicked( String menuOption ) 
	{
		if ( menuOption.equals( "StartGame" ) )
		{
			m_game.setScreen( new MainMenu( m_game, MenuScreen.MS_Level_Selection ) );
		}
		if ( menuOption.equals( "Help" ) )
		{
			m_game.setScreen( new MainMenu( m_game, MenuScreen.MS_Help ) );
		}
		else if ( menuOption.equals( "Exit" ) )
		{
			m_game.closeGame();
		}
	}
	
	/**
	 * Loads the specified game level.
	 * 
	 * @param levelIdx
	 */
	public void loadLevel( int levelIdx )
	{
		m_game.setScreen( new GameScreen( m_game, levelIdx ) );
	}
	
	@Override
	public boolean onBackPressed() 
	{
		switch( m_currentMenuScreenId )
		{
			case MS_Main:
			{
				m_game.closeGame();
				break;
			}
			
			default:
			{
				m_game.setScreen( new MainMenu( m_game, MenuScreen.MS_Main ) );
				break;
			}
		}
		return true;
	}
	
}
