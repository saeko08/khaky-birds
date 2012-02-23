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
	public Vector3[]			m_points = null;
	public Color[]				m_colors = null;
	public Vector3[]			m_directions = null;
	public float[]				m_lengths = null;
	
	// temporary variables
	private Vector3				m_tmpDirToPoint = new Vector3();
	private Vector3				m_tmpCastPoint = new Vector3();
	
	
	/**
	 * Adds a new point to the spline.
	 * 
	 * @param 	point
	 * @return	self instance allowing to chain those method calls
	 */
	public Spline addPoint( Vector3 point )
	{
		addPoint( point, null );
		
		return this;
	}
	
	/**
	 * Adds a new point to the spline.
	 * 
	 * @param 	point
	 * @param   color
	 * @return	self instance allowing to chain those method calls
	 */
	public Spline addPoint( Vector3 point, Color color )
	{
		if ( point == null )
		{
			return this;
		}
		
		m_points = Arrays.append( m_points, point );
		m_colors = Arrays.append( m_colors, color );
			
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
	
	/**
	 * Transforms a position to be relative to the spline. 
	 * 
	 * @param inPos
	 * @param outTransformedPos
	 */
	public void transform( Vector3 inPos, Vector3 outTransformedPos )
	{
		// find the index of a segment in which the position is located
		float relLength = 0;
		int segIdx = 0;
		for ( ; segIdx < m_lengths.length; ++segIdx )
		{
			float newLength = relLength + m_lengths[segIdx];
			if ( newLength >= inPos.m_x )
			{
				// this is the segment
				break;
			}
			relLength = newLength;
		}
		
		// limit the segment index to the number of segments
		if ( segIdx >= m_lengths.length )
		{
			segIdx = m_lengths.length - 1;
			relLength -= m_lengths[segIdx];
		}
		
		// get the distance along the segment where the point should be
		float t = inPos.m_x - relLength;
		if ( t < 0 )
		{
			t = 0;
		}
		else if ( t > m_lengths[segIdx] )
		{
			t = m_lengths[segIdx];
		}

		// get the direction perpendicular to the spline segment direction 
		m_directions[segIdx].cross( Vector3.EZ, m_tmpDirToPoint );
		m_tmpDirToPoint.scale( inPos.m_y );
		m_tmpCastPoint.set( m_directions[segIdx] ).scale( t );
		outTransformedPos.set( m_points[segIdx] ).add( m_tmpDirToPoint ).add( m_tmpCastPoint );
	}
	
	/**
	 * Returns a position on the spline at 'offset' distance from the first point. 
	 * 
	 * @param offset
	 * @param outPos
	 */
	public void getPosition( float offset, Vector3 outPos ) 
	{
		m_tmpCastPoint.set( offset, 0, 0 );
		transform( m_tmpCastPoint, outPos );
	}
	
	/**
	 * Call this method whenever you change the positions of spline points,
	 * so that the directions and lengths can be updated as well.
	 */
	public void refresh()
	{
		for ( int i = 0; i < m_points.length - 1; ++i )
		{
			Vector3 start = m_points[ i ];
			Vector3 end = m_points[ i + 1 ];
				
			// calculate a direction vector of the segment
			Vector3 dir = new Vector3( end ).sub( start );
			m_lengths[i] = dir.mag();
			m_directions[i] = dir.normalize();
		}
	}

}
