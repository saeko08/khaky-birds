/**
 * 
 */
package com.hypefoundry.engine.util;

import java.util.*;

import com.hypefoundry.engine.math.BoundingBox;

import android.util.FloatMath;
import android.util.Log;

/**
 * A tool for a simple 2D world division
 * 
 * @author paksas
 */
public class SpatialGrid2D< T extends SpatialGridObject >
{
	private class DynamicObjectData
	{
		public int[] 	m_cellIds 		= new int[4];
		public int[] 	m_posIds 		= new int[4];
		public final T  m_obj;
		public boolean	m_queried;
		
		DynamicObjectData( T obj )
		{
			m_obj = obj;
			
			m_cellIds[0] = m_cellIds[1] = m_cellIds[2] = m_cellIds[3] = -1;
			m_posIds[0] = m_posIds[1] = m_posIds[2] = m_posIds[3] = -1;
			m_queried = false;
		}
		
		void setCells( int[] cellIds )
		{
			m_cellIds[0] = cellIds[0];
			m_cellIds[1] = cellIds[1];
			m_cellIds[2] = cellIds[2];
			m_cellIds[3] = cellIds[3];
		}
	}
	
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
	
	
	private List< DynamicObjectData >[] 	m_dynamicCells;
	private List< StaticObjectData >[] 		m_staticCells;
	private List< DynamicObjectData >		m_dynamicObjects;
	private List< StaticObjectData >		m_staticObjects;
	
	private int 							m_cellsPerRow;
	private int 							m_cellsPerCol;
	private float 							m_cellSize;
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
		m_cellsPerRow = (int)FloatMath.ceil( worldWidth / cellSize );
		m_cellsPerCol = (int)FloatMath.ceil( worldHeight / cellSize );
		
		// create the arrays in which we'll keep the entities
		m_cellsCount = m_cellsPerRow * m_cellsPerCol;
		m_dynamicCells = new List[m_cellsCount];
		m_staticCells = new List[m_cellsCount];
		m_dynamicObjects = new ArrayList< DynamicObjectData >();
		m_staticObjects = new ArrayList< StaticObjectData >();
		
		// preallocate the memory in the collections
		for( int i = 0; i < m_cellsCount; ++i ) 
		{
			m_dynamicCells[i] = new ArrayList< DynamicObjectData >( 10 );
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
		int i = 0;
		int cellId = -1;
		while( i <= 3 && ( cellId = cellIds[i++] ) != -1 ) 
		{
			m_staticCells[cellId].add( objData );
		}
		
		m_staticObjects.add( objData );
	}

	/**
	 * Adds a new dynamic object to the grid.
	 * @param obj
	 */
	public void insertDynamicObject( T obj ) 
	{
		DynamicObjectData objData = new DynamicObjectData( obj );
		
		int[] cellIds = getCellIds( obj.getBounds() );
		int i = 0;
		int cellId = -1;
		int existingIdx = -1;
		while( i <= 3 && ( cellId = cellIds[i] ) != -1 ) 
		{
			existingIdx = m_dynamicCells[cellId].indexOf( null );
			
			if ( existingIdx >= 0)
			{
				objData.m_posIds[i] = existingIdx;
				m_dynamicCells[cellId].set( existingIdx, objData );
			}
			else
			{
				objData.m_posIds[i] = m_dynamicCells[cellId].size();
				m_dynamicCells[cellId].add( objData );
			}
			++i;
		}
		objData.setCells( cellIds );
		
		m_dynamicObjects.add( objData );
	}
	
	/**
	 * Removes an object from the grid
	 * 
	 * @param obj
	 */
	public void removeObject( T obj ) 
	{
		int[] cellIds = getCellIds( obj.getBounds() );
		int i = 0;
		int cellId = -1;
		while( i <= 3 && ( cellId = cellIds[i++] ) != -1 ) 
		{
			// TODO
			m_dynamicCells[cellId].remove( obj );
			m_staticCells[cellId].remove( obj );
		}
		
		// find the object and remove it
		for ( DynamicObjectData objData : m_dynamicObjects )
		{
			if ( objData.m_obj == obj )
			{
				m_dynamicObjects.remove( objData );
			}
		}
		
		// find the object and remove it
		for ( StaticObjectData objData : m_staticObjects )
		{
			if ( objData.m_obj == obj )
			{
				m_staticObjects.remove( objData );
			}
		}
	}
	
	/**
	 * Updates the whereabouts of all registered dynamic objects.
	 */
	public void update()
	{
		for ( DynamicObjectData objData : m_dynamicObjects )
		{
			int i = 0;
			int cellId = -1;
			while( i <= 3 && ( cellId = objData.m_cellIds[i] ) != -1 ) 
			{
				int idx = objData.m_posIds[i];
				if ( idx >= 0 )
				{
					m_dynamicCells[cellId].set( idx, null );
				}
				++i;
			}

			objData.setCells( getCellIds( objData.m_obj.getBounds() ) );

			i = 0;
			cellId = -1;
			while( i <= 3 && ( cellId = objData.m_cellIds[i] ) != -1 ) 
			{
				int idx = m_dynamicCells[cellId].indexOf( null );
				
				if ( idx < 0 )
				{
					idx = m_dynamicCells[cellId].size();
					m_dynamicCells[cellId].add( null );
				}
				
				objData.m_posIds[i] = idx;
				m_dynamicCells[cellId].set( idx, objData );
				
				++i;
			}
		}
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
		for ( DynamicObjectData objData : m_dynamicObjects )
		{
			objData.m_queried = false;
		}
		for ( StaticObjectData objData : m_staticObjects )
		{
			objData.m_queried = false;
		}
		
		int[] cellIds = getCellIds( box );
		int i = 0;
		int cellId = -1;
		while( i <= 3 && ( cellId = cellIds[i++] ) != -1 ) 
		{
			// compare against the dynamic objects
			int len = m_dynamicCells[cellId].size();
			for( int j = 0; j < len; ++j ) 
			{
				DynamicObjectData collider = m_dynamicCells[cellId].get(j);
				
				// check for duplicates
				if( collider != null && !collider.m_queried )
				{
					m_foundObjects.add( collider.m_obj );
					collider.m_queried = true;
				}
			}
			
			// compare against the static objects
			len = m_staticCells[cellId].size();
			for( int j = 0; j < len; ++j ) 
			{
				StaticObjectData collider = m_staticCells[cellId].get(j);
				
				// check for duplicates
				if( collider != null && !collider.m_queried )
				{
					m_foundObjects.add( collider.m_obj );
					collider.m_queried = true;
				}
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
		for ( DynamicObjectData objData : m_dynamicObjects )
		{
			objData.m_queried = false;
		}
		for ( StaticObjectData objData : m_staticObjects )
		{
			objData.m_queried = false;
		}
		
		BoundingBox shape = obj.getBounds();
		
		int[] cellIds = getCellIds( shape );
		int i = 0;
		int cellId = -1;
		while( i <= 3 && ( cellId = cellIds[i++] ) != -1 ) 
		{
			// compare against the dynamic objects
			int len = m_dynamicCells[cellId].size();
			for( int j = 0; j < len; ++j ) 
			{
				DynamicObjectData collider = m_dynamicCells[cellId].get(j);
				
				// check for duplicates
				if( collider != null && collider.m_obj != obj && !collider.m_queried )
				{
					m_foundObjects.add( collider.m_obj );
					collider.m_queried = true;
				}
			}
			
			// compare against the static objects
			len = m_staticCells[cellId].size();
			for( int j = 0; j < len; ++j ) 
			{
				StaticObjectData collider = m_staticCells[cellId].get(j);
				
				// check for duplicates
				if( collider != null && collider.m_obj != obj && !collider.m_queried )
				{
					m_foundObjects.add( collider.m_obj );
					collider.m_queried = true;
				}
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
		int minX = (int) (shape.m_minX / m_cellSize);
		int maxX = (int) (shape.m_maxX / m_cellSize);
		int minY = (int) (shape.m_minY / m_cellSize);
		int maxY = (int) (shape.m_maxY / m_cellSize);
		
		if ( minX < 0 || minX > m_cellsPerCol || maxX < 0 || maxX > m_cellsPerCol || 
				minY < 0 || minY > m_cellsPerRow || maxY < 0 || maxY > m_cellsPerRow )
		{
			// the object is outside the world's bounds
			m_cellIds[0] = -1;
			m_cellIds[1] = -1;
			m_cellIds[2] = -1;
			m_cellIds[3] = -1;
		}
		else
		{
			// the object can span onto maximum four different cells - let's calculate their indices 
			m_cellIds[0] = minY * m_cellsPerCol + minX;
			m_cellIds[1] = minY * m_cellsPerCol + maxX;
			m_cellIds[2] = maxY * m_cellsPerCol + minX;
			m_cellIds[3] = maxY * m_cellsPerCol + maxX;
			
			// compact the data - the cells were calculated in such an order
			// that if the object lies in just two cells, the adjacent
			// entries will have the same value - so we can simply go
			// through them and erase duplicated values
			for ( int i = 3; i >= 1; --i )
			{
				if ( m_cellIds[i] == m_cellIds[i - 1] )
				{
					// this cell has the same id as the previous one - set it to -1 then
					m_cellIds[i] = -1;
				}
			}
			
			for ( int i = 2; i >= 0; --i )
			{
				if ( m_cellIds[i] == -1 )
				{
					m_cellIds[i] = m_cellIds[i + 1];
					m_cellIds[i + 1] = -1;
				}
			}
		}
		
		return m_cellIds;
	}
	
}
