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
		SpatialGrid2D< GridObjectMock > grid = new SpatialGrid2D< GridObjectMock >( 10, 10, 5 );
		grid.insertStaticObject( new GridObjectMock( new BoundingSphere( 2, 2, 0, 2 ) ) );
		
		GridObjectMock movable = new GridObjectMock( new BoundingSphere( 7, 7, 0, 2 ) );
		grid.insertDynamicObject( movable );
		
		grid.update();
		List< GridObjectMock > potentialColliders = grid.getPotentialColliders( movable );
		assertEquals( 0, potentialColliders.size() );
		
		
		movable.m_shape.m_center.set( 6, 6, 0 );
		grid.update();
		potentialColliders = grid.getPotentialColliders( movable );
		assertEquals( 1, potentialColliders.size() );
	}
	
	public void testTwoDynamicObjects()
	{	
		SpatialGrid2D< GridObjectMock > grid = new SpatialGrid2D< GridObjectMock >( 10, 10, 5 );
		GridObjectMock movable1 = new GridObjectMock( new BoundingSphere( 2, 2, 0, 1.5f ) );
		grid.insertDynamicObject( movable1 );
		
		GridObjectMock movable2 = new GridObjectMock( new BoundingSphere( 7, 7, 0, 1.5f ) );
		grid.insertDynamicObject( movable2 );
		
		grid.update();
		List< GridObjectMock > potentialColliders = grid.getPotentialColliders( movable1 );
		assertEquals( 0, potentialColliders.size() );
		potentialColliders = grid.getPotentialColliders( movable2 );
		assertEquals( 0, potentialColliders.size() );
		
		movable1.m_shape.m_center.set( 3, 6, 0 );
		movable2.m_shape.m_center.set( 2, 5, 0 );
		grid.update();
		potentialColliders = grid.getPotentialColliders( movable1 );
		assertEquals( 1, potentialColliders.size() );
		potentialColliders = grid.getPotentialColliders( movable2 );
		assertEquals( 1, potentialColliders.size() );
	}
	
	public void testOutOfBoundsConditions()
	{
		SpatialGrid2D< GridObjectMock > grid = new SpatialGrid2D< GridObjectMock >( 10, 10, 5 );
		grid.insertStaticObject( new GridObjectMock( new BoundingSphere( 2, 2, 0, 2 ) ) );
		
		GridObjectMock movable = new GridObjectMock( new BoundingSphere( 7, 7, 0, 2 ) );
		grid.insertDynamicObject( movable );
		
		grid.update();
		List< GridObjectMock > potentialColliders = grid.getPotentialColliders( movable );
		assertEquals( 0, potentialColliders.size() );
		
		
		movable.m_shape.m_center.set( 11, -11, 0 );
		grid.update();
		potentialColliders = grid.getPotentialColliders( movable );
		assertEquals( 0, potentialColliders.size() );
	}
	
	public void testMarkingLargeArea()
	{
		SpatialGrid2D< GridObjectMock > grid = new SpatialGrid2D< GridObjectMock >( 10, 10, 2 );
		grid.insertStaticObject( new GridObjectMock( new BoundingSphere( 2, 2, 0, 2 ) ) );
		
		GridObjectMock movable = new GridObjectMock( new BoundingSphere( 7, 7, 0, 2 ) );
		grid.insertDynamicObject( movable );
		
		grid.update();
		List< GridObjectMock > potentialColliders = grid.getPotentialColliders( movable );
		assertEquals( 0, potentialColliders.size() );
		
		
		grid.update();
		potentialColliders = grid.getPotentialColliders( new BoundingBox( -100, -100, -100, 100, 100, 100 ) );
		assertEquals( 2, potentialColliders.size() );
	}
}
