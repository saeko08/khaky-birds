/**
 * 
 */
package com.hypefoundry.engine.math;

/**
 * Helper math functions.
 * 
 * @author azagor
 *
 */
public final class MathLib 
{
	private static Vector3 m_tmpDirVec = new Vector3();
	
	/**
	 * Calculates an angular difference that will get one to look at the specified position.
	 * 
	 * @param targetPos			what do we wnat to look at
	 * @param myPos				where are we right now
	 * @param myFacing			where are we looking right now
	 * 
	 * @return how much should `myFacing` change to look at the `targetPos`
	 */
	public static float lookAtDiff( Vector3 targetPos, Vector3 myPos, float myFacing )
	{
		m_tmpDirVec.set( targetPos ).sub( myPos );
		float angleToBird = m_tmpDirVec.angleXY();
		float angleDiff = Math.abs( angleToBird - myFacing );
		
		return angleDiff;
	}
	
	/**
	 * Normalizes an angle by limiting it to (0, 360) range
	 * 
	 * @param angle
	 * @return normalized angle
	 */
	public static float normalizeAngle( float angle )
	{
		if ( angle < 0 )
		{
			int mul = (int) (angle / 360);
			angle = angle - mul*360;
			
			angle += 360.0f;
		}

		int mul = (int) (angle / 360);
		angle = angle - mul*360;
		
		return angle;
	}
	
	/**
	 * Changes angle's range from (-360, 360 ) to ( -180, 180 )
	 * 
	 * @param angle
	 * @return
	 */
	public static float getDiffAngle( float angle )
	{
		if ( angle > 180.0f )
		{
			angle = angle - 360.0f;
		}
		else if ( angle < -180.0f )
		{
			angle = angle + 360.0f;
		}
		return angle;
	}
}
