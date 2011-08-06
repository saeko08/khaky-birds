package com.hypefoundry.engine.test.math;

import com.hypefoundry.engine.math.Vector3;

import android.test.AndroidTestCase;


public class Vector3Test extends AndroidTestCase 
{
	public void testAngleBetweenVectors()
	{
		Vector3 ox = new Vector3( 1, 0, 0 );
		Vector3 negOx = new Vector3( -1, 0, 0 );
		Vector3 oy = new Vector3( 0, 1, 0 );
		Vector3 negOy = new Vector3( 0, -1, 0 );
		
		assertEquals( 0.0f, ox.getAngleBetween( ox ) );
		assertEquals( -180.0f, ox.getAngleBetween( negOx ) );
		assertEquals( -90.0f, ox.getAngleBetween( oy ) );
		assertEquals( 90.0f, ox.getAngleBetween( negOy ) );
	}
}
