package com.hypefoundry.engine.world;

import java.util.*;

import android.util.Log;

import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.BoundingShape;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.util.Pool;
import com.hypefoundry.engine.util.PoolObjectFactory;
import com.hypefoundry.engine.util.SpatialGridObject;
import com.hypefoundry.engine.world.serialization.WorldFileLoader;

/**
 * A game entity. Can be an agent, a piece of decoration - anything
 * that we want to place in the game world. 
 * 
 * @author paksas
 *
 */
public abstract class Entity
{
	private final int						MAX_EVENTS_COUNT = 8;
	
	private BoundingBox						m_bb;
	private BoundingBox						m_worldBB;
	private Vector3							m_pos;
	public float							m_facing;
	private List< Aspect >					m_aspects;
	
	private List< EventsPool > 				m_eventsPool;
	private List< EntityEventListener > 	m_eventListeners;
	
	
	// ------------------------------------------------------------------------
	
	private class EventsPool< T extends EntityEvent >
	{
		private final Class< T >					m_idClass;
		private Pool< T >							m_pool;
		
		// we want to decrease the memory burden inflicted by the events transmission
		// That's why we'll allow to send only a few events per frame,
		// and this way we'll be able to remove unnecessary and costly array reallocations
		private List< T >							m_eventsToProcess;
		private int									m_sentEventsCount;
		
		/**
		 * Constructor.
		 * 
		 * @param idClass
		 * @param eventsFactory		factory of the event objects
		 */
		EventsPool( final Class< T > idClass, EventFactory< T > eventFactory )
		{
			m_idClass = idClass;

			m_eventsToProcess = new ArrayList< T >( MAX_EVENTS_COUNT );
			for ( int i = 0; i < MAX_EVENTS_COUNT; ++i )
			{
				m_eventsToProcess.add( null );
			}
			m_sentEventsCount = 0;
						
			m_pool = new Pool< T >( eventFactory, MAX_EVENTS_COUNT );
		}
		
		/**
		 * Changes the event objects factory.
		 * 
		 * @param eventFactory
		 */
		public void setFactory( EventFactory< T > eventFactory )
		{
			m_pool = new Pool< T >( eventFactory, MAX_EVENTS_COUNT );
		}
		
		/**
		 * Checks if the pool matches the specified event type
		 * 
		 * @param rhs		compared event type
		 * @return
		 */
		public boolean equals( Class eventType )
		{
			return m_idClass.equals( eventType );
		}
		
		/**
		 * Creates a new event and places it in the pool for sending.
		 * 
		 * @return
		 */
		public T newEvent()
		{
			if ( m_sentEventsCount >= MAX_EVENTS_COUNT )
			{
				// too many events in the pool - can't create any more
				return null;
			}
			
			T event = m_pool.newObject();
			m_eventsToProcess.set( m_sentEventsCount, event );
			++m_sentEventsCount;
			
			return event;
		}
		
		/**
		 * Processes the events in the pool.
		 */
		public void processEvents()
		{
			for ( int i = 0; i < m_sentEventsCount; ++i )
			{
				T event = m_eventsToProcess.get( i );
				m_eventsToProcess.set( i, null );
				
				int count = m_eventListeners.size();
				for ( int j = 0; j < count; ++j )
				{
					m_eventListeners.get(j).onEvent( event );
				}
				
				// release the event back into the pool
				m_pool.free( event );
			}
			m_sentEventsCount = 0;
		}
	}
	
	// ------------------------------------------------------------------------
	
	/**
	 * Constructor.
	 */
	protected Entity()
	{
		m_bb = new BoundingBox( 0, 0, 0, 0, 0, 0 );
		m_worldBB = new BoundingBox( 0, 0, 0, 0, 0, 0 );
		m_pos = new Vector3();
		m_facing = 0.0f;
		m_aspects = new ArrayList< Aspect >();
		
		m_eventsPool = new ArrayList< EventsPool >();
		m_eventListeners = new ArrayList< EntityEventListener >();
	}
	
	/**
	 * Sets the bounding box of the entity.
	 * 
	 * @param bb
	 */
	public final void setBoundingBox( BoundingBox bb )
	{
		m_bb = bb;
		
		updateWorldBounds();
	}
	
	/**
	 * Returns the entity's bounding shape.
	 * 
	 * @return
	 */
	public final BoundingShape getBoundingShape()
	{
		return m_bb;
	}

	/**
	 * Updates the world bounds of the entity.
	 */
	private final void updateWorldBounds() 
	{
		// update the world bounding box
		m_worldBB.set( m_bb.m_minX + m_pos.m_x, m_bb.m_minY + m_pos.m_y, m_bb.m_minZ + m_pos.m_z, 
				m_bb.m_maxX + m_pos.m_x, m_bb.m_maxY + m_pos.m_y, m_bb.m_maxZ + m_pos.m_z );
	}
	
	/**
	 * Returns the world bounds of the entity.
	 * @return
	 */
	public final BoundingBox getWorldBounds()
	{
		return m_worldBB;
	}
	
	/**
	 * Returns the position in the Z buffer. 
	 * 
	 * Numbers towards the negative values are closer to the screen,
	 * and the numbers towards the positive values are farther from the screen.
	 * 
	 * @return
	 */
	public final Vector3 getPosition()
	{
		return m_pos;
	}
	
	/**
	 * Sets the entity's position.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public final void setPosition( float x, float y, float z )
	{
		m_pos.m_x = x;
		m_pos.m_y = y;
		m_pos.m_z = z;
		
		updateWorldBounds();
	}
	
	/**
	 * Sets the entity's position.
	 * 
	 * @param rhs
	 */
	public final void setPosition( Vector3 rhs )
	{
		m_pos.m_x = rhs.m_x;
		m_pos.m_y = rhs.m_y;
		m_pos.m_z = rhs.m_z;
		
		updateWorldBounds();
	}
		
	/**
	 * Translates the entity by the specified vector.
	 * 
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public final void translate( float dx, float dy, float dz )
	{
		m_pos.m_x += dx;
		m_pos.m_y += dy;
		m_pos.m_z += dz;
		
		updateWorldBounds();
	}
	
	/**
	 * Translates the entity by the specified vector.
	 * 
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public final void translate( Vector3 ds )
	{
		m_pos.add( ds );
		
		updateWorldBounds();
	}
	
	/**
	 * Returns entity's facing ( in XY plane )
	 * 
	 * @return
	 */
	public final float getFacing()
	{
		return m_facing;
	}
	
	/**
	 * Sets a new object facing.
	 * 
	 * @param facing
	 */
	public final void setFacing( float facing )
	{
		m_facing = facing;
		while ( m_facing < 0 )
		{
			m_facing += 360.0f;
		}
		while ( m_facing > 360.0f )
		{
			m_facing -= 360.0f;
		}
	}
	
	/**
	 * Rotate the entity by the specified angle value
	 * 
	 * @param angle
	 */
	public final void rotate( float angle )
	{
		m_facing += angle;
		while ( m_facing < 0 )
		{
			m_facing += 360.0f;
		}
		while ( m_facing > 360.0f )
		{
			m_facing -= 360.0f;
		}
	}

	/**
	 * Informs the entity that it's been added to the world.
	 * 
	 * @param hostWorld
	 */
	public void onAddedToWorld( World hostWorld ) {}
	
	/**
	 * Informs the entity that it's been removed from the world.
	 * 
	 * @param hostWorld
	 */
	public void onRemovedFromWorld( World hostWorld ) {}
	
	// ------------------------------------------------------------------------
	// Aspects management
	// ------------------------------------------------------------------------
	/**
	 * Defines a new aspect of the entity.
	 * It's protected - aspects can only be defined in the entity's implementation
	 * as a part of its state definition.
	 * 
	 * @param newAspect
	 */
	protected final void defineAspect( Aspect newAspect )
	{
		// check if we're not overriding an existing aspect - we can't do that, we can replace
		// an existing one though
		int count = m_aspects.size();
		for ( int i = 0; i < count; ++i )
		{
			Aspect aspect = m_aspects.get(i);
			if ( newAspect.getClass().isInstance( aspect ) )
			{
				m_aspects.set( i, newAspect );
				return;
			}
		}
		
		// this is a brand new aspect
		m_aspects.add( newAspect );
	}
	
	/**
	 * Queries for a specific aspect type
	 * 
	 * @param type
	 * @return
	 */
	public final < T extends Aspect > T query( Class< T > type )
	{
		int count = m_aspects.size();
		for ( int i = 0; i < count; ++i )
		{
			Aspect aspect = m_aspects.get(i);
			if ( type.isInstance( aspect ) )
			{
				return (T)aspect;
			}
		}
		
		return null;
	}

	/**
	 * Checks if the entity has the specified aspect.
	 * 
	 * @param class1
	 * @return
	 */
	public final < T extends Aspect >boolean hasAspect( Class< T > type ) 
	{
		int count = m_aspects.size();
		for ( int i = 0; i < count; ++i )
		{
			Aspect aspect = m_aspects.get(i);
			if ( type.isInstance( aspect ) )
			{
				return true;
			}
		}
		
		return false;
	}
	
	// ------------------------------------------------------------------------
	// Events management
	// ------------------------------------------------------------------------
	
	/**
	 * Attaches a new entity event listener.
	 * 
	 * CAUTION: The event types need to be registered before the calls to this method.
	 * 
	 * @param eventClass		event type the listener listens to
	 * @param listener
	 */
	public final void attachEventListener( EntityEventListener listener )
	{
		if ( listener != null )
		{
			m_eventListeners.add( listener );
		}
	}
	
	public final void detachEventListener( EntityEventListener listener )
	{
		if ( listener != null )
		{
			m_eventListeners.remove( listener );
		}
	}
	
	/**
	 * Processes the events the entity received.
	 */
	public void processEvents()
	{
		int count = m_eventsPool.size();
		for( int i = 0; i < count; ++i )
		{
			m_eventsPool.get(i).processEvents();
		}
	}
	
	/**
	 * Transmits an entity event to the specific listeners.
	 * 
	 * CAUTION: single entity can only receive up to MAX_EVENTS_COUNT per frame.
	 * Once that number is reached, no new events will be transmitted to the entity
	 * and this method will return null pointers.
	 * 
	 * @param event
	 * @throws EntityEventException		when the allowed number of sent events was exceeded 
	 */
	public final < T extends EntityEvent > T sendEvent( Class< T > eventClass ) throws EntityEventException
	{
		EventsPool< T > pool = getEventsPool( eventClass );
		if ( pool == null )
		{
			// Unregistered event type - this is acceptable
			return null;
		}
		
		T event = pool.newEvent();
		if ( event == null )
		{
			throw new EntityEventException( "Amount of allowed sent events exceeded" );
		}
		return event;
	}
	
	/**
	 * Registers an event type.
	 * 
	 * @param eventClass
	 * @param factory			factory that will create the events
	 */
	public final < T extends EntityEvent > void registerEvent( Class< T > eventClass, EventFactory< T > factory )
	{
		// check if the event isn't already registered.
		// If it is - replace the old definition
		int count = m_eventsPool.size();
		for( int i = 0; i < count; ++i )
		{
			EventsPool pool = m_eventsPool.get(i);
			if ( pool.equals( eventClass ) )
			{
				pool.setFactory( factory );
				return;
			}
		}
		
		// this is a new event typ
		EventsPool< T > newPool = new EventsPool< T >( eventClass, factory );
		m_eventsPool.add( newPool );
	}
	
	/**
	 * Looks for an events pool for the specified event type.
	 * 
	 * @param eventClass
	 * @return
	 */
	private < T extends EntityEvent > EventsPool< T > getEventsPool( final Class< T > eventClass )
	{
		int count = m_eventsPool.size();
		for( int i = 0; i < count; ++i )
		{
			EventsPool pool = m_eventsPool.get(i);
			if ( pool.equals( eventClass ) )
			{
				// pool found
				return (EventsPool< T >)pool;
			}
		}
		
		// no pool found
		return null;
	}
	
	// ------------------------------------------------------------------------
	// Serialization support
	// ------------------------------------------------------------------------
	
	/**
	 * Called during the loading to deserialize entity's state.
	 * 
	 * @param configNode
	 */
	public final void load( WorldFileLoader configNode )
	{
		if ( configNode == null )
		{
			return;
		}
		
		// load common entity parameters
		m_bb.load( "localBounds", configNode );
		m_worldBB.load( "worldBounds", configNode );
		m_pos.load( "position", configNode );
		m_facing = configNode.getFloatValue( "facing" );
		
		// load the aspects
		int aspectsCount = m_aspects.size();
		for ( int i = 0; i < aspectsCount; ++i )
		{
			m_aspects.get(i).load( configNode );
		}
		
		// load implementation specific stuff
		onLoad( configNode );
	}
	
	/**
	 * Called during the loading to deserialize entity's implementation-specific state.
	 * 
	 * @param configNode
	 */
	public void onLoad( WorldFileLoader configNode ) {}
}
