package com.hypefoundry.engine.test.util;

import java.util.List;

import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.BoundingSphere;
import com.hypefoundry.engine.util.SpatialGrid2D;
import com.hypefoundry.engine.util.SpatialGridObject;

import android.test.AndroidTestCase;
import android.util.Log;

public class SpatialGrid2DQueryPerformanceTests extends AndroidTestCase 
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
	
	public void testLargeGridFewObjects()
	{
		SpatialGrid2D< GridObjectMock > grid = new SpatialGrid2D< GridObjectMock >( 1000, 1000, 5 );
		grid.insertStaticObject( new GridObjectMock( new BoundingSphere( 2, 2, 0, 2 ) ) );
		grid.insertDynamicObject( new GridObjectMock( new BoundingSphere( 7, 7, 0, 2 ) ) );
		
		GridObjectMock queryObj = new GridObjectMock( new BoundingSphere( 7, 7, 0, 2 ) );
		grid.insertDynamicObject( queryObj);
		grid.update();
		
		long startTime = System.nanoTime();
		List< GridObjectMock > colliders = null;
		for ( int i = 0; i < 100; ++i )
		{
			colliders = grid.getPotentialColliders( queryObj );
		}
		long endTime = System.nanoTime();
		long duration = ( endTime - startTime ) / 1000000;
		assertTrue( duration < 20 ); // 20 ms
	}
	
	public void testSmallGridManyObjects()
	{
		SpatialGrid2D< GridObjectMock > grid = new SpatialGrid2D< GridObjectMock >( 10, 10, 5 );
		
		for ( int i = 0; i < 200; ++i )
		{
			grid.insertStaticObject( new GridObjectMock( new BoundingSphere( (float)( Math.random() * 8.0f + 1.0f ), (float)( Math.random() * 8.0f + 1.0f ), 0, 0.5f ) ) );
		}
		for ( int i = 0; i < 200; ++i )
		{
			grid.insertDynamicObject( new GridObjectMock( new BoundingSphere( (float)( Math.random() * 8.0f + 1.0f ), (float)( Math.random() * 8.0f + 1.0f ), 0, 0.5f ) ) );
		}
		
		GridObjectMock queryObj = new GridObjectMock( new BoundingSphere( (float)( Math.random() * 8.0f + 1.0f ), (float)( Math.random() * 8.0f + 1.0f ), 0, 0.5f ) );
		grid.insertDynamicObject( queryObj);
		grid.update();
		
		long startTime = System.nanoTime();
		List< GridObjectMock > colliders = null;
		for ( int i = 0; i < 100; ++i )
		{
			colliders = grid.getPotentialColliders( queryObj );
		}
		long endTime = System.nanoTime();
		long duration = ( endTime - startTime ) / 1000000;
		assertTrue( duration < 500 ); // 500 ms
	}
}
