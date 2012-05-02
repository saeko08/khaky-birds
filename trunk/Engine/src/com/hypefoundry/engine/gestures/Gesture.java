/**
 * 
 */
package com.hypefoundry.engine.gestures;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * A recognizable gesture definition.
 * 
 * @author Paksas
 */
public abstract class Gesture 
{
	// static info about a gesture
	
	// gesture ID
	public String			m_id;
	
	// can the gesture be performed without taking the finger off the screen
	public boolean			m_continuous = false;
	

	// runtime info about the most recent instance of the gesture
	private Vector3[]		m_samplePoints 		= new Vector3[GesturesRecognition.MAX_SAMPLES];
	private int				m_samplesCount		= 0;
	
	/**
	 * Constructor.
	 */
	public Gesture()
	{
		for ( int i = 0; i < GesturesRecognition.MAX_SAMPLES; ++i )
		{
			m_samplePoints[i] = new Vector3();
		}
	}
	
	/**
	 * Compares the specified gesture against the analyzed samples.
	 * 
	 * @param instanceDirections
	 * @param instanceDirectionsCount
	 * 
	 * @return similarity factor in range [0..1]
	 */
	public abstract float analyzeGesture( Vector3[] instanceDirections, int instanceDirectionsCount );
	
	/**
	 * Returns the minimum number of samples required to match the gesture.
	 * 
	 * @return
	 */
	public abstract int getRequiredSamplesCount();
	
	// ------------------------------------------------------------------------
	// Utility methods
	// ------------------------------------------------------------------------
	
	/**
	 * Calculates and returns gesture's start position.
	 * 
	 * @param outCenter
	 */
	public void getStart( Vector3 outCenter )
	{
		if ( m_samplesCount > 0 )
		{
			outCenter.set( m_samplePoints[0] );
		}
		else
		{
			outCenter.set( 0.0f, 0.0f, 0.0f );
		}

	}
	
	/**
	 * Calculates and returns gesture's center.
	 * 
	 * @param outCenter
	 */
	public void getCenter( Vector3 outCenter )
	{
		outCenter.set( 0.0f, 0.0f, 0.0f );
		
		for ( int i = 0; i < m_samplesCount; ++i )
		{
			outCenter.add( m_samplePoints[i] );
		}
		outCenter.scale( 1.0f / (float)m_samplesCount );
	}
	
	// ------------------------------------------------------------------------
	// Serialization support
	// ------------------------------------------------------------------------

	/**
	 * Loads the gesture definition.
	 * 
	 * @param loader
	 */
	public void load( DataLoader loader ) 
	{
		m_id = loader.getStringValue( "id" );
		m_continuous = loader.getBoolValue( "continuous" );
				
		onLoad( loader );
	}
	
	/**
	 * Loads implementation-specific gesture data.
	 * @param loader
	 */
	protected abstract void onLoad(  DataLoader loader );

	/**
	 * Sets the sample points of the latest gesture instance
	 * 
	 * @param points
	 * @param samplesCount
	 */
	public void setInstancePoints( Vector3[] points, int samplesCount ) 
	{
		m_samplesCount = samplesCount;
		for ( int i = 0; i < m_samplesCount; ++i )
		{
			m_samplePoints[i].set( points[i] );
		}
	}
}
