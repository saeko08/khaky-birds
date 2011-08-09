/**
 * 
 */
package com.hypefoundry.engine.math;

/**
 * Bounding sphere.
 * @author paksas
 *
 */
final public class BoundingSphere implements BoundingShape
{
	public final 	Vector3 	m_center = new Vector3();
	public 			float 		m_radius;
	
	/**
	 * Constructor.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param radius
	 */
	public BoundingSphere( float x, float y, float z, float radius ) 
	{
		m_center.set( x, y, z );
		m_radius = radius;
	}
	
	@Override
	public float getWidth()
	{
		return m_radius;
	}
	
	@Override
	public float getHeight()
	{
		return m_radius;
	}
	
	@Override
	public Vector3 getMassCenter()
	{
		return m_center;
	}
	
	@Override
	public boolean doesOverlap( final BoundingSphere sphere )
	{
		float distance = m_center.distSq( sphere.m_center );
		float radiusSum = m_radius + sphere.m_radius;
		return distance <= radiusSum * radiusSum;
	}

	@Override
	public boolean doesOverlap( final BoundingBox box ) 
	{
		float closestX = m_center.m_x;
		float closestY = m_center.m_y;
		float closestZ = m_center.m_z;

		// find the X coordinate of the closest point on the box
		if ( m_center.m_x < box.m_minX ) 
		{
			closestX = box.m_minX;
		}
		else if ( m_center.m_x > box.m_maxX ) 
		{
			closestX = box.m_maxX;
		}
		
		// find the Y coordinate of the closest point on the box
		if ( m_center.m_y < box.m_minY ) 
		{
			closestY = box.m_minY;
		}
		else if ( m_center.m_y > box.m_maxY ) 
		{
			closestY = box.m_maxY;
		}
		
		// find the Z coordinate of the closest point on the box
		if ( m_center.m_z < box.m_minZ ) 
		{
			closestZ = box.m_minZ;
		}
		else if ( m_center.m_z > box.m_maxZ ) 
		{
			closestZ = box.m_maxZ;
		}
		
		// calculate the distance to the point and see if it's in the sphere's radius
		return m_center.distSq( closestX, closestY, closestZ ) < m_radius * m_radius;
	}

	@Override
	public boolean doesOverlap( final Vector3 point ) 
	{
		float distance = m_center.distSq( point );
		return distance <= m_radius * m_radius;
	}
	
	@Override
	public boolean doesOverlap2D( final BoundingSphere sphere )
	{
		float distance = m_center.distSq( sphere.m_center );
		float radiusSum = m_radius + sphere.m_radius;
		return distance <= radiusSum * radiusSum;
	}

	@Override
	public boolean doesOverlap2D( final BoundingBox box ) 
	{
		float closestX = m_center.m_x;
		float closestY = m_center.m_y;

		// find the X coordinate of the closest point on the box
		if ( m_center.m_x < box.m_minX ) 
		{
			closestX = box.m_minX;
		}
		else if ( m_center.m_x > box.m_maxX ) 
		{
			closestX = box.m_maxX;
		}
		
		// find the Y coordinate of the closest point on the box
		if ( m_center.m_y < box.m_minY ) 
		{
			closestY = box.m_minY;
		}
		else if ( m_center.m_y > box.m_maxY ) 
		{
			closestY = box.m_maxY;
		}
		
		// calculate the distance to the point and see if it's in the sphere's radius
		return m_center.distSq( closestX, closestY, 0.0f ) < m_radius * m_radius;
	}

	@Override
	public boolean doesOverlap2D( final Vector3 point ) 
	{
		float distance = m_center.distSq( point );
		return distance <= m_radius * m_radius;
	}
}
