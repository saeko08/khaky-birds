package com.hypefoundry.engine.util;


/**
 * A vector representation.
 * 
 * @author paksas
 *
 */
public class Vector3 
{
	public float		m_x;
	public float		m_y;
	public float		m_z;
	
	/**
	 * Default constructor.
	 */
	public Vector3()
	{
		m_x = 0;
		m_y = 0;
		m_z = 0;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param x
	 * @param y
	 */
	public Vector3( float x, float y, float z )
	{
		m_x = x;
		m_y = y;
		m_z = z;
	}
}
