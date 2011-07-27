/**
 * 
 */
package com.hypefoundry.engine.util;

import java.util.*;

import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.math.BoundingBox;

import android.util.FloatMath;

/**
 * A tool for a simple 2D world division
 * 
 * @author paksas
 */
public class SpatialGrid2D 
{
	private List< Entity >[] 		m_dynamicCells;
	private List< Entity >[] 		m_staticCells;
	
	private int 					m_cellsPerRow;
	private int 					m_cellsPerCol;
	private float 					m_cellSize;
	
	private int[] 					m_cellIds = new int[4];
	private List< Entity > 			m_foundObjects;
	
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
		// memorize thebasic data about the grid size
		m_cellSize = cellSize;
		m_cellsPerRow = (int)FloatMath.ceil( worldWidth / cellSize );
		m_cellsPerCol = (int)FloatMath.ceil( worldHeight / cellSize );
		
		// create the arrays in which we'll keep the entities
		int numCells = m_cellsPerRow * m_cellsPerCol;
		m_dynamicCells = new List[numCells];
		m_staticCells = new List[numCells];
		
		// preallocate the memory in the collections
		for( int i = 0; i < numCells; ++i ) 
		{
			m_dynamicCells[i] = new ArrayList< Entity >( 10 );
			m_staticCells[i] = new ArrayList< Entity >( 10 );
		}

		m_foundObjects = new ArrayList< Entity >( 10 );
	}
	
	/**
	 * Adds a new static object to the grid.
	 * 
	 * @param obj
	 */
	public void insertStaticObject( Entity obj ) 
	{
		int[] cellIds = getCellIds( obj );
		int i = 0;
		int cellId = -1;
		while( i <= 3 && ( cellId = cellIds[i++] ) != -1 ) 
		{
			m_staticCells[cellId].add( obj );
		}
	}
	
	/**
	 * Adds a new dynamic object to the grid.
	 * @param obj
	 */
	public void insertDynamicObject( Entity obj ) 
	{
		int[] cellIds = getCellIds(obj);
		int i = 0;
		int cellId = -1;
		while( i <= 3 && ( cellId = cellIds[i++] ) != -1 ) 
		{
			m_dynamicCells[cellId].add( obj );
		}
	}
	
	/**
	 * Removes an object from the grid
	 * 
	 * @param obj
	 */
	public void removeObject( Entity obj ) 
	{
		int[] cellIds = getCellIds(obj);
		int i = 0;
		int cellId = -1;
		while( i <= 3 && ( cellId = cellIds[i++] ) != -1 ) 
		{
			m_dynamicCells[cellId].remove( obj );
			m_staticCells[cellId].remove( obj );
		}
	}
	
	/**
	 * Removes a dynamic object from the grid.
	 * @param obj
	 */
	public void clearDynamicCells( Entity obj ) 
	{
		int len = m_dynamicCells.length;
		for(int i = 0; i < len; i++) 
		{
			m_dynamicCells[i].clear();
		}
	}
	
	/**
	 * Returns a list of all objects the specified object may collide with.
	 *   
	 * @param obj
	 * @return
	 */
	public List< Entity > getPotentialColliders( Entity obj ) 
	{
		m_foundObjects.clear();
		
		int[] cellIds = getCellIds(obj);
		int i = 0;
		int cellId = -1;
		while( i <= 3 && ( cellId = cellIds[i++] ) != -1 ) 
		{
			// compare against the dynamic objects
			int len = m_dynamicCells[cellId].size();
			for( int j = 0; j < len; ++j ) 
			{
				Entity collider = m_dynamicCells[cellId].get(j);
				
				// check for duplicates
				if( !m_foundObjects.contains( collider ) )
				{
					m_foundObjects.add( collider );
				}
			}
			
			// compare against the static objects
			len = m_staticCells[cellId].size();
			for( int j = 0; j < len; ++j ) 
			{
				Entity collider = m_staticCells[cellId].get(j);
				
				// check for duplicates
				if( !m_foundObjects.contains( collider ) )
				{
					m_foundObjects.add( collider );
				}
			}
		}
		
		return m_foundObjects;
	}
	
	/**
	 * Finds out in which grid cells does the specified object reside at the moment.
	 * 
	 * @param obj
	 * @return
	 */
	public int[] getCellIds( Entity obj ) 
	{
		// TODO: !!!!!!!!!!!!!!!
		
		return m_cellIds;
	}
	
}
