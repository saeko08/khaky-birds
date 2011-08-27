package com.hypefoundry.engine.test.renderer2D;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Color;
import com.hypefoundry.engine.renderer2D.Spline;

import android.test.AndroidTestCase;



public class SplineTest extends AndroidTestCase 
{
	public void testDistanceToPoint()
	{
		Spline spline = new Spline( Color.RED );
		spline.addPoint( new Vector3( 0, 0, 0 ) );
		spline.addPoint( new Vector3( 10, 0, 0 ) );
		
		Vector3 tmpPt = new Vector3();
		assertEquals( 5.0f, spline.getNearestPosition( new Vector3( 5, 5, 0 ), tmpPt ) );
		assertEquals( 5.0f, spline.getNearestPosition( new Vector3( 5, -5, 0 ), tmpPt ) );
		assertEquals( 5.0f, spline.getNearestPosition( new Vector3( 15, 0, 0 ), tmpPt ) );
		assertEquals( 5.0f, spline.getNearestPosition( new Vector3( -5, 0, 0 ), tmpPt ) );
	}
	
	public void testNearestPoint()
	{
		Spline spline = new Spline( Color.RED );
		spline.addPoint( new Vector3( 1.89f, 0, 10 ) );
		spline.addPoint( new Vector3( 1.89f, 9.6f, 10 ) );
		
		Vector3 nearestPoint = new Vector3();
		spline.getNearestPosition( new Vector3( 1.88f, 4.2f, 10 ), nearestPoint );
		assertTrue( nearestPoint.dist( new Vector3( 1.89f, 4.2f, 10 ) ) < 1e-3 );
	}
}
