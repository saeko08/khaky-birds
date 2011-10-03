/**
 * 
 */
package com.hypefoundry.engine.math;

import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.util.serialization.DataSaver;

/**
 * 
 * A mathematical ray.
 * 
 * @author Paksas
 */
public final class Ray implements BoundingShape 
{
	public Vector3		m_origin;
	private Vector3		m_direction;
	private Vector3		m_fullDirection;
	private float 		m_length;
	private boolean		m_2dTesting 			= true;
	
	
	// ------------------------------------------------------------------------
	// runtime data
	// ------------------------------------------------------------------------
	private Vector3 m_tmpDirCross 				= new Vector3();
	private Vector3 m_tmpOriginsDir 			= new Vector3();
	private Vector3 m_tmpOriginsDirToDirCross 	= new Vector3();
	
	/**
	 * Default constructor.
	 */
	public Ray()
	{
		m_origin		= new Vector3();
		m_direction		= new Vector3();
		m_fullDirection = new Vector3();
		m_length		= 0;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param ox	origin x
	 * @param oy	origin y
	 * @param oz	origin z
	 * @param dx	direction x
	 * @param dy	direction y
	 * @param dz	direction z
	 */
	public Ray( float ox, float oy, float oz, float dx, float dy, float dz )
	{
		m_origin		= new Vector3( ox, oy, oz );
		m_fullDirection = new Vector3( dx, dy, dz );
		m_direction		= new Vector3( dx, dy, dz );
		m_length 		= m_direction.mag();
		m_direction.normalize();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param origin
	 * @param direction
	 */
	public Ray( Vector3 origin, Vector3 direction )
	{
		m_origin = origin;
		m_fullDirection = direction;
		m_direction = new Vector3( direction );
		m_length = m_direction.mag();
		m_direction.normalize();
	}
	
	/**
	 * Makes the ray perform collision tests in 3D.
	 * 
	 * @return
	 */
	public Ray set3DTesting()
	{
		m_2dTesting = false;
		return this;
	}
	
	/**
	 * Sets a new direction of the ray.
	 * 
	 * @param direction
	 */
	public void setDirection( final Vector3 direction )
	{
		m_fullDirection.set( direction );
		m_direction.set( direction );
		m_length = m_direction.mag();
		m_direction.normalize();
	}
	
	/**
	 * Sets a new direction of the ray.
	 * 
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void setDirection( float dx, float dy, float dz )
	{
		m_fullDirection.set( dx, dy, dz );
		m_direction.set( dx, dy, dz );
		m_length = m_direction.mag();
		m_direction.normalize();
	}
	
	/**
	 * Returns the direction of the ray.
	 *  
	 * @return
	 */
	public Vector3 getDirection()
	{
		return m_direction;
	}
	
	/**
	 * Returns the length of the ray.
	 * 
	 * @return
	 */
	public float getLength() 
	{
		return m_length;
	}
	

	@Override
	public void getBoundingBox( BoundingBox box ) 
	{
		float x1 = m_origin.m_x;
		float y1 = m_origin.m_y;
		float z1 = m_origin.m_z;
		float x2 = m_origin.m_x + m_direction.m_x * m_length;
		float y2 = m_origin.m_y + m_direction.m_y * m_length;
		float z2 = m_origin.m_z + m_direction.m_z * m_length;
		
		box.setUnsorted( x1, y1, z1, x2, y2, z2 );
	}

	@Override
	public BoundingShape extrude( Vector3 origin, Vector3 direction, BoundingShape outExtrudedShape ) 
	{
		// create the shape if it hasn't been created yet
		if ( outExtrudedShape == null )
		{
			outExtrudedShape = new Ray();
		}
		
		// get the type-specific shape
		if ( ( outExtrudedShape instanceof Ray ) == false )
		{
			throw new RuntimeException( "Invalid extruded shape type for a Ray" );
		}
		Ray ray = (Ray)outExtrudedShape;
		
		// initialize the shape
		ray.m_origin.set( origin );
		ray.setDirection( direction );
		ray.m_2dTesting = m_2dTesting;
		
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
	public boolean doesOverlap( BoundingSphere sphere, Vector3 outIntersectPos ) 
	{
		return sphere.doesOverlap( this, outIntersectPos );
	}

	@Override
	public boolean doesOverlap( BoundingBox box, Vector3 outIntersectPos ) 
	{
		if ( m_2dTesting )
		{
			return testIntersection2D( box, outIntersectPos );
		}
		else
		{
			return testIntersection3D( box, outIntersectPos );
		}
	}

	@Override
	public boolean doesOverlap( Vector3 point, Vector3 outIntersectPos ) 
	{
		if ( m_2dTesting )
		{
			return testIntersection2D( point, outIntersectPos );
		}
		else
		{
			return testIntersection3D( point, outIntersectPos );
		}
	}
	
	@Override
	public boolean doesOverlap( Ray ray, Vector3 outIntersectPos )
	{		
		m_tmpOriginsDir.set( ray.m_origin ).sub( m_origin );
		
		m_fullDirection.cross( ray.m_fullDirection, m_tmpDirCross );
		float dirCrossLenSq = m_tmpDirCross.magSq();
		if ( dirCrossLenSq == 0 )
		{
			return false;
		}
		
		// calculate t1
		m_tmpOriginsDir.cross( ray.m_fullDirection, m_tmpOriginsDirToDirCross );
		float t1 = m_tmpOriginsDirToDirCross.dot( m_tmpDirCross ) / dirCrossLenSq;
		
		// calculate t2
		m_tmpOriginsDir.cross( m_fullDirection, m_tmpOriginsDirToDirCross );
		float t2 = m_tmpOriginsDirToDirCross.dot( m_tmpDirCross ) / dirCrossLenSq;
		
		if ( t1 < 0 || t1 > m_length || t2 < 0 || t2 > ray.m_length )
		{
			// no intersection could have possibly occured
			return false;
		}
		
		// calculate the intersection points along the two rays and see if they are close
		outIntersectPos.set( m_direction ).scale( t1 ).add( m_origin );
		m_tmpOriginsDir.set( ray.m_direction ).scale( t2 ).add( ray.m_origin );
		float dist = outIntersectPos.distSq( m_tmpOriginsDir );
		return ( dist < 1e-3 );
	}
	
	// ------------------------------------------------------------------------
	// Overlap test helpers
	// ------------------------------------------------------------------------
	private boolean testIntersection3D( Vector3 point, Vector3 outIntersectPos ) 
	{
		m_tmpOriginsDir.set( point ).sub( m_origin );
		
		float distToPtSq = m_tmpOriginsDir.magSq();
		float d = m_direction.dot( m_tmpOriginsDir );
		if ( d < 0 || distToPtSq > m_length*m_length )
		{
			return false;
		}
		
		d *= d;
		return Math.abs( d - distToPtSq ) < 1e-3;
	}
	
	private boolean testIntersection2D( Vector3 point, Vector3 outIntersectPos ) 
	{
		m_tmpOriginsDir.set( point ).sub( m_origin );
		m_tmpOriginsDir.m_z = 0;
		
		float distToPtSq = m_tmpOriginsDir.magSq2D();
		float d = m_direction.dot( m_tmpOriginsDir );
		if ( d < 0 || distToPtSq > m_length*m_length )
		{
			return false;
		}
		
		d *= d;
		return Math.abs( d - distToPtSq ) < 1e-3;
	}
	
	private boolean testIntersection3D( BoundingBox box, Vector3 outIntersectPos ) 
	{
		m_tmpOriginsDir.set( m_direction ).scale( m_length );
		
		// Check for point inside box, trivial reject, and determine parametric
		// distance to each front face
		boolean inside = true;
		
		float xt;
		if ( m_origin.m_x < box.m_minX ) 
		{
			xt = box.m_minX - m_origin.m_x;
			if ( xt > m_tmpOriginsDir.m_x ) 
			{
				return false;
			}
			
			xt /= m_tmpOriginsDir.m_x;
			inside = false;
		} 
		else if ( m_origin.m_x > box.m_maxX ) 
		{
			xt = box.m_maxX - m_origin.m_x;
			if ( xt < m_tmpOriginsDir.m_x ) 
			{
				return false;
			}
			
			xt /= m_tmpOriginsDir.m_x;
			inside = false;
		} 
		else 
		{
			xt = -1.0f;
		}
		
		
		float yt;
		if ( m_origin.m_y < box.m_minY ) 
		{
			yt = box.m_minY - m_origin.m_y;
			if ( yt > m_tmpOriginsDir.m_y )
			{
				return false;
			}
			
			yt /= m_tmpOriginsDir.m_y;
			inside = false;
		} 
		else if ( m_origin.m_y > box.m_maxY ) 
		{
			yt = box.m_maxY - m_origin.m_y;
			if ( yt < m_tmpOriginsDir.m_y )
			{ 
				return false;
			}
			
			yt /= m_tmpOriginsDir.m_y;
			inside = false;
		} 
		else 
		{
			yt = -1.0f;
		}
		
		float zt;
		if ( m_origin.m_z < box.m_minZ ) 
		{
			zt = box.m_minZ - m_origin.m_z;
			if ( zt > m_tmpOriginsDir.m_z )
			{
				return false;
			}
			zt /= m_tmpOriginsDir.m_z;
			inside = false;
		} 
		else if ( m_origin.m_z > box.m_maxZ ) 
		{
			zt = box.m_maxZ - m_origin.m_z;
			if ( zt < m_tmpOriginsDir.m_z )
			{
				return false;
			}
			
			zt /= m_tmpOriginsDir.m_z;
			inside = false;
		} 
		else 
		{
			zt = -1.0f;
		}
		
		// Inside box?
		if ( inside ) 
		{
			return true;
		}
		
		// Select farthest plane - this is
		// the plane of intersection.
		int which = 0;
		float t = xt;
		if ( yt > t ) 
		{
			which = 1;
			t = yt;
		}
		
		if ( zt > t ) 
		{
			which = 2;
			t = zt;
		}
		
		if ( t < 0 || t > m_length )
		{
			return false;
		}
		
		switch ( which ) 
		{
			case 0: // intersect with yz plane
			{
				float y = m_origin.m_y + m_tmpOriginsDir.m_y * t;
				if ( y < box.m_minY || y > box.m_maxY ) 
				{ 
					return false;
				}
				
				float z = m_origin.m_z + m_tmpOriginsDir.m_z * t;
				if ( z < box.m_minZ || z > box.m_maxZ)
				{ 
					return false;
				}
				break;
			} 
			
			case 1: // intersect with xz plane
			{
				float x = m_origin.m_x + m_tmpOriginsDir.m_x * t;
				if ( x < box.m_minX || x > box.m_maxX )
				{
					return false;
				}
				
				float z = m_origin.m_z + m_tmpOriginsDir.m_z * t;
				if ( z < box.m_minZ || z > box.m_maxZ )
				{ 
					return false;
				}
				break;
			} 
			
			case 2: // intersect with xy plane
			{
				float x = m_origin.m_x + m_tmpOriginsDir.m_x * t;
				if ( x < box.m_minX || x > box.m_maxX )
				{
					return false;
				}
				
				float y = m_origin.m_y + m_tmpOriginsDir.m_y * t;
				if ( y < box.m_minY || y > box.m_maxY ) 
				{ 
					return false;
				}
				break;
			} 
		}
		
		if ( outIntersectPos != null )
		{
			// calculate the intersection pos
			outIntersectPos.set( m_direction ).scale( t ).add( m_origin );
		}
		
		return true;
	}
	
	private boolean testIntersection2D( BoundingBox box, Vector3 outIntersectPos ) 
	{
		m_tmpOriginsDir.set( m_direction ).scale( m_length );
		
		// Check for point inside box, trivial reject, and determine parametric
		// distance to each front face
		boolean inside = true;
		
		float xt;
		if ( m_origin.m_x < box.m_minX ) 
		{
			xt = box.m_minX - m_origin.m_x;
			if ( xt > m_tmpOriginsDir.m_x ) 
			{
				return false;
			}
			
			xt /= m_tmpOriginsDir.m_x;
			inside = false;
		} 
		else if ( m_origin.m_x > box.m_maxX ) 
		{
			xt = box.m_maxX - m_origin.m_x;
			if ( xt < m_tmpOriginsDir.m_x ) 
			{
				return false;
			}
			
			xt /= m_tmpOriginsDir.m_x;
			inside = false;
		} 
		else 
		{
			xt = -1.0f;
		}
		
		
		float yt;
		if ( m_origin.m_y < box.m_minY ) 
		{
			yt = box.m_minY - m_origin.m_y;
			if ( yt > m_tmpOriginsDir.m_y )
			{
				return false;
			}
			
			yt /= m_tmpOriginsDir.m_y;
			inside = false;
		} 
		else if ( m_origin.m_y > box.m_maxY ) 
		{
			yt = box.m_maxY - m_origin.m_y;
			if ( yt < m_tmpOriginsDir.m_y )
			{ 
				return false;
			}
			
			yt /= m_tmpOriginsDir.m_y;
			inside = false;
		} 
		else 
		{
			yt = -1.0f;
		}
		
		// Inside box?
		if ( inside ) 
		{
			return true;
		}
		
		// Select farthest plane - this is
		// the plane of intersection.
		float t = xt;
		if ( yt > t ) 
		{
			t = yt;
		}
		
		if ( t < 0 || t > m_length )
		{
			return false;
		}
		

		float x = m_origin.m_x + m_tmpOriginsDir.m_x * t;
		if ( x < box.m_minX || x > box.m_maxX )
		{
			return false;
		}
		
		float y = m_origin.m_y + m_tmpOriginsDir.m_y * t;
		if ( y < box.m_minY || y > box.m_maxY ) 
		{ 
			return false;
		}
		
		if ( outIntersectPos != null )
		{
			// calculate the intersection pos
			outIntersectPos.set( m_direction ).scale( t ).add( m_origin );
		}
		
		return true;
	}
	
	// ------------------------------------------------------------------------
	// Serialization support
	// ------------------------------------------------------------------------

	@Override
	public void load( String id, DataLoader laoder )
{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save( String id, DataSaver saver )
	{
		// TODO Auto-generated method stub
		
	}
}
