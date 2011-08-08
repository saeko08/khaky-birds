package com.hypefoundry.engine.world;

import java.util.*;

import com.hypefoundry.engine.game.Updatable;

import android.util.Log;


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
	List< Entity >		m_entitiesToAdd;
	List< Entity >		m_entitiesToRemove;
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
		m_entitiesToAdd = new ArrayList< Entity >();
		m_entitiesToRemove = new ArrayList< Entity >();
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
		m_entitiesToRemove.remove( entity );
		m_entitiesToAdd.add( entity );
	}
	
	/**
	 * Removes the entity from the world
	 * 
	 * @param entity
	 */
	public void removeEntity( Entity entity )
	{
		m_entitiesToRemove.add( entity );
		m_entitiesToAdd.remove( entity );
	}
	
	/**
	 * Actual attachment of an entity to the world.
	 * 
	 * @param entity
	 */
	private void attachEntity( Entity entity )
	{
		if ( m_entities.indexOf( entity ) < 0 )
		{
			m_entities.add( entity );
			
			// inform the entity itself
			entity.onAddedToWorld( this );
		}
		
		// notify the views
		int count = m_views.size();
		for( int i = 0; i < count; ++i )
		{
			m_views.get(i).onEntityAdded( entity );
		}
	}
	
	/**
	 * Actual detachment of an entity to the world.
	 * 
	 * @param entity
	 */
	public void detachEntity( Entity entity )
	{		
		if ( m_entities.remove( entity ) )
		{
			// inform the entity itself
			entity.onRemovedFromWorld( this );
			
			// notify the views
			int count = m_views.size();
			for( int i = 0; i < count; ++i )
			{
				m_views.get(i).onEntityRemoved( entity );
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
		int count = m_views.size();
		for( int i = 0; i < count; ++i )
		{
			WorldView v = m_views.get(i);
			if ( v.equals( view ) )
			{
				return;
			}
		}
		m_views.add( view );

		// inform the view that it's been attached to a world
		view.onAttached( this );
		
		// inform the view about all present entities
		count = m_entities.size();
		for( int i = 0; i < count; ++i )
		{
			view.onEntityAdded( m_entities.get(i) );
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
		int count = m_entities.size();
		for( int i = 0; i < count; ++i )
		{
			view.onEntityRemoved( m_entities.get(i) );
		}
		
		// inform the view that it's been detached from a world
		view.onDetached( this );
		
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
		int count = m_entities.size();
		for( int i = 0; i < count; ++i )
		{
			operation.visit( m_entities.get(i) );
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
		// execute world attachment & detachment - detach first to save
		// memory
		int count = m_entitiesToRemove.size();
		for( int i = 0; i < count; ++i )
		{
			detachEntity( m_entitiesToRemove.get(i) );
		}
		m_entitiesToRemove.clear();
		
		count = m_entitiesToAdd.size();
		for( int i = 0; i < count; ++i )
		{
			attachEntity( m_entitiesToAdd.get(i) );
		}
		m_entitiesToAdd.clear();
		
		// process the entity events
		count = m_entities.size();
		for( int i = 0; i < count; ++i )
		{
			m_entities.get(i).processEvents();
		}
	}
	
	// ------------------------------------------------------------------------
	// Utilities
	// ------------------------------------------------------------------------

	/**
	 * Looks for the first entity of the specified type
	 * 
	 * @param entityType
	 * @return
	 */
	public Entity findEntity( Class entityType ) 
	{
		int count = m_entities.size();
		for( int i = 0; i < count; ++i )
		{
			Entity entity = m_entities.get(i);
			if ( entityType.isInstance( entity ) )
			{
				return entity;
			}
		}
		
		return null;
	}
}

