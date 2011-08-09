/**
 * 
 */
package com.hypefoundry.engine.util;

import java.util.*;

import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;

import android.util.FloatMath;
import android.util.Log;

/**
 * A tool for a simple 2D world division
 * 
 * @author paksas
 */
public class SpatialGrid2D< T extends SpatialGridObject >
{
	private class StaticObjectData
	{
		public final T  m_obj;
		public boolean	m_queried;
		
		StaticObjectData( T obj )
		{
			m_obj = obj;
			
			m_queried = false;
		}
	}
	
	
	private List< StaticObjectData >[] 		m_staticCells;
	private List< T >						m_dynamicObjects;
	private List< StaticObjectData >		m_staticObjects;
	
	private int 							m_cellsPerRow;
	private int 							m_cellsPerCol;
	private float 							m_cellSize;
	private float							m_dynObjectsMaxDistSq;
	private int								m_cellsCount;
	
	private int[] 							m_cellIds = new int[4];
	private List< T > 						m_foundObjects;
	
	/**
	 * Constructor.
	 * 
	 * @param worldWidth
	 * @param worldHeight
	 * @param cellSize
	 */
	@SuppressWarnings( "unchecked" )
	public SpatialGrid2D( float worldWidth, float worldHeight, float cellSize ) 
	{		
		// memorize the basic data about the grid size
		m_cellSize = cellSize;
		m_dynObjectsMaxDistSq = ( 2.0f * m_cellSize ) * ( 2.0f * m_cellSize );
		m_cellsPerRow = (int)FloatMath.ceil( worldWidth / cellSize );
		m_cellsPerCol = (int)FloatMath.ceil( worldHeight / cellSize );
		
		// create the arrays in which we'll keep the entities
		m_cellsCount = m_cellsPerRow * m_cellsPerCol;
		m_staticCells = new List[m_cellsCount];
		m_dynamicObjects = new ArrayList< T >();
		m_staticObjects = new ArrayList< StaticObjectData >();
		
		// preallocate the memory in the collections
		for( int i = 0; i < m_cellsCount; ++i ) 
		{
			m_staticCells[i] = new ArrayList< StaticObjectData >( 10 );
		}

		m_foundObjects = new ArrayList< T >( 10 );
	}
	
	/**
	 * Adds a new static object to the grid.
	 * 
	 * @param obj
	 */
	public void insertStaticObject( T obj ) 
	{
		StaticObjectData objData = new StaticObjectData( obj );
		int[] cellIds = getCellIds( obj.getBounds() );
		int cellAddr = cellIds[2] * m_cellsPerRow + cellIds[0];
		for ( int y = cellIds[2]; y <= cellIds[3]; ++y )
		{
			for ( int x = cellIds[0]; x <= cellIds[1]; ++x, ++cellAddr )
			{
				assert( cellAddr < m_cellsCount );	// invalid cell address check
				
				m_staticCells[cellAddr].add( objData );
			}
		}
		
		m_staticObjects.add( objData );
	}

	/**
	 * Adds a new dynamic object to the grid.
	 * @param obj
	 */
	public void insertDynamicObject( T obj ) 
	{	
		if ( obj != null )
		{
			m_dynamicObjects.add( obj );
		}
	}
	
	/**
	 * Removes an object from the grid
	 * 
	 * @param obj
	 */
	public void removeObject( T obj ) 
	{
		int[] cellIds = getCellIds( obj.getBounds() );
		int cellAddr = cellIds[2] * m_cellsPerRow + cellIds[0];
		for ( int y = cellIds[2]; y <= cellIds[3]; ++y )
		{
			for ( int x = cellIds[0]; x <= cellIds[1]; ++x, ++cellAddr )
			{
				assert( cellAddr < m_cellsCount );	// invalid cell address check
				
				m_staticCells[cellAddr].remove( obj );
				
			}	
		}
		
		// find the object and remove it
		int count = m_dynamicObjects.size();
		for ( int i = 0; i < count; ++i )
		{
			T objData = m_dynamicObjects.get(i);
			if ( objData == obj )
			{
				m_dynamicObjects.remove( objData );
				break;
			}
		}
	
		
		// find the object and remove it
		count = m_staticObjects.size();
		for ( int i = 0; i < count; ++i )
		{
			StaticObjectData objData = m_staticObjects.get(i);
			if ( objData.m_obj == obj )
			{
				m_staticObjects.remove( objData );
				break;
			}
		}
	
	}
	
	/**
	 * Updates the whereabouts of all registered dynamic objects.
	 */
	public void update()
	{
	}
	
	/**
	 * Returns a list of all objects within the specified area.
	 *   
	 * @param box
	 * @return
	 */
	public List< T > getPotentialColliders( BoundingBox box ) 
	{
		m_foundObjects.clear();
		int count = m_staticObjects.size();
		for ( int i = 0; i < count; ++i )
		{
			m_staticObjects.get(i).m_queried = false;
		}
		
		// go through the static objects and find the ones nearby
		int[] cellIds = getCellIds( box );
		int cellAddr = cellIds[2] * m_cellsPerRow + cellIds[0];
		for ( int y = cellIds[2]; y <= cellIds[3]; ++y )
		{
			for ( int x = cellIds[0]; x <= cellIds[1]; ++x, ++cellAddr )
			{
				// compare against the static objects
				count = m_staticCells[cellAddr].size();
				for( int j = 0; j < count; ++j ) 
				{
					StaticObjectData collider = m_staticCells[cellAddr].get(j);
					
					// check for duplicates
					if( collider != null && !collider.m_queried )
					{
						m_foundObjects.add( collider.m_obj );
						collider.m_queried = true;
					}
				}
			}
		}
		
		// go through the dynamic objects and find the ones nearby
		count = m_dynamicObjects.size();
		for ( int i = 0; i < count; ++i )
		{
			T collider = m_dynamicObjects.get(i);
			if ( collider.getBounds().doesOverlap2D( box ) )
			{
				m_foundObjects.add( collider );	
			}
		}
		
		return m_foundObjects;
	}
	
	/**
	 * Returns a list of all objects the specified object may collide with.
	 *   
	 * @param obj
	 * @return
	 */
	public List< T > getPotentialColliders( T obj ) 
	{
		// clear the found objects list
		m_foundObjects.clear();
		int count = m_staticObjects.size();
		for ( int i = 0; i < count; ++i )
		{
			m_staticObjects.get(i).m_queried = false;
		}
		
		BoundingBox shape = obj.getBounds();
		
		int[] cellIds = getCellIds( shape );
		int cellAddr = cellIds[2] * m_cellsPerRow + cellIds[0];
		for ( int y = cellIds[2]; y <= cellIds[3]; ++y )
		{
			for ( int x = cellIds[0]; x <= cellIds[1]; ++x, ++cellAddr )
			{				
				// compare against the static objects
				count = m_staticCells[cellAddr].size();
				for( int j = 0; j < count; ++j ) 
				{
					StaticObjectData collider = m_staticCells[cellAddr].get(j);
					
					// check for duplicates
					if( collider != null && collider.m_obj != obj && !collider.m_queried )
					{
						m_foundObjects.add( collider.m_obj );
						collider.m_queried = true;
					}
				}
			}
		}
		
		// go through the dynamic objects and find the ones nearby
		count = m_dynamicObjects.size();
		for ( int i = 0; i < count; ++i )
		{
			T collider = m_dynamicObjects.get(i);
			if ( collider != obj && collider.getBounds().doesOverlap2D( shape ) )
			{
				m_foundObjects.add( collider );	
			}
		}
		
		return m_foundObjects;
	}
	
	
	/**
	 * Finds out in which grid cells does the specified object reside at the moment.
	 * 
	 * @param shape
	 * @return
	 */
	public int[] getCellIds( BoundingBox shape ) 
	{		
		int minX = (int)(shape.m_minX / m_cellSize);
		int maxX = (int)(shape.m_maxX / m_cellSize);
		int minY = (int)(shape.m_minY / m_cellSize);
		int maxY = (int)(shape.m_maxY / m_cellSize);
		
		if ( minX < 0 ) { minX = 0; } else if ( minX >= m_cellsPerRow ) { minX = m_cellsPerRow - 1; }
		if ( maxX < 0 ) { maxX = 0; } else if ( maxX >= m_cellsPerRow ) { maxX = m_cellsPerRow - 1; }
		if ( minY < 0 ) { minY = 0; } else if ( minY >= m_cellsPerCol ) { minY = m_cellsPerCol - 1; }
		if ( maxY < 0 ) { maxY = 0; } else if ( maxY >= m_cellsPerCol ) { maxY = m_cellsPerCol - 1; }

		m_cellIds[0] = minX;
		m_cellIds[1] = maxX;
		m_cellIds[2] = minY;
		m_cellIds[3] = maxY;
				
		return m_cellIds;
	}
	
}
