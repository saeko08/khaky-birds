package com.hypefoundry.engine.math;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.util.serialization.DataSaver;


/**
 * A bounding box.
 * 
 * @author paksas
 *
 */
final public class BoundingBox implements BoundingShape
{
	public float 			m_minX; 
	public float 			m_minY;
	public float 			m_minZ;
	public float 			m_maxX;
	public float 			m_maxY;
	public float 			m_maxZ;
	private final Vector3 	m_massCenter = new Vector3();
	
	private final Vector3	m_tmpVec = new Vector3();
	
	
	/**
	 * Default constructor.
	 * 
	 * @param m_shape
	 */
	public BoundingBox() 
	{
		m_minX = 0;
		m_minY = 0;
		m_minZ = 0;
		m_maxX = 0;
		m_maxY = 0;
		m_maxZ = 0;
	}
	
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
		
		calculateMassCenter();
	}
	
	/**
	 * Creates a bounding box based on a bounding sphere.
	 * 
	 * @param sphere
	 */
	public void set( BoundingSphere sphere )
	{
		float halfSize = sphere.m_radius;
		m_minX = sphere.m_center.m_x - halfSize;
		m_minY = sphere.m_center.m_y - halfSize;
		m_minZ = sphere.m_center.m_z - halfSize;
		m_maxX = sphere.m_center.m_x + halfSize;
		m_maxY = sphere.m_center.m_y + halfSize;
		m_maxZ = sphere.m_center.m_z + halfSize;
		
		calculateMassCenter();
	}
	
	/**
	 *Creates a bounding box based on the specified bounding coordinates
	 * 
	 * @param sphere
	 */
	public void set( float minX, float minY, float minZ, float maxX, float maxY, float maxZ )
	{
		m_minX = minX;
		m_minY = minY;
		m_minZ = minZ;
		m_maxX = maxX;
		m_maxY = maxY;
		m_maxZ = maxZ;
		
		calculateMassCenter();
	}
	
	/**
	 * Creates a bounding box based on a different bounding box
	 * 
	 * @param sphere
	 */
	public void set( BoundingBox rhs )
	{
		m_minX = rhs.m_minX;
		m_minY = rhs.m_minY;
		m_minZ = rhs.m_minZ;
		m_maxX = rhs.m_maxX;
		m_maxY = rhs.m_maxY;
		m_maxZ = rhs.m_maxZ;
		
		calculateMassCenter();
	}
	
	/**
	 * A Helper method for calculating the mass center.
	 */
	private void calculateMassCenter()
	{
		m_massCenter.set( m_maxX, m_maxY, m_maxZ ).sub( m_minX, m_minY, m_minZ ).scale( 0.5f );
	}
	
	@Override
	public float getWidth()
	{
		return m_maxX - m_minX;
	}
	
	@Override
	public float getHeight()
	{
		return m_maxY - m_minY;
	}
	
	@Override
	public Vector3 getMassCenter() 
	{
		return m_massCenter;
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
	
	@Override
	final public boolean doesOverlap( final BoundingBox box )
	{
		return !( m_minX > box.m_maxX || m_maxX < box.m_minX || m_minY > box.m_maxY || m_maxY < box.m_minY || m_minZ > box.m_maxZ || m_maxZ < box.m_minZ );
	}

	@Override
	public boolean doesOverlap( final BoundingSphere sphere ) 
	{
		// the code's implemented in the BoundingSphere class - so let's use it
		return sphere.doesOverlap( this );
	}
	
	@Override
	final public boolean doesOverlap( final Vector3 pos )
	{
		return m_minX <= pos.m_x && m_maxX >= pos.m_x && m_minY <= pos.m_y && m_maxY >= pos.m_y && m_minZ <= pos.m_z && m_maxZ >= pos.m_z;
	}
	
	@Override 
	public boolean doesOverlap( final Ray ray, Vector3 outIntersectPos )
	{
		m_tmpVec.set( ray.getDirection() ).scale( ray.getLength() );
		
		// Check for point inside box, trivial reject, and determine parametric
		// distance to each front face
		boolean inside = true;
		
		float xt;
		if ( ray.m_origin.m_x < m_minX ) 
		{
			xt = m_minX - ray.m_origin.m_x;
			if ( xt > m_tmpVec.m_x ) 
			{
				return false;
			}
			
			xt /= m_tmpVec.m_x;
			inside = false;
		} 
		else if ( ray.m_origin.m_x > m_maxX ) 
		{
			xt = m_maxX - ray.m_origin.m_x;
			if ( xt < m_tmpVec.m_x ) 
			{
				return false;
			}
			
			xt /= m_tmpVec.m_x;
			inside = false;
		} 
		else 
		{
			xt = -1.0f;
		}
		
		
		float yt;
		if ( ray.m_origin.m_y < m_minY ) 
		{
			yt = m_minY - ray.m_origin.m_y;
			if ( yt > m_tmpVec.m_y )
			{
				return false;
			}
			
			yt /= m_tmpVec.m_y;
			inside = false;
		} 
		else if ( ray.m_origin.m_y > m_maxY ) 
		{
			yt = m_maxY - ray.m_origin.m_y;
			if ( yt < m_tmpVec.m_y )
			{ 
				return false;
			}
			
			yt /= m_tmpVec.m_y;
			inside = false;
		} 
		else 
		{
			yt = -1.0f;
		}
		
		float zt;
		if ( ray.m_origin.m_z < m_minZ ) 
		{
			zt = m_minZ - ray.m_origin.m_z;
			if ( zt > m_tmpVec.m_z )
			{
				return false;
			}
			zt /= m_tmpVec.m_z;
			inside = false;
		} 
		else if ( ray.m_origin.m_z > m_maxZ ) 
		{
			zt = m_maxZ - ray.m_origin.m_z;
			if ( zt < m_tmpVec.m_z )
			{
				return false;
			}
			
			zt /= m_tmpVec.m_z;
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
		
		if ( t < 0 || t > ray.getLength() )
		{
			return false;
		}
		
		switch ( which ) 
		{
			case 0: // intersect with yz plane
			{
				float y = ray.m_origin.m_y + m_tmpVec.m_y * t;
				if ( y < m_minY || y > m_maxY ) 
				{ 
					return false;
				}
				
				float z = ray.m_origin.m_z + m_tmpVec.m_z * t;
				if ( z < m_minZ || z > m_maxZ)
				{ 
					return false;
				}
				break;
			} 
			
			case 1: // intersect with xz plane
			{
				float x = ray.m_origin.m_x + m_tmpVec.m_x * t;
				if ( x < m_minX || x > m_maxX )
				{
					return false;
				}
				
				float z = ray.m_origin.m_z + m_tmpVec.m_z * t;
				if ( z < m_minZ || z > m_maxZ)
				{ 
					return false;
				}
				break;
			} 
			
			case 2: // intersect with xy plane
			{
				float x = ray.m_origin.m_x + m_tmpVec.m_x * t;
				if ( x < m_minX || x > m_maxX )
				{
					return false;
				}
				
				float y = ray.m_origin.m_y + m_tmpVec.m_y * t;
				if ( y < m_minY || y > m_maxY ) 
				{ 
					return false;
				}
				break;
			} 
		}
		
		if ( outIntersectPos != null )
		{
			// calculate the intersection pos
			outIntersectPos.set( ray.getDirection() ).scale( t ).add( ray.m_origin );
		}
		
		return true;
	}
	
	@Override
	final public boolean doesOverlap2D( final BoundingBox box )
	{
		return !( m_minX > box.m_maxX || m_maxX < box.m_minX || m_minY > box.m_maxY || m_maxY < box.m_minY );
	}

	@Override
	public boolean doesOverlap2D( final BoundingSphere sphere ) 
	{
		// the code's implemented in the BoundingSphere class - so let's use it
		return sphere.doesOverlap2D( this );
	}
	
	@Override
	final public boolean doesOverlap2D( final Vector3 pos )
	{
		return m_minX <= pos.m_x && m_maxX >= pos.m_x && m_minY <= pos.m_y && m_maxY >= pos.m_y;
	}
	
	@Override 
	public boolean doesOverlap2D( final Ray ray, Vector3 outIntersectPos )
	{
		m_tmpVec.set( ray.getDirection() ).scale( ray.getLength() );
		
		// Check for point inside box, trivial reject, and determine parametric
		// distance to each front face
		boolean inside = true;
		
		float xt;
		if ( ray.m_origin.m_x < m_minX ) 
		{
			xt = m_minX - ray.m_origin.m_x;
			if ( xt > m_tmpVec.m_x ) 
			{
				return false;
			}
			
			xt /= m_tmpVec.m_x;
			inside = false;
		} 
		else if ( ray.m_origin.m_x > m_maxX ) 
		{
			xt = m_maxX - ray.m_origin.m_x;
			if ( xt < m_tmpVec.m_x ) 
			{
				return false;
			}
			
			xt /= m_tmpVec.m_x;
			inside = false;
		} 
		else 
		{
			xt = -1.0f;
		}
		
		
		float yt;
		if ( ray.m_origin.m_y < m_minY ) 
		{
			yt = m_minY - ray.m_origin.m_y;
			if ( yt > m_tmpVec.m_y )
			{
				return false;
			}
			
			yt /= m_tmpVec.m_y;
			inside = false;
		} 
		else if ( ray.m_origin.m_y > m_maxY ) 
		{
			yt = m_maxY - ray.m_origin.m_y;
			if ( yt < m_tmpVec.m_y )
			{ 
				return false;
			}
			
			yt /= m_tmpVec.m_y;
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
		int which = 0;
		float t = xt;
		if ( yt > t ) 
		{
			which = 1;
			t = yt;
		}
		
		if ( t < 0 || t > ray.getLength() )
		{
			return false;
		}
		
		switch ( which ) 
		{
			case 0: // intersect with yz plane
			{
				float y = ray.m_origin.m_y + m_tmpVec.m_y * t;
				if ( y < m_minY || y > m_maxY ) 
				{ 
					return false;
				}

				break;
			} 
			
			case 1: // intersect with xz plane
			{
				float x = ray.m_origin.m_x + m_tmpVec.m_x * t;
				if ( x < m_minX || x > m_maxX )
				{
					return false;
				}
				
				break;
			} 
		}
		
		if ( outIntersectPos != null )
		{
			// calculate the intersection pos
			outIntersectPos.set( ray.getDirection() ).scale( t ).add( ray.m_origin );
			outIntersectPos.m_z = m_massCenter.m_z;
		}
		
		return true;
	}
	
	@Override
	public void load( String id, DataLoader loader )
	{
		DataLoader node = loader.getChild( id );
		if ( node == null )
		{
			// parent node doesn't contain the description of this shape
			return;
		}
		
		m_minX = node.getFloatValue( "minX" );
		m_minY = node.getFloatValue( "minY" );
		m_minZ = node.getFloatValue( "minZ" );
		m_maxX = node.getFloatValue( "maxX" );
		m_maxY = node.getFloatValue( "maxY" );
		m_maxZ = node.getFloatValue( "maxZ" );
		
		calculateMassCenter();
	}
	
	@Override
	public void save( String id, DataSaver saver )
	{
		DataSaver node = saver.addChild( id );

		node.setFloatValue( "minX", m_minX );
		node.setFloatValue( "minY", m_minY );
		node.setFloatValue( "minZ", m_minZ );
		node.setFloatValue( "maxX", m_maxX );
		node.setFloatValue( "maxY", m_maxY );
		node.setFloatValue( "maxZ", m_maxZ );
	}
}
