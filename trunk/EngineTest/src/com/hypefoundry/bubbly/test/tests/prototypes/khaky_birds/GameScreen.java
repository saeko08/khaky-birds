package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds;

import java.util.List;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird.Bird;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird.BirdController;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird.BirdVisual;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.cables.ElectricCables;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.cables.ElectricCablesAI;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.cables.ElectricCablesVisual;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.crap.Crap;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.crap.CrapAI;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.crap.CrapVisual;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.falcon.Falcon;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.falcon.FalconAI;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.falcon.FalconVisual;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.ground.Ground;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.ground.GroundVisual;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.pedestrian.Pedestrian;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.pedestrian.PedestrianAI;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.pedestrian.PedestrianVisual;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.shock.ElectricShock;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.shock.ElectricShockAI;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.shock.ElectricShockVisual;
import com.hypefoundry.engine.controllers.ControllersView;
import com.hypefoundry.engine.controllers.EntityController;
import com.hypefoundry.engine.controllers.EntityControllerFactory;
import com.hypefoundry.engine.core.Input.TouchEvent;
import com.hypefoundry.engine.util.FPSCounter;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.game.Game;
import com.hypefoundry.engine.game.Screen;
import com.hypefoundry.engine.world.World;
import com.hypefoundry.engine.physics.PhysicalBody;
import com.hypefoundry.engine.physics.PhysicalBodyFactory;
import com.hypefoundry.engine.physics.PhysicsView;
import com.hypefoundry.engine.physics.bodies.CollisionBody;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.EntityVisualFactory;
import com.hypefoundry.engine.renderer2D.Renderer2D;

public class GameScreen extends Screen 
{
	World										m_world;
	Renderer2D									m_worldRenderer;
	ControllersView								m_controllersView;
	PhysicsView									m_physicsView;
	
	FPSCounter									m_fpsCounter = new FPSCounter();
	
	/**
	 * Constructor.
	 * 
	 * @param game				host game
	 */
	public GameScreen( Game game ) 
	{
		super( game );
		
		// create the game world
		m_world = new World( 9.6f, 4.8f );
		
		// create the renderer
		m_worldRenderer = new Renderer2D( game );
		m_world.attachView( m_worldRenderer );
		
		// register visuals
		m_worldRenderer.register( Bird.class , new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new BirdVisual( m_resourceManager, parentEntity ); } } );
		m_worldRenderer.register( ElectricCables.class , new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new ElectricCablesVisual( m_resourceManager, parentEntity ); } } );
		m_worldRenderer.register( ElectricShock.class , new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new ElectricShockVisual( m_resourceManager, parentEntity ); } } );
		m_worldRenderer.register( Ground.class , new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new GroundVisual( m_resourceManager, parentEntity ); } } );
		m_worldRenderer.register( Pedestrian.class , new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new PedestrianVisual( m_resourceManager, parentEntity ); } } );
		m_worldRenderer.register( Crap.class , new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new CrapVisual( m_resourceManager, parentEntity ); } } );
		m_worldRenderer.register( Falcon.class , new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new FalconVisual( m_resourceManager, parentEntity ); } } );
		/*
		// register controllers
		m_controllersView = new ControllersView( this );
		m_world.attachView( m_controllersView );
		
		m_controllersView.register( Bird.class , new EntityControllerFactory() { @Override public EntityController instantiate( Entity parentEntity ) { return new BirdController( m_game.getInput(), parentEntity ); } } );
		m_controllersView.register( ElectricCables.class , new EntityControllerFactory() { @Override public EntityController instantiate( Entity parentEntity ) { return new ElectricCablesAI( m_world, parentEntity ); } } );
		m_controllersView.register( ElectricShock.class , new EntityControllerFactory() { @Override public EntityController instantiate( Entity parentEntity ) { return new ElectricShockAI( m_world, parentEntity ); } } );
		m_controllersView.register( Pedestrian.class , new EntityControllerFactory() { @Override public EntityController instantiate( Entity parentEntity ) { return new PedestrianAI( parentEntity ); } } );
		m_controllersView.register( Crap.class , new EntityControllerFactory() { @Override public EntityController instantiate( Entity parentEntity ) { return new CrapAI( m_world,parentEntity ); } } );
		m_controllersView.register( Falcon.class , new EntityControllerFactory() { @Override public EntityController instantiate( Entity parentEntity ) { return new FalconAI( m_world,parentEntity ); } } );
		
		// register physics
		m_physicsView = new PhysicsView( 2.0f ); // TODO: configure cell size
		m_world.attachView( m_physicsView );
		
		m_physicsView.register( Bird.class , new PhysicalBodyFactory() { @Override public PhysicalBody instantiate( Entity parentEntity ) { return new CollisionBody( parentEntity ); } } );
		m_physicsView.register( ElectricShock.class , new PhysicalBodyFactory() { @Override public PhysicalBody instantiate( Entity parentEntity ) { return new CollisionBody( parentEntity ); } } );
		m_physicsView.register( Pedestrian.class , new PhysicalBodyFactory() { @Override public PhysicalBody instantiate( Entity parentEntity ) { return new CollisionBody( parentEntity ); } } );
		m_physicsView.register( Crap.class , new PhysicalBodyFactory() { @Override public PhysicalBody instantiate( Entity parentEntity ) { return new CollisionBody( parentEntity ); } } );
		m_physicsView.register( Falcon.class , new PhysicalBodyFactory() { @Override public PhysicalBody instantiate( Entity parentEntity ) { return new CollisionBody( parentEntity ); } } );
		*/
		// register the updatables
		addUpdatable( m_world );
		// addUpdatable( m_physicsView );
				
		// populate the game world
		populateGameWorld();
	}

	/**
	 * Populates the game world with entities.
	 */
	private void populateGameWorld() 
	{
		// first - create the ground
		//m_world.addEntity( new Ground() );
		
		// create the cables the bird will move on
		//m_world.addEntity( new ElectricCables() );
		
		//create falcon that will hunt our bird
		//m_world.addEntity( new Falcon() );
		
		// next - we need the pedestrians that wander around
		final int pedestriansCount = 100;
		final float spawnAreaWidth = m_world.getWidth();
		final float spawnAreaHeight = m_world.getHeight();
		for ( int i = 0; i < pedestriansCount; ++i )
		{
			Pedestrian pedestrian = new Pedestrian( spawnAreaWidth, spawnAreaHeight );
			m_world.addEntity( pedestrian );
		}
		
		// finally add our main character
		//m_world.addEntity( new Bird() );
	}

	@Override
	public void present( float deltaTime ) 
	{	
		m_game.getInput().getKeyEvents();
		m_game.getInput().getTouchEvents();
		
		// draw the world contents
		m_worldRenderer.draw();
		m_fpsCounter.logFrame();
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

}
