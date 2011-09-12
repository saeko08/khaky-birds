package com.hypefoundry.engine.test.game;

import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityEvent;
import com.hypefoundry.engine.world.EntityEventException;
import com.hypefoundry.engine.world.EntityEventListener;
import com.hypefoundry.engine.world.EventFactory;

import android.test.AndroidTestCase;


public class EntityEventsTest extends AndroidTestCase 
{	

	class MockEvent implements EntityEvent 
	{
		public String		m_someData;
		
		public void initialize( String someData )
		{
			m_someData = someData;
		}
		
		@Override
		public void deserialize( DataLoader loader ) {}
	}
	
	// ------------------------------------------------------------------------
	
	class EntityMock extends Entity
	{
	}
	
	// ------------------------------------------------------------------------
	
	public void testCustomEventsListening()
	{
		final StringBuilder	log = new StringBuilder();
		
		EntityMock entity = new EntityMock();

		entity.registerEvent( MockEvent.class, new EventFactory< MockEvent >() { @Override public MockEvent createObject() { return new MockEvent(); } } );
		entity.attachEventListener( new EntityEventListener() {
			@Override
			public void onEvent( EntityEvent event )
			{
				log.append( "onSomethingHappening( " );
				log.append( ((MockEvent)event).m_someData );
				log.append( " );" );
			}
		} );
		
		try
		{
			MockEvent event = entity.sendEvent( MockEvent.class );
			event.initialize( "ass");
		}
		catch( EntityEventException ex )
		{
			assertTrue( false );
		}
		assertTrue( log.toString().equals( "" ) );
		
		entity.processEvents();
		assertTrue( log.toString().equals( "onSomethingHappening( ass );" ) );
	}
	
}

