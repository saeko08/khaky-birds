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
}
