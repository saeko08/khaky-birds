package com.hypefoundry.engine.game;

import java.util.*;


/**
 * Represents the game world.
 * 
 * @author paksas
 *
 */
public class World implements Updatable
{
	private float		m_width;
	private float		m_height;
	List< Entity >		m_entities;
	List< WorldView >	m_views;
	
	/**
	 * Constructor.
	 * 
	 * @param width			world width
	 * @param height		world height
	 */
	public World( float	width, float height )
	{
		m_width = width;
		m_height = height;
		
		m_entities = new ArrayList< Entity >();
		m_views = new ArrayList< WorldView >();
	}
	
	
	// TODO: instead of these methods being polled all the time, 
	// send events to entities that are outside the world bounds
	
	/**
	 * Returns the width of the world.
	 * 
	 * @return
	 */
	public float getWidth()
	{
		return m_width;
	}
	
	/**
	 * Returns the height of the world.
	 * 
	 * @return
	 */
	public float getHeight()
	{
		return m_height;
	}
	
	/**
	 * Adds a new entity to the world.
	 * 
	 * @param entity
	 */
	public void addEntity( Entity entity )
	{
		if ( m_entities.indexOf( entity ) < 0 )
		{
			m_entities.add( entity );
		}
		
		// notify the views
		for( WorldView view : m_views )
		{
			view.onEntityAdded( entity );
		}
	}
	
	/**
	 * Removes the entity from the world
	 * 
	 * @param entity
	 */
	public void removeEntity( Entity entity )
	{		
		if ( m_entities.remove( entity ) )
		{
			// notify the views
			for( WorldView view : m_views )
			{
				view.onEntityRemoved( entity );
			}
		}
	}
	
	/**
	 * Attaches a new view to the world.
	 * 
	 * @param newView
	 */
	public void attachView( WorldView view )
	{
		for( WorldView v : m_views )
		{
			if ( v.equals( view ) )
			{
				return;
			}
		}
		m_views.add( view );
		
		// inform the view about all present entities
		for ( Entity entity : m_entities )
		{
			view.onEntityAdded( entity );
		}
	}
	
	/**
	 * Attaches a new view to the world.
	 * 
	 * @param newView
	 */
	public void detachView( WorldView view )
	{
		// remove all entities from the view
		for ( Entity entity : m_entities )
		{
			view.onEntityRemoved( entity );
		}
		
		// remove the view
		m_views.remove( view );
	}
	
	/**
	 * Executes an operation on all registered entities.
	 * 
	 * @param operation
	 */
	public void executeOperation( EntityOperation operation )
	{
		for ( Entity entity : m_entities )
		{
			operation.visit( entity );
		}
	}
	
	/**
	 * Updates the state of the world.
	 * 
	 * @param deltaTime
	 */
	@Override
	public void update( float deltaTime )
	{		
		// resolve collisions
		resolveCollisions();
	}
	
	// ------------------------------------------------------------------------
	// Utilities
	// ------------------------------------------------------------------------

	/**
	 * TODO: The method is efficient in terms of GC,
	 * but inefficient in terms of the number of overlap
	 * tests being performed.
	 */
	private void resolveCollisions() 
	{
		// go through all the entities and test 
		// their mutual overlap O(n2)
		int count = m_entities.size();
		for( int i = 0; i < count; ++i )
		{
			Entity e1 = m_entities.get(i);
			
			for ( int j = 0; j < count; ++j )
			{
				if ( i == j )
				{
					// don't perform the identity tests
					continue;
				}
				Entity e2 = m_entities.get(j);
				if ( e1.doesOverlap( e2 ) )
				{
					e1.onCollision( e2 );
				}
			}
		}
	}
}

