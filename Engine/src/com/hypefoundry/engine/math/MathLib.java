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
	
	/**
	 * Clamps the value to the specified range.
	 * 
	 * @param val
	 * @param min
	 * @param max
	 * @return
	 */
	public static float clamp( float val, float min, float max ) 
	{
		if ( val < min )
		{
			return min;
		}
		else if ( val > max )
		{
			return max;
		}
		else
		{
			return val;
		}
	}
	
	/**
	 * Processes the dimensions with respect to the specified viewport size
	 * to fill it to the maximum possible extents while preserving the aspect ratio.
	 * 
	 * @param inViewportSize
	 * @param inOutDimensions
	 */
	public static void processAspectRatio( Vector3 inViewportSize, Vector3 inOutDimensions ) 
	{
		float ratio = inOutDimensions.m_x / inOutDimensions.m_y;
		
		float dW1 = inViewportSize.m_x;
		float dH1 = inViewportSize.m_x / ratio;
		
		float dW2 = inViewportSize.m_y * ratio;
		float dH2 = inViewportSize.m_y;

		if ( dW1 <= inViewportSize.m_x && dH1 <= inViewportSize.m_y )
		{
			inOutDimensions.m_x = dW1;
			inOutDimensions.m_y = dH1;
		}
		else
		{
			inOutDimensions.m_x = dW2;
			inOutDimensions.m_y = dH2;
		}
	}
}
