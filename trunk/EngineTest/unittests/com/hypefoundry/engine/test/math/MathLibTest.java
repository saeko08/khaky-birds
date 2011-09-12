package com.hypefoundry.engine.test.math;

import com.hypefoundry.engine.math.MathLib;

import android.test.AndroidTestCase;

public class MathLibTest extends AndroidTestCase
{
	public void testNormalizeAngle()
	{
		assertEquals( 0.0f, MathLib.normalizeAngle( 0 ) );
		assertEquals( 180.0f, MathLib.normalizeAngle( 180 ) );
		assertEquals( 359.0f, MathLib.normalizeAngle( 359 ) );
		assertEquals( 0.0f, MathLib.normalizeAngle( 360 ) );
		assertEquals( 120.0f, MathLib.normalizeAngle( 360+120 ) );
		assertEquals( 180.0f, MathLib.normalizeAngle( -180 ) );
		assertEquals( 90.0f, MathLib.normalizeAngle( -270 ) );
		assertEquals( 355.0f, MathLib.normalizeAngle( -365 ) );
	}
}
