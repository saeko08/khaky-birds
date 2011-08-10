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
public class SpatialGrid2D
{	
	private Grid 							m_staticGrid;
	private Grid 							m_dynamicGrid;
	
	// runtime temp data
	private int[]							m_cellIds = new int[4];
	
	/**
	 * Constructor.
	 * 
	 * @param worldWidth
	 * @param worldHeight
	 * @param cellSize
	 * @param maxObjects		max objects that can be placed in the grid
	 */
	@SuppressWarnings( "unchecked" )
	public SpatialGrid2D( float worldWidth, float worldHeight, float cellSize, short maxObjects ) 
	{		
		// create the grids
		m_staticGrid = new Grid( worldWidth, worldHeight, cellSize, maxObjects );
		m_dynamicGrid = new Grid( worldWidth, worldHeight, cellSize, maxObjects );
	}
	
	/**
	 * Adds a new static object to the grid.
	 * 
	 * @param obj
	 */
	public void insertStaticObject( SpatialGridObject obj ) 
	{
		m_staticGrid.insert( obj );
	}

	/**
	 * Adds a new dynamic object to the grid.
	 * @param obj
	 */
	public void insertDynamicObject( SpatialGridObject obj ) 
	{	
		m_dynamicGrid.insert( obj );
	}
	
	/**
	 * Removes an object from the grid
	 * 
	 * @param obj
	 */
	public void removeObject( SpatialGridObject obj ) 
	{
		m_staticGrid.removeObject( obj );
		m_dynamicGrid.removeObject( obj );
	}
	
	/**
	 * Updates the whereabouts of all registered dynamic objects.
	 */
	public void update()
	{
		m_dynamicGrid.update();
	}
	
	/**
	 * Returns a list of all objects within the specified area.
	 *   
	 * @param shape
	 * @param colliders		an array capable of storing the colliders found in the world
	 * @return	number of colliders found
	 */
	public int getPotentialColliders( BoundingBox shape, SpatialGridObject[] colliders ) 
	{
		int collidersCount = 0;
		
		m_staticGrid.getCellIds( shape, m_cellIds );
		
		collidersCount = m_staticGrid.getPotentialColliders( colliders, collidersCount, m_cellIds );
		collidersCount = m_dynamicGrid.getPotentialColliders( colliders, collidersCount, m_cellIds );
			
		return collidersCount;
	}
	
	/**
	 * Returns a list of all objects the specified object may collide with.
	 *   
	 * @param obj
	 * @param colliders		an array capable of storing the colliders found in the world
	 * @return	number of colliders found
	 */
	public int getPotentialColliders( SpatialGridObject obj, SpatialGridObject[] colliders ) 
	{
		// get the shape from the object the vicinity of which we're querying
		BoundingBox shape = obj.getBounds();
		
		int collidersCount = 0;
		m_staticGrid.getCellIds( shape, m_cellIds );
		
		collidersCount = m_staticGrid.getPotentialColliders( colliders, collidersCount, m_cellIds );
		collidersCount = m_dynamicGrid.getPotentialColliders( colliders, collidersCount, m_cellIds );
			
		return collidersCount;
	}
		
}

///////////////////////////////////////////////////////////////////////////////

class ObjectData
{
	final SpatialGridObject  		m_obj;
	short							m_idx;
	int[]							m_cellIds = new int[4];
	
	ObjectData( SpatialGridObject obj, short idx )
	{
		m_obj = obj;
		m_idx = idx;
	}
	
	void setCells( int[] ids )
	{
		m_cellIds[0] = ids[0];
		m_cellIds[1] = ids[1];
		m_cellIds[2] = ids[2];
		m_cellIds[3] = ids[3];
	}
}

///////////////////////////////////////////////////////////////////////////////

class Grid
{
	private int 							m_cellsCount;
	private int 							m_cellsPerRow;
	private int 							m_cellsPerCol;
	private short 							m_maxObjects;
	private float 							m_cellSize;
	
	private BitField[] 						m_cells;
	private ObjectData[]					m_objects;
	
	// runtime temp data
	private BitField 						m_colliders;
	
	/**
	 * Constructor.
	 * 
	 * @param worldWidth
	 * @param worldHeight
	 * @param cellSize
	 * @param maxObjects		max objects that can be placed in the grid
	 */
	@SuppressWarnings( "unchecked" )
	Grid( float worldWidth, float worldHeight, float cellSize, short maxObjects )
	{	
		// memorize the basic data about the grid size
		m_cellSize = cellSize;
		m_cellsPerRow = (int)FloatMath.ceil( worldWidth / cellSize );
		m_cellsPerCol = (int)FloatMath.ceil( worldHeight / cellSize );
		m_cellsCount = m_cellsPerRow * m_cellsPerCol;
		m_maxObjects = maxObjects;
				
		m_cells = new BitField[m_cellsCount];
		for ( int i = 0; i < m_cellsCount; ++i )
		{
			m_cells[i] = new BitField();
		}
		
		m_objects = new ObjectData[ m_maxObjects ];
		m_colliders = new BitField();
	}
	
	/**
	 * Adds a new static object to the grid.
	 * 
	 * @param obj
	 * @param cellIds
	 */
	public void insert( SpatialGridObject obj ) 
	{	
		// create the object
		ObjectData objData = null;
		
		// find the first unoccupied slot in the array and store the object there, initializing
		// its index
		for ( short i = 0; i < m_objects.length; ++i )
		{
			if ( m_objects[i] == null )
			{
				objData = new ObjectData( obj, i );
				m_objects[i] = objData;
				break;
			}
		}
		
		if ( objData == null )
		{
			// the storage capacity's been exceeded
			return;
		}
		
		// set it in the grid
		getCellIds( obj.getBounds(), objData.m_cellIds );
		int cellAddr = objData.m_cellIds[2] * m_cellsPerRow + objData.m_cellIds[0];
		for ( int y = objData.m_cellIds[2]; y <= objData.m_cellIds[3]; ++y )
		{
			for ( int x = objData.m_cellIds[0]; x <= objData.m_cellIds[1]; ++x, ++cellAddr )
			{
				assert( cellAddr < m_cellsCount );	// invalid cell address check
				
				m_cells[cellAddr].set( objData.m_idx, true );
			}
		}
	}
		
	/**
	 * Removes an object from the grid
	 * 
	 * @param obj
	 * @param cellIds
	 */
	public void removeObject( SpatialGridObject obj ) 
	{		
		ObjectData objData = null;
		// find the object and remove it
		for ( short i = 0; i < m_objects.length; ++i )
		{
			if ( m_objects[i].m_obj == obj )
			{
				objData = m_objects[ i ];
				m_objects[ i ] = null;
				break;
			}
		}
		
		if ( objData == null )
		{
			// object wasn't found
			return;
		}
		
		// remove the object from the cells
		int cellAddr = objData.m_cellIds[2] * m_cellsPerRow + objData.m_cellIds[0];
		for ( int y = objData.m_cellIds[2]; y <= objData.m_cellIds[3]; ++y )
		{
			for ( int x = objData.m_cellIds[0]; x <= objData.m_cellIds[1]; ++x, ++cellAddr )
			{
				assert( cellAddr < m_cellsCount );	// invalid cell address check
				
				m_cells[cellAddr].set( objData.m_idx, false );		
			}	
		}
	}
	
	/**
	 * Updates the grid's contents.
	 */
	public void update()
	{
		for ( int i = 0; i < m_maxObjects; ++i )
		{
			if ( m_objects[i] == null )
			{
				continue;
			}
			
			// reset the old cells
			ObjectData objData = m_objects[i];
			int cellAddr = objData.m_cellIds[2] * m_cellsPerRow + objData.m_cellIds[0];
			for ( int y = objData.m_cellIds[2]; y <= objData.m_cellIds[3]; ++y )
			{
				for ( int x = objData.m_cellIds[0]; x <= objData.m_cellIds[1]; ++x, ++cellAddr )
				{
					m_cells[cellAddr].set( objData.m_idx, false );
				}
			}
			
			// set the new ones
			getCellIds( objData.m_obj.getBounds(), objData.m_cellIds );
			cellAddr = objData.m_cellIds[2] * m_cellsPerRow + objData.m_cellIds[0];
			for ( int y = objData.m_cellIds[2]; y <= objData.m_cellIds[3]; ++y )
			{
				for ( int x = objData.m_cellIds[0]; x <= objData.m_cellIds[1]; ++x, ++cellAddr )
				{
					m_cells[cellAddr].set( objData.m_idx, true );
				}
			}
		}
	}
	
	/**
	 * Returns a list of all objects within the specified area.
	 *   
	 * @param box
	 * @param colliders		a bitfield specifying which objects to take
	 * @param startIdx		index from which the datqa should be appended to the 'colliders' array
	 * @pram cellIds		cell span that should be queried
	 * @return				end index in the array 
	 */
	public int getPotentialColliders( SpatialGridObject[] colliders, int startIdx, int[] cellIds ) 
	{	
		// check which objects overlap the specified area
		m_colliders.zeroes();
		int cellAddr = cellIds[2] * m_cellsPerRow + cellIds[0];
		for ( int y = cellIds[2]; y <= cellIds[3]; ++y )
		{
			for ( int x = cellIds[0]; x <= cellIds[1]; ++x, ++cellAddr )
			{
				m_colliders.or( m_cells[cellAddr] );
			}
		}
	
		// find the objects corresponding to the collider indices
		int collidersCount = startIdx;
		byte chunkIdx = 0;
		int key = 1;
		int k = 0;
		for ( short i = 0; i < m_maxObjects; ++i, ++k )
		{
			if ( k >= BitField.CHUNK_CAPACITY )
			{
				++chunkIdx;
				key = 1;
				k = 0;
			}
			if ( ( m_colliders.m_bits[chunkIdx] & key ) == key )
			{
				colliders[ collidersCount++ ] = m_objects[i].m_obj;
			}
			key = key << 1;
		}
		
		return collidersCount;
	}
	
	/**
	 * Finds out in which grid cells does the specified object reside at the moment.
	 * 
	 * @param shape
	 * @param cellIds
	 */
	public void getCellIds( BoundingBox shape, int[] cellIds ) 
	{		
		int minX = (int)(shape.m_minX / m_cellSize);
		int maxX = (int)(shape.m_maxX / m_cellSize);
		int minY = (int)(shape.m_minY / m_cellSize);
		int maxY = (int)(shape.m_maxY / m_cellSize);
		
		if ( minX < 0 ) { minX = 0; } else if ( minX >= m_cellsPerRow ) { minX = m_cellsPerRow - 1; }
		if ( maxX < 0 ) { maxX = 0; } else if ( maxX >= m_cellsPerRow ) { maxX = m_cellsPerRow - 1; }
		if ( minY < 0 ) { minY = 0; } else if ( minY >= m_cellsPerCol ) { minY = m_cellsPerCol - 1; }
		if ( maxY < 0 ) { maxY = 0; } else if ( maxY >= m_cellsPerCol ) { maxY = m_cellsPerCol - 1; }

		cellIds[0] = minX;
		cellIds[1] = maxX;
		cellIds[2] = minY;
		cellIds[3] = maxY;
	}
}
