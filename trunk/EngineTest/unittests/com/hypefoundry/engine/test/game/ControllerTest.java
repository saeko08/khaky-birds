package com.hypefoundry.engine.test.game;

import com.hypefoundry.engine.controllers.ControllersView;
import com.hypefoundry.engine.controllers.EntityController;
import com.hypefoundry.engine.controllers.EntityControllerFactory;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.game.Updatable;
import com.hypefoundry.engine.game.UpdatesManager;
import com.hypefoundry.engine.world.World;

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

		@Override
		public void pause(boolean enable) {}
	}
	
	class Chair extends Entity
	{
	}
	
	class Bike extends Entity
	{
	}
	
	String m_report = "";
	public class ReportingControllerMock extends EntityController
	{
		public ReportingControllerMock( String id, Entity parentEntity )
		{
			super( parentEntity );			
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
		world.setSize( 500, 500 );
		
		// setup the renderer
		ControllersView controllersView = new ControllersView( new UpdatesManagerStub() );
		world.attachView( controllersView );
		
		// register types
		controllersView.register( Chair.class , new EntityControllerFactory() { @Override public EntityController instantiate( Entity parentEntity ) { return new ReportingControllerMock( "Chair", parentEntity ); } } );
		controllersView.register( Bike.class , new EntityControllerFactory() { @Override public EntityController instantiate( Entity parentEntity ) { return new ReportingControllerMock( "Bike", parentEntity ); } } );
		
		world.addEntity( new Bike() );
		world.addEntity( new Chair() );
		world.update( 0 );
		assertEquals( "Bike;Chair;", m_report );
	}
}
