/**
 * 
 */
package com.hypefoundry.engine.math;

import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.util.serialization.DataSaver;

/**
 * Bounding sphere.
 * @author paksas
 *
 */
final public class BoundingSphere implements BoundingShape
{
	public final 	Vector3 	m_center = new Vector3();
	public 			float 		m_radius;
	
	private final	Vector3 	m_tmpVec = new Vector3();
	
	/**
	 * Default constructor.
	 */
	public BoundingSphere() 
	{
		m_radius = 0;
	}
	
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
	
	/**
	 * Sets the sphere's parameters.
	 * 
	 * @param origin
	 * @param radius
	 */
	public void set( Vector3 origin, float radius )
	{
		m_center.set( origin );
		m_radius = radius;
	}
	
	@Override
	public void getBoundingBox( BoundingBox box )
	{
		box.set( m_center.m_x - m_radius, m_center.m_y - m_radius, m_center.m_z - m_radius, m_center.m_x + m_radius, m_center.m_y + m_radius, m_center.m_z + m_radius );
	}
	
	@Override
	public BoundingShape extrude( Vector3 origin, Vector3 direction, BoundingShape outExtrudedShape )
	{
		// create the shape if it hasn't been created yet
		if ( outExtrudedShape == null )
		{
			outExtrudedShape = new BoundingSphere();
		}
		
		// get the type-specific shape
		if ( ( outExtrudedShape instanceof BoundingSphere ) == false )
		{
			throw new RuntimeException( "Invalid extruded shape type for the BoundingSphere" );
		}
		BoundingSphere sphere = (BoundingSphere)outExtrudedShape;
		
		// calculate new center of the extruded sphere
		m_tmpVec.set( origin ).add( direction );
		
		// initialize the shape
		sphere.set( m_tmpVec, m_radius );
		
		return outExtrudedShape;
	}
	
	// ------------------------------------------------------------------------
	// Overlap tests
	// ------------------------------------------------------------------------
	
	@Override 
	public boolean doesOverlap( BoundingShape shape, Vector3 outIntersectPos )
	{
		return shape.doesOverlap( this, outIntersectPos );
	}
	
	@Override
	public boolean doesOverlap( final BoundingSphere sphere, Vector3 outIntersectPos )
	{
		// TODO: outIntersectPos
		float distance = m_center.distSq( sphere.m_center );
		float radiusSum = m_radius + sphere.m_radius;
		return distance <= radiusSum * radiusSum;
	}

	@Override
	public boolean doesOverlap( final BoundingBox box, Vector3 outIntersectPos ) 
	{
		// TODO: outIntersectPos
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
	public boolean doesOverlap( final Vector3 point, Vector3 outIntersectPos ) 
	{
		float distance = m_center.distSq( point );
		if ( distance <= m_radius * m_radius )
		{
			outIntersectPos.set( point );
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override 
	public boolean doesOverlap( final Ray ray, Vector3 outIntersectPos )
	{
		m_tmpVec.set( m_center ).sub( ray.m_origin );
		float t = ray.getDirection().dot( m_tmpVec );
		if ( t < 0 )
		{
			return false;
		}
		

		float distToCenterSq = m_center.distSq( ray.m_origin );
		float distToCastCenterSq = distToCenterSq - t*t;
		if ( m_radius*m_radius < distToCastCenterSq )
		{
			return false;
		}
		
		float distCastCenterToIntersectionSq = m_radius*m_radius - distToCastCenterSq;
		float td = t - (float)Math.sqrt( distCastCenterToIntersectionSq );
		if ( td < 0 || td > ray.getLength() )
		{
			return false;
		}
		
		if ( outIntersectPos != null )
		{
			// calculate the intersection pos
			outIntersectPos.set( ray.getDirection() ).scale( td ).add( ray.m_origin );
		}
		
		return true;
	}
	
	// ------------------------------------------------------------------------
	// Serialization support
	// ------------------------------------------------------------------------
	
	@Override
	public void load( String id, DataLoader loader )
	{
		DataLoader node = loader.getChild( id );
		if ( node == null )
		{
			// parent node doesn't contain the description of this shape
			return;
		}
		
		m_center.load( "center", node );
		m_radius = node.getFloatValue( "radius" );
	}
	
	@Override
	public void save( String id, DataSaver saver )
	{
		DataSaver node = saver.addChild( id );

		m_center.save( "center", node );
		node.setFloatValue( "radius", m_radius );
	}
}
