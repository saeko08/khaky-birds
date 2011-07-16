package com.hypefoundry.engine.test.game;

import com.hypefoundry.engine.controllers.ControllersView;
import com.hypefoundry.engine.controllers.EntityController;
import com.hypefoundry.engine.controllers.EntityControllerFactory;
import com.hypefoundry.engine.core.Graphics;
import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.game.Updatable;
import com.hypefoundry.engine.game.UpdatesManager;
import com.hypefoundry.engine.game.World;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.test.game.RendererTest.Bike;
import com.hypefoundry.engine.test.game.RendererTest.Chair;
import com.hypefoundry.engine.test.game.RendererTest.GraphicsStub;
import com.hypefoundry.engine.test.game.RendererTest.ReportingVisualMock;

import android.test.AndroidTestCase;


public class ControllerTest extends AndroidTestCase 
{
	// ------------------------------------------------------------------------
	// Mocks & stubs
	// ------------------------------------------------------------------------
	class UpdatesManagerStub implements UpdatesManager
	{

		@Override
		public void addUpdatable(Updatable updatable) {}

		@Override
		public void removeUpdatable(Updatable updatable) {}
		
	}
	
	class Chair extends Entity
	{
		@Override
		public void onCollision(Entity colider) {}	
	}
	
	class Bike extends Entity
	{
		@Override
		public void onCollision(Entity colider) {}	
	}
	
	String m_report = "";
	public class ReportingControllerMock extends EntityController
	{
		private String 	m_id;
		public ReportingControllerMock( String id, Entity parentEntity )
		{
			super( parentEntity );
			m_id = id;
			
			m_report += id;
			m_report += ";";
		}
		
		@Override
		public void update(float deltaTime) {}
	}
	
	// ------------------------------------------------------------------------
	// Tests
	// ------------------------------------------------------------------------
	
	public void testDifferentTypes()
	{
		World world = new World();
		
		// setup the renderer
		ControllersView controllersView = new ControllersView( new UpdatesManagerStub() );
		world.attachView( controllersView );
		
		// register types
		controllersView.register( Chair.class , new EntityControllerFactory() { @Override public EntityController instantiate( Entity parentEntity ) { return new ReportingControllerMock( "Chair", parentEntity ); } } );
		controllersView.register( Bike.class , new EntityControllerFactory() { @Override public EntityController instantiate( Entity parentEntity ) { return new ReportingControllerMock( "Bike", parentEntity ); } } );
		
		world.addEntity( new Bike() );
		world.addEntity( new Chair() );
		assertEquals( "Bike;Chair;", m_report );
	}
}
