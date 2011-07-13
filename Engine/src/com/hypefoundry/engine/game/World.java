package com.hypefoundry.engine.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game world.
 * 
 * @author paksas
 *
 */
public class World 
{
	List<Entity>		m_entities;
	
	/**
	 * Constructor.
	 */
	public World()
	{
		m_entities = new ArrayList<Entity>();
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
	}
	
	/**
	 * Removes the entity from the world
	 * 
	 * @param entity
	 */
	public void removeEntity( Entity entity )
	{
		m_entities.remove( entity );
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

