package com.hypefoundry.engine.math;

import android.util.FloatMath;

import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.util.serialization.DataSaver;


/**
 * A vector representation.
 * 
 * @author paksas
 *
 */
public final class Vector3 
{
	public static float TO_RADIANS = (1 / 180.0f) * (float) Math.PI;
	public static float TO_DEGREES = (1 / (float) Math.PI) * 180;
	
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
	 * Constructor.
	 * 
	 * @param rhs
	 */
	public Vector3( Vector3 rhs ) 
	{
		m_x = rhs.m_x;
		m_y = rhs.m_y;
		m_z = rhs.m_z;
	}

	/**
	 * Sets the value of the vector based on a different vector.
	 * 
	 * @param x
	 * @param y
	 * @param y
	 * @return  this vector
	 */
	public Vector3 set( float x, float y, float z ) 
	{
		m_x = x;
		m_y = y;
		m_z = z;
		return this;
	}
	
	/**
	 * Sets the value of the vector based on a different vector.
	 * 
	 * @param rhs
	 * @return  this vector
	 */
	public Vector3 set( Vector3 rhs ) 
	{
		m_x = rhs.m_x;
		m_y = rhs.m_y;
		m_z = rhs.m_z;
		return this;
	}

	/**
	 * Normalizes the vector.
	 */
	public Vector3 normalize() 
	{
		float sqMag = (float) Math.sqrt( m_x*m_x + m_y*m_y + m_z*m_z );
		if ( sqMag <= 1e-6 )
		{
			return this;
		}
		
		float invMag = 1.0f/sqMag;
		m_x *= invMag;
		m_y *= invMag;
		m_z *= invMag;
		return this;
	}
	
	/**
	 * Normalizes the vector in 2D ( x & y components ), and sets its z component to 0.
	 */
	public Vector3 normalize2D() 
	{
		float sqMag = (float) Math.sqrt( m_x*m_x + m_y*m_y );
		if ( sqMag <= 1e-6 )
		{
			return this;
		}
		
		float invMag = 1.0f/sqMag;
		m_x *= invMag;
		m_y *= invMag;
		m_z = 0;
		return this;
	}
	
	/**
	 * Normalizes the vector and places the result in the specified vector.
	 * 
	 * @param outVec
	 */
	public void normalized( Vector3 outVec )
	{
		float sqMag = (float) Math.sqrt( m_x*m_x + m_y*m_y + m_z*m_z );
		float invMag = 0.0f;
		if ( sqMag > 1e-6 )
		{
			invMag = 1.0f/(float) sqMag;
		}
		
		outVec.m_x = m_x * invMag;
		outVec.m_y = m_y * invMag;
		outVec.m_z = m_z * invMag;
	}
	
	/**
	 * Normalizes the vector in 2D ( x & y components ), sets its z component to 0
	 * and places the result in the specified vector.
	 * 
	 * @param outVec
	 */
	public void normalized2D( Vector3 outVec )
	{
		float sqMag = (float) Math.sqrt( m_x*m_x + m_y*m_y );
		float invMag = 0.0f;
		if ( sqMag > 1e-6 )
		{
			invMag = 1.0f/(float) sqMag;
		}
		
		outVec.m_x = m_x * invMag;
		outVec.m_y = m_y * invMag;
		outVec.m_z = 0;
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
	 * @return  this vector
	 */
	public Vector3 scale( float s )
	{
		m_x *= s;
		m_y *= s;
		m_z *= s;
		return this;
	}
	
	/**
	 * Copies the specified vector and scales it uniformally in three directions.
	 * 
	 * @param rhs
	 * @param s
	 * @return  this vector
	 */
	public Vector3 scale( Vector3 rhs, float s )
	{
		m_x = rhs.m_x * s;
		m_y = rhs.m_y * s;
		m_z = rhs.m_z * s;
		return this;
	}
	
	/**
	 * Adds two vectors
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return  this vector
	 */
	public Vector3 add( float x, float y, float z )
	{
		m_x += x;
		m_y += y;
		m_z += z;
		return this;
	}
	
	/**
	 * Adds two vectors
	 * 
	 * @param rhs
	 * @return  this vector
	 */
	public Vector3 add( Vector3 rhs )
	{
		m_x += rhs.m_x;
		m_y += rhs.m_y;
		m_z += rhs.m_z;
		return this;
	}
	
	/**
	 * Subtracts two vectors
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return  this vector
	 */
	public Vector3 sub( float x, float y, float z )
	{
		m_x -= x;
		m_y -= y;
		m_z -= z;
		return this;
	}
	
	/**
	 * Subtracts two vectors
	 * 
	 * @param rhs
	 * @return  this vector
	 */
	public Vector3 sub( Vector3 rhs )
	{
		m_x -= rhs.m_x;
		m_y -= rhs.m_y;
		m_z -= rhs.m_z;
		return this;
	}
	
	/**
	 * Calculates the angle the vector creates with the X axis with respect to the XY plane.
	 * @return
	 */
	public float angleXY() 
	{
		float angle = (float) Math.atan2( m_y, m_x ) * TO_DEGREES;
		if ( angle < 0 )
		{
			angle += 360;
		}
		return angle;
	}
	
	/**
	 * Rotates the vector around the Z axis.
	 * 
	 * @param angle
	 * @return	this vector
	 */
	public Vector3 rotateZ( float angle ) 
	{
		float rad = angle * TO_RADIANS;
		float cos = FloatMath.cos( rad );
		float sin = FloatMath.sin( rad );
		
		float newX = m_x * cos - m_y * sin;
		float newY = m_x * sin + m_y * cos;
		m_x = newX;
		m_y = newY;
		
		return this;
	}
	
	/**
	 * Calculates the distance to the specified vector.
	 * 
	 * @param other
	 * @return
	 */
	public float dist( Vector3 rhs ) 
	{
		float distX = m_x - rhs.m_x;
		float distY = m_y - rhs.m_y;
		float distZ = m_z - rhs.m_z;
		return FloatMath.sqrt( distX * distX + distY * distY + distZ * distZ );
	}
	
	/**
	 * Calculates the distance to the specified vector.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public float dist( float x, float y, float z ) 
	{
		float distX = m_x - x;
		float distY = m_y - y;
		float distZ = m_z - z;
		return FloatMath.sqrt( distX * distX + distY * distY + distZ * distZ );
	}
	
	/**
	 * Calculates the square distance to the specified vector.
	 * 
	 * @param other
	 * @return
	 */
	public float distSq( Vector3 rhs ) 
	{
		float distX = m_x - rhs.m_x;
		float distY = m_y - rhs.m_y;
		float distZ = m_z - rhs.m_z;
		return distX * distX + distY * distY + distZ * distZ;
	}
	
	/**
	 * Calculates the square distance to the specified vector.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public float distSq( float x, float y, float z ) 
	{
		float distX = m_x - x;
		float distY = m_y - y;
		float distZ = m_z - z;
		return distX * distX + distY * distY + distZ * distZ;
	}
	
	/**
	 * Calculates the 2D distance to the specified vector.
	 * 
	 * @param other
	 * @return
	 */
	public float dist2D( Vector3 rhs ) 
	{
		float distX = m_x - rhs.m_x;
		float distY = m_y - rhs.m_y;
		float distZ = m_z - rhs.m_z;
		return FloatMath.sqrt( distX * distX + distY * distY );
	}
	
	/**
	 * Calculates the 2D distance to the specified vector.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public float dist2D( float x, float y ) 
	{
		float distX = m_x - x;
		float distY = m_y - y;
		return FloatMath.sqrt( distX * distX + distY * distY );
	}
	
	/**
	 * Calculates the 2D square distance to the specified vector.
	 * 
	 * @param other
	 * @return
	 */
	public float distSq2D( Vector3 rhs ) 
	{
		float distX = m_x - rhs.m_x;
		float distY = m_y - rhs.m_y;
		return distX * distX + distY * distY;
	}
	
	/**
	 * Calculates the 2D square distance to the specified vector.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public float distSq2D( float x, float y) 
	{
		float distX = m_x - x;
		float distY = m_y - y;
		return distX * distX + distY * distY;
	}
	
	
	/**
	 * Limits vector's length to the specified value without changing its direction.  
	 * 
	 * @param newLen
	 */
	public void clamp( float newLen )
	{
		float newLenSq = newLen * newLen; 
		float lenSq = m_x*m_x + m_y*m_y + m_z*m_z;
		if ( newLenSq < lenSq )
		{
			float invMag = 0;
			if ( lenSq > 1e-6 )
			{
				invMag = (float) Math.sqrt( newLenSq / lenSq );
			}
			m_x *= invMag;
			m_y *= invMag;
			m_z *= invMag;
		}
	}

	/**
	 * Returns an angle between two vectors.
	 * 
	 * @param rhs
	 * @return
	 */
	public float getAngleBetween( Vector3 rhs ) 
	{
		float angle1 = angleXY();
		float angle2 = rhs.angleXY();
		float angle = angle1 - angle2;
		
		if ( angle > 180.0f )
		{
			angle = 360.0f - angle;
		}
		else if ( angle < -180.0f )
		{
			angle = 360.0f + angle;
		}

		return angle;
	}
	
	/**
	 * Calculates a dot product between this and the specified vector.
	 * 
	 * @param rhs
	 * @return
	 */
	public float dot( Vector3 rhs )
	{
		return m_x * rhs.m_x + m_y * rhs.m_y + m_z * rhs.m_z; 
	}
	
	/**
	 * Calculates a 2D dot product between this and the specified vector.
	 * 
	 * @param rhs
	 * @return
	 */
	public float dot2D( Vector3 rhs )
	{
		return m_x * rhs.m_x + m_y * rhs.m_y; 
	}
	
	// ------------------------------------------------------------------------
	// Serialization support
	// ------------------------------------------------------------------------
	/**
	 * Configures the vector based on the config file node's contents.
	 * 
	 * @param id			id of the child node describing this shape
	 * @param parentNode	parent node in which to look for the data
	 */
	public void load( String id, DataLoader loader )
	{
		DataLoader node = loader.getChild( id );
		if ( node == null )
		{
			// parent node doesn't contain the description of this shape
			return;
		}
		
		m_x = node.getFloatValue( "x" );
		m_y = node.getFloatValue( "y" );
		m_z = node.getFloatValue( "z" );
	}
	
	/**
	 * Saves the vector configuration.
	 * 
	 * @param id			id of the child node describing this shape
	 * @param parentNode	parent node to which the configuration should be added
	 */
	public void save( String id, DataSaver saver )
	{
		DataSaver node = saver.addChild( id );

		node.setFloatValue( "x", m_x );
		node.setFloatValue( "y", m_y );
		node.setFloatValue( "z", m_z );
	}
}
