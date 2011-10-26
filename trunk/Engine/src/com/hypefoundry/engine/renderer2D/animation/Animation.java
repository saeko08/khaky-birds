/**
 * 
 */
package com.hypefoundry.engine.renderer2D.animation;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.hypefoundry.engine.core.Resource;
import com.hypefoundry.engine.renderer2D.RenderState;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.renderer2D.animation.AnimEvent;
import com.hypefoundry.engine.renderer2D.animation.AnimEventFactory;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.util.serialization.xml.XMLDataLoader;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityEvent;
import com.hypefoundry.engine.renderer2D.animation.frameDeserializers.*;

/**
 * Keyframed texture animation.
 * 
 * @author Paksas
 */
public class Animation extends Resource
{
	// ------------------------------------------------------------------------
	// Frames deserialization base
	// ------------------------------------------------------------------------	
	static class FrameDeserializersFactoryData
	{
		Class< ? extends FrameDeserializer >	m_type;
		FrameDeserializer						m_deserializer;
		
		FrameDeserializersFactoryData( Class< ? extends FrameDeserializer > type, FrameDeserializer deserializer )
		{
			m_type = type;
			m_deserializer = deserializer;
		}
	}
	
	private static FrameDeserializersFactoryData[]		m_frameDeserializers = {
			new FrameDeserializersFactoryData( StaticRegion.class, new StaticRegion() ),
			new FrameDeserializersFactoryData( LinearInterpolation.class, new LinearInterpolation() )
	};
	
	private static FrameDeserializer getDeserializer( String type )
	{
		for ( int i = 0; i < m_frameDeserializers.length; ++i )
		{
			FrameDeserializersFactoryData data = m_frameDeserializers[i];
			if ( data.m_type.getSimpleName().equals( type ) )
			{
				return data.m_deserializer;
			}
		}
		
		// factory definition not found
		return null;
	}
	
	// ------------------------------------------------------------------------
	// Events deserialization
	// ------------------------------------------------------------------------
	static class EventFactoryData
	{
		Class< ? extends EntityEvent >			m_type;
		AnimEventFactory						m_factory;
		
		EventFactoryData( Class< ? extends EntityEvent > type, AnimEventFactory factory )
		{
			m_type = type;
			m_factory = factory;
		}
	}
	
	private static List< EventFactoryData >   	m_eventFactories = new ArrayList< EventFactoryData >();
	
	/**
	 * Instantiates an event of the specified type, providing it's defined
	 * 
	 * @param type
	 * @return
	 */
	private static EntityEvent instantiateEvent( String type )
	{
		int count = m_eventFactories.size();
		for ( int i = 0; i < count; ++i )
		{
			EventFactoryData data = m_eventFactories.get(i);
			if ( data.m_type.getSimpleName().equals( type ) )
			{
				return data.m_factory.create();
			}
		}
		
		// factory definition not found
		return null;
	}
	
	// ------------------------------------------------------------------------
	
	public RenderState 							m_renderState;
	private float								m_frameDuration;
	private float								m_animationDuration;
	private boolean								m_looped;
	
	TextureRegion[]								m_regions;
	private EntityEvent[][]						m_events;
	int											m_framesCount;				// how many frames are actually defined
	
	/**
	 * Default constructor.
	 */
	public Animation()
	{
		m_frameDuration = 0.0f;
		m_animationDuration = 0.0f;
		m_regions = null;
		m_looped = true;
		m_framesCount = 0;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param frameDuration
	 * @param looped
	 * @param regions
	 * @param events
	 */
	public Animation( float frameDuration, boolean looped, TextureRegion[] regions, EntityEvent[][] events )
	{
		m_frameDuration = frameDuration;
		m_looped = looped;
		m_regions = regions;
		m_events = events;
		m_framesCount = regions.length;
		
		m_animationDuration = frameDuration * regions.length;
	}
	
	/**
	 * Registers a new type of event the animations can send to entities.
	 * 
	 * @param type
	 * @param factory
	 */
	public static void registerAnimEvent( Class< ? extends EntityEvent > type, AnimEventFactory factory )
	{
		m_eventFactories.add( new EventFactoryData( type, factory ) );
	}
	
	/**
	 * Returns duration of the animation.
	 * 
	 * @return
	 */
	public float getDuration() 
	{
		return m_animationDuration;
	}
	
	/**
	 * Returns the duration of a single frame.
	 * 
	 * @return
	 */
	public float getFrameDuration()
	{
		return m_frameDuration;
	}
	
	/**
	 * Returns an index of the animation frame for the specified time.
	 * 
	 * @param animationTime
	 * @return
	 */
	public int getFrameIdx( float animationTime )
	{
		// get the frame index
		int animIdx = 0;
		if ( m_looped )
		{
			animIdx = (int)( animationTime / m_frameDuration ) % m_framesCount;
		}
		else
		{
			animIdx = (int)( animationTime / m_frameDuration );
			if ( animIdx < 0 )
			{
				animIdx = 0;
			}
			else if ( animIdx >= m_framesCount )
			{
				animIdx = m_framesCount - 1;
			}
		}
		
		return animIdx;
	}
	
	/**
	 * Returns the number of frames of the animation.
	 * 
	 * @return
	 */
	public int getFramesCount() 
	{
		return m_framesCount;
	}
	
	/**
	 * Makes room for additional number of animation frames.
	 * CAUTION: It doesn't change the defined frames counter ( m_framesCount )
	 * 
	 * @param framesCount
	 */
	public void expand( int framesCount )
	{
		// texture regions
		TextureRegion[] newRegions = null;
		if ( m_regions == null )
		{
			newRegions = new TextureRegion[framesCount];
		}
		else
		{
			newRegions = new TextureRegion[ m_regions.length + 1 ];
			for ( int i = 0; i < m_regions.length; ++i )
			{
				newRegions[i] = m_regions[i];
			}
		}
		m_regions = newRegions;
		
		// events
		EntityEvent[][] newEvents = null;
		if ( m_events == null )
		{
			newEvents = new EntityEvent[framesCount][];
		}
		else
		{
			newEvents = new EntityEvent[ m_events.length + 1 ][];
			for ( int i = 0; i < m_events.length; ++i )
			{
				newEvents[i] = m_events[i];
			}
		}
		m_events = newEvents;
	}
	
	/**
	 * Appends a new animation frame.
	 * 
	 * CAUTION: the animation must have a room for the frame prior
	 * to this method being called.
	 * 
	 * @param frameIdx
	 * @param region
	 */
	public void appendFrame( TextureRegion region )
	{
		if ( m_framesCount >= m_regions.length )
		{
			throw new ArrayIndexOutOfBoundsException( "There's no room for new keyframes" );
		}
		
		m_regions[m_framesCount] = region;
		++m_framesCount;
	}
	
	/**
	 * Transmits animation events to the specified entity.
	 * 
	 * @param entity
	 * @param frameIdx
	 * @return 'true' if any events were transmitted
	 */
	public boolean transmitAnimEvents( Entity entity, int frameIdx )
	{
		EntityEvent[] events = m_events[frameIdx];
		int eventsTransmittedCount = 0;
		if ( events != null )
		{
			for ( int i = 0; i < events.length; ++i )
			{
				EntityEvent event = events[i];
				entity.sendEvent( AnimEvent.class ).set( event );
				++eventsTransmittedCount;
			}
		}
		
		return eventsTransmittedCount > 0;
	}
	
	@Override
	public void load() 
	{
		if ( m_regions != null )
		{
			// animation is already loaded
			return;
		}
		
		InputStream stream = null;
		try 
		{
			stream = m_game.getFileIO().readAsset( m_assetPath );
		} 
		catch ( IOException e ) 
		{
			throw new RuntimeException( e );
		}
		
		// parse the animation data
		DataLoader animNode = XMLDataLoader.parse( stream, "Animation" );
		if ( animNode != null )
		{
			m_frameDuration = animNode.getFloatValue( "frameDuration" );
			m_looped = animNode.getBoolValue( "looped", true );
			
			// deserialize shared render state
			m_renderState = new RenderState();
			m_renderState.deserialize( m_resMgr, animNode );
			
			// count the number of frames the animation will have and preallocate the storages
			int framesCount = 0;
			for ( DataLoader frameNode = animNode.getChild( "Frame" ); frameNode != null; frameNode = frameNode.getSibling() )
			{
				String type = frameNode.getStringValue( "type" );
				FrameDeserializer deserializer = getDeserializer( type );
				if ( deserializer != null )
				{
					framesCount += deserializer.getFramesCount( frameNode, this );
				}
			}
			expand( framesCount );
			
			// deserialize frames
			for ( DataLoader frameNode = animNode.getChild( "Frame" ); frameNode != null; frameNode = frameNode.getSibling() )
			{
				String type = frameNode.getStringValue( "type" );
				FrameDeserializer deserializer = getDeserializer( type );
				if ( deserializer != null )
				{
					// memorize the number of frames we have right now - this will be the index
					// of the first frame in the stream of frames created by this deseralizer,
					// and thus the index of a frame that we want to emit the animation events
					int eventFrameIdx = m_framesCount;
					
					// deserialize the frame
					deserializer.deserialize( frameNode, this );
					
					// each node may have animation events - deserialize those as well
					deserializeEvents( frameNode, eventFrameIdx );
					
				}
			}
			
			/*
			int framesCount = animNode.getChildrenCount( "TextureRegion" );
			m_regions = new TextureRegion[ framesCount ];
			m_events = new EntityEvent[framesCount][];
			int frameIdx = 0;
			for ( DataLoader regionNode = animNode.getChild( "TextureRegion" ); regionNode != null; regionNode = regionNode.getSibling(), ++frameIdx )
			{
				m_regions[frameIdx] = new TextureRegion( renderState );
				m_regions[frameIdx].deserializeCoordinates( regionNode );
				
				// read the events, if any are defined
				int eventsCount = regionNode.getChildrenCount( "Event" );
				if ( eventsCount > 0 )
				{
					int eventIdx = 0;
					m_events[frameIdx] = new EntityEvent[eventsCount];
					for ( DataLoader eventNode = regionNode.getChild( "Event" ); eventNode != null; eventNode = eventNode.getSibling(), ++eventIdx )
					{
						String eventType = eventNode.getStringValue( "type" );
						EntityEvent event = instantiateEvent( eventType );
						if ( event != null )
						{
							event.deserialize( eventNode );
							m_events[frameIdx][eventIdx] = event;
						}
					}
				}
			}
			*/
			
			// calculate animation duration
			m_animationDuration = m_frameDuration * m_framesCount;
		}
	}
	
	/**
	 * Deserializes the events stored in the frame.
	 * 
	 * @param frameNode
	 * @param eventFrameIdx		index of a frame into which the events should be inserted
	 */
	private void deserializeEvents( DataLoader frameNode, int eventFrameIdx )
	{

		// read the events, if any are defined
		int eventsCount = frameNode.getChildrenCount( "Event" );
		if ( eventsCount > 0 )
		{
			int eventIdx = 0;
			m_events[eventFrameIdx] = new EntityEvent[eventsCount];
			for ( DataLoader eventNode = frameNode.getChild( "Event" ); eventNode != null; eventNode = eventNode.getSibling(), ++eventIdx )
			{
				String eventType = eventNode.getStringValue( "type" );
				EntityEvent event = instantiateEvent( eventType );
				if ( event != null )
				{
					event.deserialize( eventNode );
					m_events[eventFrameIdx][eventIdx] = event;
				}
			}
		}
	}
	
	@Override
	public void release() 
	{
		// nothing to do here
	}
}
