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
		public final T  m_obj;
		public boolean	m_queried;
		
		DynamicObjectData( T obj )
		{
			m_obj = obj;
			
			m_cellIds[0] = m_cellIds[1] = m_cellIds[2] = m_cellIds[3] = -1;
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
		DynamicObjectData objData = new DynamicObjectData( obj );
		
		int[] cellIds = getCellIds( obj.getBounds() );
		int existingIdx = -1;
		int cellAddr = cellIds[2] * m_cellsPerRow + cellIds[0];
		for ( int y = cellIds[2]; y <= cellIds[3]; ++y )
		{
			for ( int x = cellIds[0]; x <= cellIds[1]; ++x, ++cellAddr )
			{
				assert( cellAddr < m_cellsCount );	// invalid cell address check
				
				existingIdx = m_dynamicCells[cellAddr].indexOf( null );
			
				if ( existingIdx >= 0)
				{
					m_dynamicCells[cellAddr].set( existingIdx, objData );
				}
				else
				{
					m_dynamicCells[cellAddr].add( objData );
				}
			}
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
		int cellAddr = cellIds[2] * m_cellsPerRow + cellIds[0];
		for ( int y = cellIds[2]; y <= cellIds[3]; ++y )
		{
			for ( int x = cellIds[0]; x <= cellIds[1]; ++x, ++cellAddr )
			{
				assert( cellAddr < m_cellsCount );	// invalid cell address check
				
				m_dynamicCells[cellAddr].remove( obj );
				m_staticCells[cellAddr].remove( obj );
				
			}	
		}
		
		// find the object and remove it
		for ( DynamicObjectData objData : m_dynamicObjects )
		{
			if ( objData.m_obj == obj )
			{
				m_dynamicObjects.remove( objData );
				break;
			}
		}
	
		
		// find the object and remove it
		for ( StaticObjectData objData : m_staticObjects )
		{
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
		for ( DynamicObjectData objData : m_dynamicObjects )
		{
			int cellAddr = objData.m_cellIds[2] * m_cellsPerRow + objData.m_cellIds[0];
			for ( int y = objData.m_cellIds[2]; y <= objData.m_cellIds[3]; ++y )
			{
				for ( int x = objData.m_cellIds[0]; x <= objData.m_cellIds[1]; ++x, ++cellAddr )
				{
					assert( cellAddr < m_cellsCount );	// invalid cell address check
					int idx = m_dynamicCells[cellAddr].indexOf( objData );
					if ( idx >= 0 )
					{
						m_dynamicCells[cellAddr].set( idx, null );
					}
				}
			}

			objData.setCells( getCellIds( objData.m_obj.getBounds() ) );

			cellAddr = objData.m_cellIds[2] * m_cellsPerRow + objData.m_cellIds[0];
			for ( int y = objData.m_cellIds[2]; y <= objData.m_cellIds[3]; ++y )
			{
				for ( int x = objData.m_cellIds[0]; x <= objData.m_cellIds[1]; ++x, ++cellAddr )
				{
					int idx = m_dynamicCells[cellAddr].indexOf( null );
					
					if ( idx < 0 )
					{
						idx = m_dynamicCells[cellAddr].size();
						m_dynamicCells[cellAddr].add( null );
					}
					
					m_dynamicCells[cellAddr].set( idx, objData );
				}
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
		int cellAddr = cellIds[2] * m_cellsPerRow + cellIds[0];
		for ( int y = cellIds[2]; y <= cellIds[3]; ++y )
		{
			for ( int x = cellIds[0]; x <= cellIds[1]; ++x, ++cellAddr )
			{
				// compare against the dynamic objects
				int len = m_dynamicCells[cellAddr].size();
				for( int j = 0; j < len; ++j ) 
				{
					DynamicObjectData collider = m_dynamicCells[cellAddr].get(j);
					
					// check for duplicates
					if( collider != null && !collider.m_queried )
					{
						m_foundObjects.add( collider.m_obj );
						collider.m_queried = true;
					}
				}
				
				// compare against the static objects
				len = m_staticCells[cellAddr].size();
				for( int j = 0; j < len; ++j ) 
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
		int cellAddr = cellIds[2] * m_cellsPerRow + cellIds[0];
		for ( int y = cellIds[2]; y <= cellIds[3]; ++y )
		{
			for ( int x = cellIds[0]; x <= cellIds[1]; ++x, ++cellAddr )
			{
				// compare against the dynamic objects
				int len = m_dynamicCells[cellAddr].size();
				for( int j = 0; j < len; ++j ) 
				{
					DynamicObjectData collider = m_dynamicCells[cellAddr].get(j);
					
					// check for duplicates
					if( collider != null && collider.m_obj != obj && !collider.m_queried )
					{
						m_foundObjects.add( collider.m_obj );
						collider.m_queried = true;
					}
				}
				
				// compare against the static objects
				len = m_staticCells[cellAddr].size();
				for( int j = 0; j < len; ++j ) 
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
