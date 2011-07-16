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
	public float 		m_minZ;
	public float 		m_maxX;
	public float 		m_maxY;
	public float 		m_maxZ;
	
	/**
	 * Constructor.
	 * 
	 * @param minX
	 * @param minY
	 * @param minZ
	 * @param maxX
	 * @param maxY
	 * @param maxZ
	 */
	public BoundingBox( float minX, float minY, float minZ, float maxX, float maxY, float maxZ)
	{
		m_minX = minX;
		m_minY = minY;
		m_minZ = minZ;
		m_maxX = maxX;
		m_maxY = maxY;
		m_maxZ = maxZ;
	}
	
	/**
	 * Checks if the box overlaps a point.
	 * 
	 * @param pos
	 * @return
	 */
	final public boolean doesOverlap( Vector3 pos )
	{
		return m_minX <= pos.m_x && m_maxX >= pos.m_x && m_minY <= pos.m_y && m_maxY >= pos.m_y && m_minZ <= pos.m_z && m_maxZ >= pos.m_z;
	}
	
	/**
	 * Checks if the box overlaps a point.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	final public boolean doesOverlap( float x, float y, float z )
	{
		return m_minX <= x && m_maxX >= x && m_minY <= y && m_maxY >= y && m_minZ <= z && m_maxZ >= z;
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
		return !( m_minX > box.m_maxX || m_maxX < box.m_minX || m_minY > box.m_maxY || m_maxY < box.m_minY || m_minZ > box.m_maxZ || m_maxZ < box.m_minZ );
	}
}
