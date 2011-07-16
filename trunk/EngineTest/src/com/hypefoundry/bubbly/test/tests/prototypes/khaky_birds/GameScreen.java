package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.controllers.BirdController;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.controllers.ElectricCablesAI;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.controllers.ElectricShockAI;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.controllers.PedestrianAI;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.Bird;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.ElectricCables;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.ElectricShock;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.Ground;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.Pedestrian;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.visuals.BirdVisual;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.visuals.ElectricCablesVisual;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.visuals.ElectricShockVisual;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.visuals.GroundVisual;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.visuals.PedestrianVisual;
import com.hypefoundry.engine.controllers.ControllersView;
import com.hypefoundry.engine.controllers.EntityController;
import com.hypefoundry.engine.controllers.EntityControllerFactory;
import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.game.Game;
import com.hypefoundry.engine.game.Screen;
import com.hypefoundry.engine.game.Updatable;
import com.hypefoundry.engine.game.World;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.EntityVisualFactory;
import com.hypefoundry.engine.renderer2D.Renderer2D;

public class GameScreen extends Screen 
{
	World										m_world;
	Renderer2D									m_worldRenderer;
	ControllersView								m_controllersView;
	
	/**
	 * Constructor.
	 * 
	 * @param game				host game
	 */
	public GameScreen( Game game ) 
	{
		super(game);
		
		// create the game world
		m_world = new World();

		// register the updatables
		addUpdatable( m_world );
		
		// create the renderer
		m_worldRenderer = new Renderer2D( game.getGraphics() );
		m_world.attachView( m_worldRenderer );
		
		// register visuals
		m_worldRenderer.register( Bird.class , new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new BirdVisual( parentEntity ); } } );
		m_worldRenderer.register( ElectricCables.class , new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new ElectricCablesVisual( parentEntity ); } } );
		m_worldRenderer.register( ElectricShock.class , new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new ElectricShockVisual( parentEntity ); } } );
		m_worldRenderer.register( Ground.class , new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new GroundVisual( parentEntity ); } } );
		m_worldRenderer.register( Pedestrian.class , new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new PedestrianVisual( parentEntity ); } } );
		
		// register controllers
		m_controllersView = new ControllersView( this );
		m_controllersView.register( Bird.class , new EntityControllerFactory() { @Override public EntityController instantiate( Entity parentEntity ) { return new BirdController( parentEntity ); } } );
		m_controllersView.register( ElectricCables.class , new EntityControllerFactory() { @Override public EntityController instantiate( Entity parentEntity ) { return new ElectricCablesAI( parentEntity ); } } );
		m_controllersView.register( ElectricShock.class , new EntityControllerFactory() { @Override public EntityController instantiate( Entity parentEntity ) { return new ElectricShockAI( parentEntity ); } } );
		m_controllersView.register( Pedestrian.class , new EntityControllerFactory() { @Override public EntityController instantiate( Entity parentEntity ) { return new PedestrianAI( parentEntity ); } } );
		
		// populate the game world
		populateGameWorld();
	}

	/**
	 * Populates the game world with entities.
	 */
	private void populateGameWorld() 
	{
		// first - create the ground
		m_world.addEntity( new Ground() );
		
		// next - we need the pedestrians that wander around
		final int pedestriansCount = 1;
		for ( int i = 0; i < pedestriansCount; ++i )
		{
			Pedestrian pedestrian = new Pedestrian();
			m_world.addEntity( pedestrian );
		}
	}

	@Override
	public void present( float deltaTime ) 
	{	
		// draw the world contents
		m_worldRenderer.draw();
	}

	@Override
	public void pause() 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() 
	{
		// TODO Auto-generated method stub

	}

}
