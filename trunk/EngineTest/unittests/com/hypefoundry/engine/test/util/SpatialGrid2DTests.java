package com.hypefoundry.engine.test.util;

import java.util.List;

import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.BoundingSphere;
import com.hypefoundry.engine.util.SpatialGrid2D;
import com.hypefoundry.engine.util.SpatialGridObject;

import android.test.AndroidTestCase;


public class SpatialGrid2DTests extends AndroidTestCase 
{
	private class GridObjectMock implements SpatialGridObject
	{
		public BoundingSphere	m_shape;
		private BoundingBox		m_boundingBox;
		
		public GridObjectMock( BoundingSphere shape )
		{
			m_shape = shape;
			m_boundingBox = new BoundingBox();
		}
		
		@Override
		public BoundingBox getBounds()
		{
			m_boundingBox.set( m_shape );
			return m_boundingBox;
		}
	}
	
	public void testStaticAndDynamicObject()
	{	
		final short MAX_ENTITIES_COUNT = 5;
		SpatialGrid2D grid = new SpatialGrid2D( 10, 10, 5, MAX_ENTITIES_COUNT );
		grid.insertStaticObject( new GridObjectMock( new BoundingSphere( 2, 2, 2 ) ) );
		
		GridObjectMock movable = new GridObjectMock( new BoundingSphere( 7, 7, 2 ) );
		grid.insertDynamicObject( movable );
		
		grid.update();
		GridObjectMock[] colliders = new GridObjectMock[MAX_ENTITIES_COUNT];
		int collidersCount = grid.getPotentialColliders( movable, colliders );
		assertEquals( 1, collidersCount );
		
		
		movable.m_shape.m_center.set( 6, 6, 0 );
		grid.update();
		collidersCount = grid.getPotentialColliders( movable, colliders );
		assertEquals( 2, collidersCount );
	}
	
	public void testTwoDynamicObjects()
	{	
		final short MAX_ENTITIES_COUNT = 5;
		SpatialGrid2D grid = new SpatialGrid2D( 10, 10, 5, MAX_ENTITIES_COUNT );
		GridObjectMock movable1 = new GridObjectMock( new BoundingSphere( 2, 2, 1.5f ) );
		grid.insertDynamicObject( movable1 );
		
		GridObjectMock movable2 = new GridObjectMock( new BoundingSphere( 7, 7, 1.5f ) );
		grid.insertDynamicObject( movable2 );
		
		grid.update();
		GridObjectMock[] colliders = new GridObjectMock[MAX_ENTITIES_COUNT];
		int collidersCount = grid.getPotentialColliders( movable1, colliders );
		assertEquals( 1, collidersCount );
		collidersCount = grid.getPotentialColliders( movable2, colliders );
		assertEquals( 1, collidersCount );
		
		movable1.m_shape.m_center.set( 3, 6, 0 );
		movable2.m_shape.m_center.set( 2, 5, 0 );
		grid.update();
		collidersCount = grid.getPotentialColliders( movable1, colliders );
		assertEquals( 2, collidersCount );
		collidersCount = grid.getPotentialColliders( movable2, colliders );
		assertEquals( 2, collidersCount );
	}
	
	public void testOutOfBoundsConditions()
	{
		final short MAX_ENTITIES_COUNT = 5;
		SpatialGrid2D grid = new SpatialGrid2D( 10, 10, 5, MAX_ENTITIES_COUNT );
		grid.insertStaticObject( new GridObjectMock( new BoundingSphere( 2, 2, 2 ) ) );
		
		GridObjectMock movable = new GridObjectMock( new BoundingSphere( 7, 7, 2 ) );
		grid.insertDynamicObject( movable );
		
		grid.update();
		GridObjectMock[] colliders = new GridObjectMock[MAX_ENTITIES_COUNT];
		int collidersCount = grid.getPotentialColliders( movable, colliders );
		assertEquals( 1, collidersCount );
		
		
		movable.m_shape.m_center.set( 11, -11, 0 );
		grid.update();
		collidersCount = grid.getPotentialColliders( movable, colliders );
		assertEquals( 1, collidersCount );
	}
	
	public void testMarkingLargeArea()
	{
		final short MAX_ENTITIES_COUNT = 5;
		SpatialGrid2D grid = new SpatialGrid2D( 10, 10, 2, MAX_ENTITIES_COUNT );
		grid.insertStaticObject( new GridObjectMock( new BoundingSphere( 1, 1, 2 ) ) );
		
		GridObjectMock movable = new GridObjectMock( new BoundingSphere( 8, 8, 2 ) );
		grid.insertDynamicObject( movable );
		
		grid.update();
		GridObjectMock[] colliders = new GridObjectMock[MAX_ENTITIES_COUNT];
		int collidersCount = grid.getPotentialColliders( movable, colliders );
		assertEquals( 1, collidersCount );
			
		grid.update();
		collidersCount = grid.getPotentialColliders( new BoundingBox( -100, -100, 100, 100 ), colliders );
		assertEquals( 2, collidersCount );
	}
}
