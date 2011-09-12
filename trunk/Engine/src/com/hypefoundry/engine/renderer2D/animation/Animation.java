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


/**
 * Keyframed texture animation.
 * 
 * @author Paksas
 */
public class Animation extends Resource
{
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
	
	// ------------------------------------------------------------------------
	
	private float								m_frameDuration;
	TextureRegion[]								m_regions;
	private EntityEvent[][]						m_events;
	private boolean								m_looped;
	private static List< EventFactoryData >   	m_eventFactories = new ArrayList< EventFactoryData >();
	
	
	/**
	 * Default constructor.
	 */
	public Animation()
	{
		m_frameDuration = 0.0f;
		m_regions = null;
		m_looped = true;
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
			animIdx = (int)( animationTime / m_frameDuration ) % m_regions.length;
		}
		else
		{
			animIdx = (int)( animationTime / m_frameDuration );
			if ( animIdx < 0 )
			{
				animIdx = 0;
			}
			else if ( animIdx >= m_regions.length )
			{
				animIdx = m_regions.length - 1;
			}
		}
		
		return animIdx;
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
			
			RenderState renderState = new RenderState();
			renderState.deserialize( m_resMgr, animNode );
			
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
		}
	}
	
	@Override
	public void release() 
	{
		// nothing to do here
	}
	
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
}
