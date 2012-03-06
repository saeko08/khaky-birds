/**
 * 
 */
package com.hypefoundry.engine.gestures.gestureTypes;

import com.hypefoundry.engine.gestures.Gesture;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * @author Paksas
 *
 */
public class AngularGesture extends Gesture 
{
	// static info about a gesture
	public int		m_directionChanges;
	public float	m_desiredAngle;
	public float	m_tolerance;
	
	@Override
	public float analyzeGesture( Vector3[] instanceDirections, int instanceDirectionsCount )
	{
		int requiredDirectionsCount = m_directionChanges + 1;
		if ( instanceDirectionsCount < requiredDirectionsCount )
		{
			// not enough direction changes
			return 0.0f;
		}
		
		// search for the first occurance of the desired directions change
		float probability = 1.0f;
		for ( int i = 1; i < requiredDirectionsCount; ++i )
		{
			float dot = instanceDirections[i].dot2D( instanceDirections[i - 1] );
			float angle = (float)Math.acos( dot );
			
			float angleDiff = Math.abs( m_desiredAngle - angle );
			angleDiff = Math.min( angleDiff, m_tolerance );
		
			float partialProbability = ( m_tolerance - angleDiff ) / m_tolerance;
			probability = Math.min( probability, partialProbability );
		}
		
		return probability;
	}
		
	@Override
	protected void onLoad(DataLoader loader)
	{
		m_directionChanges = loader.getIntValue( "directionChanges" );
		m_desiredAngle = loader.getFloatValue( "angle" ) * (float)Math.PI / 180.0f;
		m_tolerance = loader.getFloatValue( "angleTolerance" ) * (float)Math.PI / 180.0f;
	}
}
