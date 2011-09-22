package com.hypefoundry.engine.test.renderer2D;

import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.renderer2D.animation.AnimEvent;
import com.hypefoundry.engine.renderer2D.animation.Animation;
import com.hypefoundry.engine.renderer2D.animation.AnimationPlayer;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityEvent;
import com.hypefoundry.engine.world.EntityEventListener;
import com.hypefoundry.engine.world.EventFactory;

import android.test.AndroidTestCase;


public class AnimationTest extends AndroidTestCase 
{
	class EntityMock extends Entity
	{
	}
	
	// ------------------------------------------------------------------------
	
	class EventsListenerMock implements EntityEventListener
	{
		int		m_eventsCount;
		
		@Override
		public void onEvent( EntityEvent event ) 
		{
			++m_eventsCount;
		}
	}
	
	// ------------------------------------------------------------------------
	
	class EventMock implements EntityEvent
	{
		@Override
		public void deserialize( DataLoader loader ) {}
	}
	
	// ------------------------------------------------------------------------
	
	public void testLoopedPlayback()
	{
		TextureRegion[] frames = {
				new TextureRegion(),
				new TextureRegion(),
				new TextureRegion(),
		};
		Animation animation = new Animation( 0.2f, true, frames, null );
		
		assertEquals( 0, animation.getFrameIdx( 0 ) );
		assertEquals( 0, animation.getFrameIdx( 0.1f ) );
		assertEquals( 0, animation.getFrameIdx( 0.19f ) );
		assertEquals( 1, animation.getFrameIdx( 0.2f ) );
		assertEquals( 1, animation.getFrameIdx( 0.21f ) );
		assertEquals( 2, animation.getFrameIdx( 0.41f ) );
		assertEquals( 0, animation.getFrameIdx( 0.6f ) );
		assertEquals( 0, animation.getFrameIdx( 0.61f ) );
	}
	
	public void testUnloopedPlayback()
	{
		TextureRegion[] frames = {
				new TextureRegion(),
				new TextureRegion(),
				new TextureRegion(),
		};
		Animation animation = new Animation( 0.2f, false, frames, null );
		
		assertEquals( 0, animation.getFrameIdx( 0 ) );
		assertEquals( 0, animation.getFrameIdx( 0.1f ) );
		assertEquals( 0, animation.getFrameIdx( 0.19f ) );
		assertEquals( 1, animation.getFrameIdx( 0.2f ) );
		assertEquals( 1, animation.getFrameIdx( 0.21f ) );
		assertEquals( 2, animation.getFrameIdx( 0.41f ) );
		
		// not looped
		assertEquals( 2, animation.getFrameIdx( 0.6f ) );
		assertEquals( 2, animation.getFrameIdx( 0.61f ) );
	}
	
	public void testAnimEventsEmittedOnlyOnceDuringFrame()
	{
		TextureRegion[] frames = {
				new TextureRegion(),
				new TextureRegion(),
				new TextureRegion(),
				new TextureRegion(),
		};
		EntityEvent[][] events = {
				null,
				{
					new EventMock()
				},
				null,
				{
					new EventMock()
				},
		};
		
		Animation animation = new Animation( 0.2f, true, frames, events );
		
		// setup the entity that will be receiving the events
		Entity eventsReceiver = new EntityMock();
		eventsReceiver.registerEvent( AnimEvent.class, new EventFactory< AnimEvent >() { @Override public AnimEvent createObject() { return new AnimEvent(); } } );
		EventsListenerMock listener = new EventsListenerMock();
		eventsReceiver.attachEventListener( listener );
		
		// start the animation
		AnimationPlayer player = new AnimationPlayer();
		int animIdx = player.addAnimation( animation );
		player.select( animIdx );
		
		player.getTextureRegion( eventsReceiver, 0 );
		eventsReceiver.processEvents();
		assertEquals( 0, listener.m_eventsCount );
		
		player.getTextureRegion( eventsReceiver, 0.2f );
		eventsReceiver.processEvents();
		assertEquals( 1, listener.m_eventsCount );
		
		// the event is transmitted only once during a frame
		// even if the frame is displayed many times 
		listener.m_eventsCount = 0;
		player.getTextureRegion( eventsReceiver, 0.1f );
		eventsReceiver.processEvents();
		assertEquals( 0, listener.m_eventsCount );
		
		player.getTextureRegion( eventsReceiver, 0.1f );
		eventsReceiver.processEvents();
		assertEquals( 0, listener.m_eventsCount );
		
		player.getTextureRegion( eventsReceiver, 0.2f );
		eventsReceiver.processEvents();
		assertEquals( 1, listener.m_eventsCount );
	}
	
	public void testAnimEventsFromSkippedFramesStillEmitted()
	{
		TextureRegion[] frames = {
				new TextureRegion(),
				new TextureRegion(),
				new TextureRegion(),
		};
		EntityEvent[][] events = {
				null,
				{
					new EventMock()
				},
				null,
		};
		
		Animation animation = new Animation( 0.2f, true, frames, events );
		
		// setup the entity that will be receiving the events
		Entity eventsReceiver = new EntityMock();
		eventsReceiver.registerEvent( AnimEvent.class, new EventFactory< AnimEvent >() { @Override public AnimEvent createObject() { return new AnimEvent(); } } );
		EventsListenerMock listener = new EventsListenerMock();
		eventsReceiver.attachEventListener( listener );
		
		// start the animation
		AnimationPlayer player = new AnimationPlayer();
		int animIdx = player.addAnimation( animation );
		player.select( animIdx );
		
		player.getTextureRegion( eventsReceiver, 0 );
		eventsReceiver.processEvents();
		assertEquals( 0, listener.m_eventsCount );
		
		// and now we're skipping the entire animation
		player.getTextureRegion( eventsReceiver, 0.5f );
		eventsReceiver.processEvents();
		assertEquals( 1, listener.m_eventsCount );
		
		// it will also work when you skip a few loops of animation, however all events will be transmitted just once
		listener.m_eventsCount = 0;
		player.getTextureRegion( eventsReceiver, 12.0f );
		eventsReceiver.processEvents();
		assertEquals( 1, listener.m_eventsCount );
	}
}
