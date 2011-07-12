package com.hypefoundry.engine.util;

/**
 * A bounding box.
 * 
 * @author paksas
 *
 */
final public class BoundingBox 
{
	public float 		m_minX; 
	public float 		m_minY;
	public float 		m_maxX;
	public float 		m_maxY;
	
	
	/**
	 * Constructor.
	 * 
	 * @param minX
	 * @param minY
	 * @param maxX
	 * @param maxY
	 */
	public BoundingBox( float minX, float minY, float maxX, float maxY )
	{
		m_minX = minX;
		m_minY = minY;
		m_maxX = maxX;
		m_maxY = maxY;
	}
	
	/**
	 * Checks if the box overlaps a point.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	final public boolean doesOverlap( float x, float y )
	{
		return m_minX <= x && m_maxX >= x && m_minY <= y && m_maxY >= y;
	}
	
	/**
	 * Checks if the box overlaps another box.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	final public boolean doesOverlap( final BoundingBox box )
	{
		return !( m_minX > box.m_maxX || m_maxX < box.m_minX || m_minY > box.m_maxY || m_maxY < box.m_minY );
	}
}
