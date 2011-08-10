package com.hypefoundry.engine.test.util;

import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.BoundingSphere;
import com.hypefoundry.engine.util.SpatialGrid2D;
import com.hypefoundry.engine.util.SpatialGridObject;

import android.test.AndroidTestCase;


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
	
	private final int MAX_INTERESTED_OBJECTS = 10;
	
	public void testLargeGridFewObjects()
	{
		final short MAX_ENTITIES_COUNT = 5;
		SpatialGrid2D grid = new SpatialGrid2D( 1000, 1000, 5, MAX_ENTITIES_COUNT );
		grid.insertStaticObject( new GridObjectMock( new BoundingSphere( 2, 2, 0, 2 ) ) );
		grid.insertDynamicObject( new GridObjectMock( new BoundingSphere( 7, 7, 0, 2 ) ) );
		
		GridObjectMock queryObj = new GridObjectMock( new BoundingSphere( 7, 7, 0, 2 ) );
		grid.insertDynamicObject( queryObj);
		grid.update();
		
		long startTime = System.nanoTime();
		GridObjectMock[] colliders = new GridObjectMock[MAX_ENTITIES_COUNT];
		for ( int i = 0; i < MAX_INTERESTED_OBJECTS; ++i )
		{
			grid.getPotentialColliders( queryObj, colliders );
		}
		long endTime = System.nanoTime();
		long duration = ( endTime - startTime ) / 1000000;
		assertTrue( new StringBuilder().append( "Actual duration: " ).append( duration ).append( "[ms]" ).toString(), duration < 1 ); // 1 ms
	}
	
	public void testSmallGridManyObjects()
	{
		final int STATIC_ENTITIES_COUNT = 200;
		final int DYNAMIC_ENTITIES_COUNT = 100;
		final short MAX_ENTITIES_COUNT = STATIC_ENTITIES_COUNT + DYNAMIC_ENTITIES_COUNT;
		SpatialGrid2D grid = new SpatialGrid2D( 10, 10, 5, MAX_ENTITIES_COUNT );
		
		for ( int i = 0; i < STATIC_ENTITIES_COUNT; ++i )
		{
			grid.insertStaticObject( new GridObjectMock( new BoundingSphere( (float)( Math.random() * 8.0f + 1.0f ), (float)( Math.random() * 8.0f + 1.0f ), 0, 0.5f ) ) );
		}
		for ( int i = 0; i < DYNAMIC_ENTITIES_COUNT; ++i )
		{
			grid.insertDynamicObject( new GridObjectMock( new BoundingSphere( (float)( Math.random() * 8.0f + 1.0f ), (float)( Math.random() * 8.0f + 1.0f ), 0, 0.5f ) ) );
		}
		
		GridObjectMock queryObj = new GridObjectMock( new BoundingSphere( (float)( Math.random() * 8.0f + 1.0f ), (float)( Math.random() * 8.0f + 1.0f ), 0, 0.5f ) );
		grid.insertDynamicObject( queryObj);
		grid.update();
		
		long startTime = System.nanoTime();
		GridObjectMock[] colliders = new GridObjectMock[MAX_ENTITIES_COUNT];
		for ( int i = 0; i < MAX_INTERESTED_OBJECTS; ++i )
		{
			grid.getPotentialColliders( queryObj, colliders );
		}
		long endTime = System.nanoTime();
		long duration = ( endTime - startTime ) / 1000000;
		assertTrue( new StringBuilder().append( "Actual duration: " ).append( duration ).append( "[ms]" ).toString(), duration < 10 ); // 10 ms
	}
}
