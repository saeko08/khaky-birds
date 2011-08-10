package com.hypefoundry.engine.test.util;

import java.util.List;

import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.BoundingSphere;
import com.hypefoundry.engine.util.SpatialGrid2D;
import com.hypefoundry.engine.util.SpatialGridObject;

import android.test.AndroidTestCase;
import android.util.Log;

public class SpatialGrid2DUpdatePerformanceTests extends AndroidTestCase
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
	
	final short MAX_CLIENT_SYSTEMS = 3;
	
	public void testLargeGridFewObjects()
	{
		final short MAX_ENTITIES_COUNT = 5;
		SpatialGrid2D grid = new SpatialGrid2D( 1000, 1000, 5, MAX_ENTITIES_COUNT );
		grid.insertDynamicObject( new GridObjectMock( new BoundingSphere( 2, 2, 0, 2 ) ) );
		grid.insertDynamicObject( new GridObjectMock( new BoundingSphere( 7, 7, 0, 2 ) ) );
	
		long startTime = System.nanoTime();
		for ( int i = 0; i < MAX_CLIENT_SYSTEMS; ++i )
		{
			grid.update();
		}
		long endTime = System.nanoTime();
		long duration = ( endTime - startTime ) / 1000000;
		assertTrue( new StringBuilder().append( "Actual duration: " ).append( duration ).append( "[ms]" ).toString(), duration < 2 ); // 2 ms
	}
	
	public void testSmallGridManyObjects()
	{
		final short MAX_ENTITIES_COUNT = 100;
		SpatialGrid2D grid = new SpatialGrid2D( 10, 10, 5, MAX_ENTITIES_COUNT );
				
		for ( int i = 0; i < MAX_ENTITIES_COUNT; ++i )
		{
			grid.insertDynamicObject( new GridObjectMock( new BoundingSphere( (float)( Math.random() * 8.0f + 1.0f ), (float)( Math.random() * 8.0f + 1.0f ), 0, 0.5f ) ) );
		}
		
		long startTime = System.nanoTime();
		for ( int i = 0; i < MAX_CLIENT_SYSTEMS; ++i )
		{
			grid.update();
		}
		long endTime = System.nanoTime();
		long duration = ( endTime - startTime ) / 1000000;
		assertTrue( new StringBuilder().append( "Actual duration: " ).append( duration ).append( "[ms]" ).toString(), duration < 20 ); // 20 ms
	}
}
