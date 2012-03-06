/**
 * 
 */
package com.hypefoundry.engine.gestures;

import java.util.List;

import com.hypefoundry.engine.math.Vector3;

/**
 * Algorithm behind the gestures recognition.
 * 
 * @author Paksas
 */
public class GesturesAnalyzer 
{
	private final float		DIRECTION_SIMILARITY_TOLERANCE		= 0.5f; //  directions are considered the same if their cos(angle) > cos(60)
	private final float		MIN_LEG_LENGTH_SQ					= 100.0f; // minimum gesture's leg length ( squared value expressed in pixels )
	
	private Vector3[]		m_tmpDirections						= new Vector3[ GesturesRecognition.MAX_SAMPLES ];
	
	
	public GesturesAnalyzer()
	{
		for ( int i = 0; i < GesturesRecognition.MAX_SAMPLES; ++i )
		{
			m_tmpDirections[i] = new Vector3();	
		}
	}
	
	/**
	 * Analyzes the specified velocities in terms of the gesture given for comparison, trying
	 * to find they match.
	 * 
	 * @param velocities
	 * @param velocitiesCount
	 * @param gesturesDB
	 * 
	 * @return best matching gesture, or null if none was found worthy
	 */
	public Gesture analyze( Vector3[] points, int samplesCount, List< Gesture > gesturesDB )
	{
		if ( samplesCount <= 1 )
		{
			// too short for a gesture
			return null;
		}
		
		// first - sample the directions
		int instanceDirectionsCount = 0;
		for ( int i = 1; i < samplesCount; ++i )
		{
			m_tmpDirections[instanceDirectionsCount].set( points[i] ).sub( points[i - 1] );
			float len = m_tmpDirections[instanceDirectionsCount].magSq2D();
			if ( len < MIN_LEG_LENGTH_SQ )
			{
				// gesture's leg is too short - skip it
				continue;
			}
			
			m_tmpDirections[instanceDirectionsCount].normalize2D();
			if ( instanceDirectionsCount > 0 )
			{
				// compare with the previous direction - if they are more or less the same, 
				// consider it one direction
				float dirDot = m_tmpDirections[instanceDirectionsCount].dot2D( m_tmpDirections[instanceDirectionsCount - 1] );
				if ( dirDot < DIRECTION_SIMILARITY_TOLERANCE )
				{
					// they are not the same - memorize this one
					++instanceDirectionsCount;
				}
			}
			else
			{
				// always memorize the first direction
				++instanceDirectionsCount;
			}
		}
		
		
		int gesturesCount = gesturesDB.size();
		float highestMatchFactor = 0.0f;
		Gesture bestMatchingGesture = null;
		for ( int i = 0; i < gesturesCount; ++i )
		{
			Gesture gesture = gesturesDB.get(i);
			float matchFactor = gesture.analyzeGesture( m_tmpDirections, instanceDirectionsCount );
			
			if ( matchFactor > highestMatchFactor )
			{
				// we found a new best matching gesture
				highestMatchFactor = matchFactor;
				bestMatchingGesture = gesture;
			}
		}
		
		return bestMatchingGesture;
	}
}
