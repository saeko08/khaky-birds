package com.hypefoundry.engine.util;


/**
 * A vector representation.
 * 
 * @author paksas
 *
 */
public class Vector2 
{
	public float		m_x;
	public float		m_y;
	
	/**
	 * Default constructor.
	 */
	public Vector2()
	{
		m_x = 0;
		m_y = 0;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param x
	 * @param y
	 */
	public Vector2( float x, float y )
	{
		m_x = x;
		m_y = y;
	}
}
