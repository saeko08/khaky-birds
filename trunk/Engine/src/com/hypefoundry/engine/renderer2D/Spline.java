/**
 * 
 */
package com.hypefoundry.engine.renderer2D;


import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.util.Arrays;


/**
 * A line that can be bent.
 * 
 * @author paksas
 *
 */
public final class Spline 
{
	public Color				m_color;
	public Vector3[]			m_points = null;
	private Vector3[]			m_directions = null;
	private float[]				m_lengths = null;
	
	// temporary variables
	private Vector3				m_tmpDirToPoint = new Vector3();
	private Vector3				m_tmpCastPoint = new Vector3();
	
	
	/**
	 * Constructor.
	 * 
	 * @param color		spline color
	 */
	public Spline( Color color )
	{
		m_color = color;
	}
	
	/**
	 * Adds a new point to the spline.
	 * 
	 * @param 	point
	 * @return	self instance allowing to chain those method calls
	 */
	public Spline addPoint( Vector3 point )
	{
		if ( point == null )
		{
			return this;
		}
		
		m_points = Arrays.append( m_points, point );
			
		if ( m_points.length >= 2 )
		{
			Vector3 start = m_points[ m_points.length - 2 ];
			Vector3 end = m_points[ m_points.length - 1 ];
			
			// calculate a direction vector of the segment
			Vector3 dir = new Vector3( end ).sub( start );
			m_lengths = Arrays.append( m_lengths, dir.mag() );
			m_directions = Arrays.append( m_directions, dir.normalize() );
		}
		
		return this;
	}
	
	/**
	 * Calculates the position on the spline nearest to the current position.
	 * 
	 * @param point
	 * @param outPos
	 */
	public float getNearestPosition( final Vector3 point, Vector3 outPos ) 
	{
		if ( m_points.length < 2 )
		{
			// this is not a line - it contains only one vertex
			return Float.MAX_VALUE;		
		}
		
		// find the segment the point is closest to and return that distance
		float minDistSq = Float.MAX_VALUE;
		for ( int i = 0; i < m_points.length - 1; ++i )
		{
			Vector3 vertex = m_points[i];
			Vector3 direction = m_directions[i];
			m_tmpDirToPoint.set( point ).sub( vertex );
			
			// cast the vector onto the direction of the line
			float t = m_tmpDirToPoint.dot( direction );
			float distSq = 0;
			if ( t > 0 && t < m_lengths[i] )
			{
				m_tmpCastPoint.set( direction ).scale( t ).add( vertex );
			}
			else
			{
				m_tmpCastPoint.set( vertex );
			}

			distSq = m_tmpCastPoint.distSq( point );
			if ( distSq < minDistSq )
			{
				outPos.set( m_tmpCastPoint );
				minDistSq = distSq;
			}
		}
		
		// check the last point as well
		float distSq = m_points[ m_points.length - 1 ].distSq( point );
		if ( distSq < minDistSq )
		{
			outPos.set( m_points[ m_points.length - 1 ] );
			minDistSq = distSq;
		}
		return (float)Math.sqrt( minDistSq );
	}

}
