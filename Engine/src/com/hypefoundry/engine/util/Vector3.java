package com.hypefoundry.engine.util;


/**
 * A vector representation.
 * 
 * @author paksas
 *
 */
public final class Vector3 
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

	/**
	 * Normalizes the vector.
	 */
	public void normalize() 
	{
		float invMag = 1.0f/(float) Math.sqrt( m_x*m_x + m_y*m_y + m_z*m_z );
		m_x *= invMag;
		m_y *= invMag;
		m_z *= invMag;
	}
	
	/**
	 * Magnitude ( length ) of the vector.
	 * @return
	 */
	public float mag()
	{
		float mag = (float) Math.sqrt( m_x*m_x + m_y*m_y + m_z*m_z );
		return mag;
	}
	
	/**
	 * Scales the vector uniformally in three directions.
	 * @param s
	 */
	public void scale( float s )
	{
		m_x *= s;
		m_y *= s;
		m_z *= s;
	}
	
	/**
	 * Copies the specified vector and scales it uniformally in three directions.
	 * 
	 * @param rhs
	 * @param s
	 */
	public void scale( Vector3 rhs, float s )
	{
		m_x = rhs.m_x * s;
		m_y = rhs.m_y * s;
		m_z = rhs.m_z * s;
	}
	
	/**
	 * Adds two vectors
	 * 
	 * @param rhs
	 */
	public void add( Vector3 rhs )
	{
		m_x += rhs.m_x;
		m_y += rhs.m_y;
		m_z += rhs.m_z;
	}
	
	/**
	 * Subtracts two vectors
	 * 
	 * @param rhs
	 */
	public void sub( Vector3 rhs )
	{
		m_x -= rhs.m_x;
		m_y -= rhs.m_y;
		m_z -= rhs.m_z;
	}
}
