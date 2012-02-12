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
import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.game.Game;
import com.hypefoundry.engine.game.Screen;
import com.hypefoundry.engine.hud.Hud;
import com.hypefoundry.engine.hud.HudRenderer;
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
import com.hypefoundry.kabloons.MainMenu;
import com.hypefoundry.kabloons.entities.background.AnimatedBackground;
import com.hypefoundry.kabloons.entities.background.AnimatedBackgroundVisual;
import com.hypefoundry.kabloons.entities.background.Background;
import com.hypefoundry.kabloons.entities.background.BackgroundVisual;
import com.hypefoundry.kabloons.entities.baloon.Baloon;
import com.hypefoundry.kabloons.entities.baloon.BaloonController;
import com.hypefoundry.kabloons.entities.baloon.BaloonVisual;
import com.hypefoundry.kabloons.entities.buzzSaw.BuzzSaw;
import com.hypefoundry.kabloons.entities.buzzSaw.BuzzSawPhysicalBody;
import com.hypefoundry.kabloons.entities.buzzSaw.BuzzSawVisual;
import com.hypefoundry.kabloons.entities.exitDoor.ExitDoor;
import com.hypefoundry.kabloons.entities.exitDoor.ExitDoorVisual;
import com.hypefoundry.kabloons.entities.fan.Fan;
import com.hypefoundry.kabloons.entities.fan.FanController;
import com.hypefoundry.kabloons.entities.fan.FanPhysicalBody;
import com.hypefoundry.kabloons.entities.fan.FanVisual;
import com.hypefoundry.kabloons.entities.player.Player;
import com.hypefoundry.kabloons.entities.player.PlayerController;
import com.hypefoundry.kabloons.entities.toggle.Toggle;
import com.hypefoundry.kabloons.entities.toggle.ToggleController;
import com.hypefoundry.kabloons.entities.toggle.ToggleVisual;
import com.hypefoundry.kabloons.utils.AssetsFactory;



/**
 * @author Paksas
 *
 */
public class GameScreen extends Screen 
{
	private int									m_levelIdx;
	private static int							m_levelsCount = 9;
	
	public World								m_world;
	public Renderer2D							m_worldRenderer;
	ControllersView								m_controllersView;
	PhysicsView									m_physicsView;
	public HudRenderer							m_hudRenderer;
	public AssetsFactory						m_assetsFactory;
	
	/**
	 * Constructor.
	 * 
	 * @param game				host game
	 * @param levelIdx			level index
	 */
	public GameScreen( Game game, int levelIdx ) 
	{
		super( game );
		
		// memorize level idx
		m_levelIdx = levelIdx;
		
		final GameScreen gameScreen = this;
		
		// load common assets definitions
		try 
		{
			InputStream worldFileStream = game.getFileIO().readAsset( "gameplay/entityDefinitions.xml" );
			m_assetsFactory = new AssetsFactory( XMLDataLoader.parse( worldFileStream, "Definitions" ) );
		} 
		catch ( IOException e ) 
		{
			Log.d( "Game", "Error while loading common assets definitions" );
			throw new RuntimeException( e );
		}
		
		// create the game world
		m_world = new World();
		
		// serialization support
		m_world.registerEntity( Background.class, new EntityFactory() { @Override public Entity create() { return new Background(); } } );
		m_world.registerEntity( AnimatedBackground.class, new EntityFactory() { @Override public Entity create() { return new AnimatedBackground(); } } );
		m_world.registerEntity( Baloon.class, new EntityFactory() { @Override public Entity create() { return new Baloon(); } } );
		m_world.registerEntity( ExitDoor.class, new EntityFactory() { @Override public Entity create() { return new ExitDoor( m_assetsFactory ); } } );
		m_world.registerEntity( Fan.class, new EntityFactory() { @Override public Entity create() { return new Fan(); } } );
		m_world.registerEntity( Player.class, new EntityFactory() { @Override public Entity create() { return new Player(); } } );
		m_world.registerEntity( Toggle.class, new EntityFactory() { @Override public Entity create() { return new Toggle( m_assetsFactory ); } } );
		m_world.registerEntity( BuzzSaw.class, new EntityFactory() { @Override public Entity create() { return new BuzzSaw( m_assetsFactory ); } } );
		
		// load the world
		try 
		{
			String levelPath = getLevelPath( levelIdx );
			InputStream worldFileStream = game.getFileIO().readAsset( levelPath );
			m_world.load( XMLDataLoader.parse( worldFileStream, "World" ) );
		} 
		catch ( IOException e ) 
		{
			Log.d( "Game", "Error while loading world" );
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
		m_worldRenderer.register( Baloon.class, new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new BaloonVisual( m_resourceManager, parentEntity ); } } );
		m_worldRenderer.register( ExitDoor.class, new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new ExitDoorVisual( m_resourceManager, parentEntity ); } } );
		m_worldRenderer.register( Fan.class, new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new FanVisual( m_resourceManager, parentEntity ); } } );
		m_worldRenderer.register( Toggle.class, new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new ToggleVisual( m_resourceManager, parentEntity ); } } );
		m_worldRenderer.register( BuzzSaw.class, new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new BuzzSawVisual( m_resourceManager, parentEntity ); } } );
		
		// register controllers
		m_world.attachView( m_controllersView );
		m_controllersView.register( Baloon.class , new EntityControllerFactory() { @Override public EntityController instantiate( Entity parentEntity ) { return new BaloonController( parentEntity ); } } );
		m_controllersView.register( Fan.class , new EntityControllerFactory() { @Override public EntityController instantiate( Entity parentEntity ) { return new FanController( parentEntity ); } } );
		m_controllersView.register( Player.class , new EntityControllerFactory() { @Override public EntityController instantiate( Entity parentEntity ) { return new PlayerController( gameScreen, parentEntity ); } } );
		m_controllersView.register( Toggle.class , new EntityControllerFactory() { @Override public EntityController instantiate( Entity parentEntity ) { return new ToggleController( parentEntity ); } } );
		
		// register physics
		m_world.attachView( m_physicsView );
		m_physicsView.register( Baloon.class , new PhysicalBodyFactory() { @Override public PhysicalBody instantiate( Entity parentEntity ) { return new CollisionBody( parentEntity, true ); } } );
		m_physicsView.register( Fan.class , new PhysicalBodyFactory() { @Override public PhysicalBody instantiate( Entity parentEntity ) { return new FanPhysicalBody( parentEntity ); } } );
		m_physicsView.register( ExitDoor.class , new PhysicalBodyFactory() { @Override public PhysicalBody instantiate( Entity parentEntity ) { return new CollisionBody( parentEntity, true ); } } );
		m_physicsView.register( Toggle.class , new PhysicalBodyFactory() { @Override public PhysicalBody instantiate( Entity parentEntity ) { return new CollisionBody( parentEntity, true ); } } );
		m_physicsView.register( BuzzSaw.class , new PhysicalBodyFactory() { @Override public PhysicalBody instantiate( Entity parentEntity ) { return new BuzzSawPhysicalBody( parentEntity ); } } );
		
		// register the updatables
		addUpdatable( m_world );
		addUpdatable( m_physicsView );
		
		// initialize the HUD renderer
		Hud hud = m_resourceManager.getResource( Hud.class, "hud/gameplay/gameHudDefinition.xml" );
		m_hudRenderer = new HudRenderer( game, hud );
		registerInputHandler( m_hudRenderer );
	}
	
	/**
	 * Returns the active resource manager instance.
	 *  
	 * @return
	 */
	public ResourceManager getResourceManager()
	{
		return m_resourceManager;
	}
	
	/**
	 * Returns the game instance.
	 * 
	 * @return
	 */
	public Game getGame()
	{
		return m_game;
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
		
		// draw the hud
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
	// Game flow control
	// ------------------------------------------------------------------------
	/**
	 * Makes the game exit to main menu.
	 */
	public void exitToMenu() 
	{
		m_game.setScreen( new MainMenu( m_game ) );
	}

	/**
	 * Loads the specified level.
	 * 
	 * @param levelIdx
	 */
	public void loadLevel( int levelIdx ) 
	{
		levelIdx = validateLevelIdx( levelIdx );
		m_game.setScreen( new GameScreen( m_game, levelIdx ) );
	}
	
	/**
	 * Creates a path to the specified game level.
	 * 
	 * @param levelIdx
	 * @return
	 */
	private String getLevelPath( int levelIdx )
	{
		// assert the input data
		levelIdx = validateLevelIdx( levelIdx );
		
		// build the path
		StringBuilder levelPath = new StringBuilder();
		levelPath.append( "levels/world_" );
		if ( levelIdx < 10 )
		{
			levelPath.append( "0" );
		}
		levelPath.append( levelIdx );

		levelPath.append( ".xml" );
		
		return levelPath.toString();
	}
	
	/**
	 * Validates and returns a corrected level index.
	 * @param idx
	 * @return
	 */
	private int validateLevelIdx( int idx )
	{
		// assert the input data
		if ( idx < 1 )
		{
			idx = 1;
		}
		else if ( idx > m_levelsCount )
		{
			idx = m_levelsCount;
		}
		
		return idx;
	}

	public void loadNextLevel() 
	{
		if ( m_levelIdx < m_levelsCount - 1 )
		{
			loadLevel( m_levelIdx + 1 );
		}
		else
		{
			// there are no more levels - exit to the main menu
			exitToMenu();
		}
	}

	public void reloadLevel() 
	{
		loadLevel( m_levelIdx );
	}
}
