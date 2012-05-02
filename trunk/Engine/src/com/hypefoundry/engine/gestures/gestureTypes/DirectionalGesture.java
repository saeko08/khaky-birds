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
public class DirectionalGesture extends Gesture 
{
	private final float		DIRECTION_TOLERANCE					= 0.866f; // 30 degrees of tolerance with respect to each gesture's leg direction
	
	// static info about a gesture
	public Vector3[]		m_directions 						= new Vector3[0];
	
	
		
	@Override
	public float analyzeGesture( Vector3[] instanceDirections, int instanceDirectionsCount )
	{
		int blueprintDirectionsCount = m_directions.length;
		if ( instanceDirectionsCount < blueprintDirectionsCount )
		{
			// the instance has less directions than the blueprint, so it can't possibly be that gesture
			return 0.0f;
		}
		
		// on the other hand however, if the instance has MORE directions than the blueprint, it doesn't necessarily
		// mean that the gesture's all bad - maybe the user drew additional leg while taking his finger off etc.
		// It's still worth testing
		
		// compare partial directions - if any doesn't match, then it means it's not the gesture we're looking for
		float probability = 1.0f;
		for ( int i = 0; i < blueprintDirectionsCount; ++i )
		{
			float dirDot = instanceDirections[i].dot( m_directions[i] );
			dirDot = Math.max( dirDot, DIRECTION_TOLERANCE );
			probability = Math.min( probability, dirDot );
			
			if ( probability < DIRECTION_TOLERANCE )
			{
				// that's not the gesture
				return 0.0f;
			}
		}
		
		// right now the probability is in [DIRECTION_TOLERANCE..1] range - map it back to [0..1]
		probability = ( probability - DIRECTION_TOLERANCE ) / ( 1.0f - DIRECTION_TOLERANCE );
		
		// now - if there were more legs than in the blueprint, decrease the final probability
		// by scaling it by the percentage of the legs analyzed
		float legsAnalyzedFactor = (float)blueprintDirectionsCount / (float)instanceDirectionsCount;
		probability *= legsAnalyzedFactor;
		
		return probability;		
	}
	
	@Override
	public int getRequiredSamplesCount()
	{
		return 2;
	}
	
	@Override
	protected void onLoad(DataLoader loader)
	{
		// load directions
		int directionsCount = loader.getChildrenCount( "Direction" );
		m_directions = new Vector3[ directionsCount ];
		
		int i = 0;
		for ( DataLoader dirNode = loader.getChild( "Direction" ); dirNode != null; dirNode = dirNode.getSibling(), ++i )
		{
			m_directions[i] = new Vector3();
			m_directions[i].load( dirNode );
			m_directions[i].normalize2D();
		}		
	}
}
