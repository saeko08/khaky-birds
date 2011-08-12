package com.hypefoundry.engine.test.game;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

import com.hypefoundry.engine.util.serialization.xml.WorldFileLoader;
import com.hypefoundry.engine.util.serialization.xml.WorldFileSaver;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.World;
import com.hypefoundry.engine.world.WorldView;
import com.hypefoundry.engine.world.serialization.EntityFactory;
import com.hypefoundry.engine.world.serialization.xml.XMLWorldFileLoader;
import com.hypefoundry.engine.world.serialization.xml.XMLWorldFileSaver;

import android.test.AndroidTestCase;


public class WorldTest extends AndroidTestCase 
{
	class Apple extends Entity
	{
		private int 				m_sweetness;
		
		Apple() { m_sweetness = 0; }
		Apple( int sweetness ) { m_sweetness = sweetness; }
		
		@Override
		public void onLoad( WorldFileLoader loader )
		{
			m_sweetness = loader.getIntValue( "sweetness" );
		}
		
		@Override
		public void onSave( WorldFileSaver saver )
		{
			saver.setIntValue( "val" , m_sweetness );
		}
	}
	
	// ------------------------------------------------------------------------
	
	class Orange extends Entity
	{
		private int[] m_sourness = null;
		
		Orange() {}
		Orange( int[] sourness ) { m_sourness = sourness; }
		
		@Override
		public void onLoad( WorldFileLoader loader )
		{
			int count = loader.getChildrenCount( "sourness" );
			m_sourness = new int[count];
			int i = 0;
			for( WorldFileLoader child = loader.getChild( "sourness" ); child != null; child = child.getSibling(), ++i )
			{
				m_sourness[i] = child.getIntValue( "val" );
			}
		}
		
		@Override
		public void onSave( WorldFileSaver saver )
		{
			for ( int i = 0; i < m_sourness.length; ++i )
			{
				saver.addChild( "sourness" ).setIntValue( "val" , m_sourness[i] );
			}
		}
	}
	
	// ------------------------------------------------------------------------
	
	class WorldViewMock implements WorldView
	{
		List< Entity >		m_entities;
		
		WorldViewMock()
		{
			m_entities = new ArrayList< Entity >();
		}
		
		@Override
		public void onAttached( World world ) {}

		@Override
		public void onDetached( World world ) {}

		@Override
		public void onEntityAdded( Entity entity ) 
		{
			m_entities.add( entity );
		}

		@Override
		public void onEntityRemoved( Entity entity ) 
		{
			m_entities.remove( entity );
		}
		
	}
	
	// ------------------------------------------------------------------------
	
	public void testLoading() throws UnsupportedEncodingException
	{
		String worldDefinition = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
				"<World width='200' height='300'>" +
					"<Entity type=\"Apple\" sweetness='5' />" +
					"<Entity type=\"Orange\" >" +
						"<sourness val='3' />" +
						"<sourness val='5' />" +
						"<sourness val='2' />" +
					"</Entity>" +
					"<Entity type=\"Apple\" sweetness='3' />" +
				"</World>";
		
		World world = new World();
		world.registerEntity( Apple.class, new EntityFactory() { @Override public Entity create() { return new Apple(); } } );
		world.registerEntity( Orange.class, new EntityFactory() { @Override public Entity create() { return new Orange(); } } );
		
		world.load( XMLWorldFileLoader.parse( new ByteArrayInputStream( worldDefinition.getBytes("UTF-8") ) ) );
		
		assertEquals( 200.0f, world.getWidth() );
		assertEquals( 300.0f, world.getHeight() );
		
		// update the world so that the entities get properly added to it
		world.update(0);
		
		// check if everything was loaded ok
		WorldViewMock view = new WorldViewMock();
		world.attachView( view );
		assertEquals( 3, view.m_entities.size() );
	}
	
	public void testSaving() throws UnsupportedEncodingException
	{		
		String receivedResult = "";
		
		// save
		{
			World world = new World();
			world.setSize( 200, 300 );
			world.addEntity( new Apple( 4 ) );
			world.addEntity( new Orange( new int[]{ 3, 7, 4 } ) );
			world.addEntity( new Apple( 7 ) );
			// update the world so that all entities get properly added
			world.update(0);
			
			// save the world's state
			WorldFileSaver saver = XMLWorldFileSaver.create();
			world.save( saver );
			
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			saver.flush( outStream );
			receivedResult = outStream.toString();
		}
		
		// load
		{
			World world = new World();
			world.registerEntity( Apple.class, new EntityFactory() { @Override public Entity create() { return new Apple(); } } );
			world.registerEntity( Orange.class, new EntityFactory() { @Override public Entity create() { return new Orange(); } } );
			
			world.load( XMLWorldFileLoader.parse( new ByteArrayInputStream( receivedResult.getBytes("UTF-8") ) ) );
			
			assertEquals( 200.0f, world.getWidth() );
			assertEquals( 300.0f, world.getHeight() );
			
			// update the world so that the entities get properly added to it
			world.update(0);
			
			// check if everything was loaded ok
			WorldViewMock view = new WorldViewMock();
			world.attachView( view );
			assertEquals( 3, view.m_entities.size() );
		}
	}
}

