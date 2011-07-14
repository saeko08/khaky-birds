package com.hypefoundry.engine.test.game;

import android.test.AndroidTestCase;

import com.hypefoundry.engine.core.Graphics;
import com.hypefoundry.engine.core.Pixmap;
import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.game.World;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.EntityVisualFactory;
import com.hypefoundry.engine.renderer2D.Renderer2D;


public class RendererTest extends AndroidTestCase 
{
	// ------------------------------------------------------------------------
	// Mocks
	// ------------------------------------------------------------------------
	class GraphicsStub implements Graphics
	{
		@Override
		public Pixmap newPixmap(String fileName, PixmapFormat format) { return null; }

		@Override
		public void clear(int color) {}

		@Override
		public void drawPixel(int x, int y, int color) {}

		@Override
		public void drawLine(int x, int y, int x2, int y2, int color) {}

		@Override
		public void drawRect(int x, int y, int width, int height, int color) {}

		@Override
		public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight) {}

		@Override
		public void drawPixmap(Pixmap pixmap, int x, int y) {}

		@Override
		public int getWidth() { return 0; }

		@Override
		public int getHeight() { return 0; }	
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
	
	int m_drawnEntities = 0;
	
	public class VisualMock extends EntityVisual
	{
		public VisualMock( Entity parentEntity )
		{
			super( parentEntity );
		}
		
		@Override
		public void draw(Graphics graphics) 
		{
			m_drawnEntities += 1;
		}
	}
	
	String m_report = "";
	public class ReportingVisualMock extends EntityVisual
	{
		public ReportingVisualMock( String id, Entity parentEntity )
		{
			super( parentEntity );
			
			m_report += id;
			m_report += ";";
		}
		
		@Override
		public void draw(Graphics graphics) 
		{
		}
	}


	// ------------------------------------------------------------------------
	// Tests
	// ------------------------------------------------------------------------
	public void testSimpleRendering()
	{
		World world = new World();
		
		// setup the renderer
		Renderer2D renderer = new Renderer2D( new GraphicsStub() );
		renderer.registerVisual( Chair.class , new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new VisualMock( parentEntity ); } } );
		
		// at first nothing is drawn - of course
		renderer.draw();
		assertEquals( 0, m_drawnEntities );
		
		// we add the first entity to the world, but the view is not attached, so nothing is drawn
		world.addEntity( new Chair() );
		world.addEntity( new Chair() );
		renderer.draw();
		assertEquals( 0, m_drawnEntities );
		
		// attach the view to the world - it receives a bulk notification about
		// all attached entities
		world.attachView( renderer );
		renderer.draw();
		assertEquals( 2, m_drawnEntities );
		
		// adding some more entities
		m_drawnEntities = 0;
		world.addEntity( new Chair() );
		renderer.draw();
		assertEquals( 3, m_drawnEntities );
		
	}
	
	public void testUnregisteredType()
	{
		World world = new World();
		
		// setup the renderer
		Renderer2D renderer = new Renderer2D( new GraphicsStub() );
		world.attachView( renderer );
		
		// mind that we didn't register any type here
		
		try
		{
			world.addEntity( new Chair() );
			assertTrue( false );
		}
		catch( Exception e )
		{
			assertTrue( true );	
		}
	}
	
	public void testDifferentTypes()
	{
		World world = new World();
		
		// setup the renderer
		Renderer2D renderer = new Renderer2D( new GraphicsStub() );
		world.attachView( renderer );
		
		// register types
		renderer.registerVisual( Chair.class , new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new ReportingVisualMock( "Chair", parentEntity ); } } );
		renderer.registerVisual( Bike.class , new EntityVisualFactory() { @Override public EntityVisual instantiate( Entity parentEntity ) { return new ReportingVisualMock( "Bike", parentEntity ); } } );
		
		world.addEntity( new Bike() );
		world.addEntity( new Chair() );
		assertEquals( "Bike;Chair;", m_report );
	}
	
	public void testRenderingOrder()
	{
		// TODO
	}
}