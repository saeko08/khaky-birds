package com.hypefoundry.engine.test.renderer2D;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Spline;

import android.test.AndroidTestCase;



public class SplineTest extends AndroidTestCase 
{
	public void testDistanceToPoint()
	{
		Spline spline = new Spline();
		spline.addPoint( new Vector3( 0, 0, 0 ), null );
		spline.addPoint( new Vector3( 10, 0, 0 ), null );
		
		Vector3 tmpPt = new Vector3();
		assertEquals( 5.0f, spline.getNearestPosition( new Vector3( 5, 5, 0 ), tmpPt ) );
		assertEquals( 5.0f, spline.getNearestPosition( new Vector3( 5, -5, 0 ), tmpPt ) );
		assertEquals( 5.0f, spline.getNearestPosition( new Vector3( 15, 0, 0 ), tmpPt ) );
		assertEquals( 5.0f, spline.getNearestPosition( new Vector3( -5, 0, 0 ), tmpPt ) );
	}
	
	public void testNearestPoint()
	{
		Spline spline = new Spline();
		spline.addPoint( new Vector3( 1.89f, 0, 10 ), null );
		spline.addPoint( new Vector3( 1.89f, 9.6f, 10 ), null );
		
		Vector3 nearestPoint = new Vector3();
		spline.getNearestPosition( new Vector3( 1.88f, 4.2f, 10 ), nearestPoint );
		assertTrue( nearestPoint.dist( new Vector3( 1.89f, 4.2f, 10 ) ) < 1e-3 );
	}
	
	public void testRefreshing()
	{
		Spline spline = new Spline();
		spline.addPoint( new Vector3( 0, 0, 10 ), null );
		spline.addPoint( new Vector3( 0, 10, 10 ), null );
		
		// check the initial values
		assertEquals( 10.0f, spline.m_lengths[0] );
		assertTrue( spline.m_directions[0].dist( new Vector3( 0, 1, 0 ) ) < 1e-3 );
		
		// change the spline's shape
		spline.m_points[1].set( 5, 0, 10 );
		spline.refresh();
		
		// verify that the values have changed
		assertEquals( 5.0f, spline.m_lengths[0] );
		assertTrue( spline.m_directions[0].dist( new Vector3( 1, 0, 0 ) ) < 1e-3 );
	}
	
	public void testTransformations()
	{
		Spline spline = new Spline();
		spline.addPoint( new Vector3( 0, 5, 10 ), null );
		spline.addPoint( new Vector3( 0, 10, 10 ), null );
		spline.addPoint( new Vector3( 5, 10, 10 ), null );
		
		Vector3 transformedPt = new Vector3();
		
		// test a few points
		spline.transform( new Vector3( 0, 0, 0 ), transformedPt );
		assertTrue( transformedPt.dist( new Vector3( 0, 5, 10 ) ) < 1e-3 );
		
		spline.transform( new Vector3( 3, 0, 0 ), transformedPt );
		assertTrue( transformedPt.dist( new Vector3( 0, 8, 10 ) ) < 1e-3 );
		
		spline.transform( new Vector3( 6, 0, 0 ), transformedPt );
		assertTrue( transformedPt.dist( new Vector3( 1, 10, 10 ) ) < 1e-3 );
		
		spline.transform( new Vector3( 10, 0, 0 ), transformedPt );
		assertTrue( transformedPt.dist( new Vector3( 5, 10, 10 ) ) < 1e-3 );
		
		// out of bounds conditions
		spline.transform( new Vector3( -1, 0, 0 ), transformedPt );
		assertTrue( transformedPt.dist( new Vector3( 0, 5, 10 ) ) < 1e-3 );
		
		spline.transform( new Vector3( 11, 0, 0 ), transformedPt );
		assertTrue( transformedPt.dist( new Vector3( 5, 10, 10 ) ) < 1e-3 );
		
		// points on the sides
		spline.transform( new Vector3( 3, 1, 0 ), transformedPt );
		assertTrue( transformedPt.dist( new Vector3( 1, 8, 10 ) ) < 1e-3 );
		
	}
}
