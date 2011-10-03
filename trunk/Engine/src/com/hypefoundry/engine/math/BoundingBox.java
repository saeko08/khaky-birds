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
	}
	
	/**
	 * Constructor.
	 * 
	 * @param xSize
	 * @param ySize
	 * @param zSize
	 */
	public BoundingBox( float xSize, float ySize, float zSize )
	{
		m_minX = -xSize * 0.5f;
		m_minY = -ySize * 0.5f;
		m_minZ = -zSize * 0.5f;
		m_maxX =  xSize * 0.5f;
		m_maxY =  ySize * 0.5f;
		m_maxZ =  zSize * 0.5f;
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
	}
	
	/**
	 * Creates a bounding box based on the specified bounding coordinates.
	 * 
	 * @param minX
	 * @param minY
	 * @param minZ
	 * @param maxX
	 * @param maxY
	 * @param maxZ
	 */
	public void set( float minX, float minY, float minZ, float maxX, float maxY, float maxZ )
	{
		m_minX = minX;
		m_minY = minY;
		m_minZ = minZ;
		m_maxX = maxX;
		m_maxY = maxY;
		m_maxZ = maxZ;
	}
	
	/**
	 * Creates a bounding box based on a different bounding box.
	 * 
	 * @param rhs
	 */
	public void set( BoundingBox rhs )
	{
		m_minX = rhs.m_minX;
		m_minY = rhs.m_minY;
		m_minZ = rhs.m_minZ;
		m_maxX = rhs.m_maxX;
		m_maxY = rhs.m_maxY;
		m_maxZ = rhs.m_maxZ;
	}
	
	/**
	 * Creates a bounding box based on the specified coordinates that come in an unsorted order.
	 * 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z1
	 */
	public void setUnsorted( float x1, float y1, float z1, float x2, float y2, float z2 )
	{	
		if ( x1 < x2 )
		{
			m_minX = x1;
			m_maxX = x2;
		}
		else
		{
			m_minX = x2;
			m_maxX = x1;
		}
		
		if ( y1 < y2 )
		{
			m_minY = y1;
			m_maxY = y2;
		}
		else
		{
			m_minY = y2;
			m_maxY = y1;
		}
		
		if ( z1 < z2 )
		{
			m_minZ = z1;
			m_maxZ = z2;
		}
		else
		{
			m_minZ = z2;
			m_maxZ = z1;
		}
	}
	
	/**
	 * Returns the width of the shape.
	 * 
	 * @return
	 */
	public float getWidth()
	{
		return m_maxX - m_minX;
	}
	
	/**
	 * Returns the height of the shape.
	 * 
	 * @return
	 */
	public float getHeight()
	{
		return m_maxY - m_minY;
	}
	
	@Override
	public void getBoundingBox( BoundingBox box )
	{
		box.set( this );
	}
	
	@Override
	public BoundingShape extrude( Vector3 origin, Vector3 direction, BoundingShape outExtrudedShape )
	{
		// create the shape if it hasn't been created yet
		if ( outExtrudedShape == null )
		{
			outExtrudedShape = new BoundingBox();
		}
		
		// get the type-specific shape
		if ( ( outExtrudedShape instanceof BoundingBox ) == false )
		{
			throw new RuntimeException( "Invalid extruded shape type for the BoundingBox" );
		}
		BoundingBox box = (BoundingBox)outExtrudedShape;
		
		// calculate new center of the extruded box
		m_tmpVec.set( origin ).add( direction );
		float hsX = ( m_maxX - m_minX ) * 0.5f;
		float hsY = ( m_maxY - m_minY ) * 0.5f;
		float hsZ = ( m_maxZ - m_minZ ) * 0.5f;
		
		// initialize the shape
		box.set( m_tmpVec.m_x - hsX, m_tmpVec.m_y - hsY, m_tmpVec.m_z - hsZ, m_tmpVec.m_x + hsX, m_tmpVec.m_y + hsY, m_tmpVec.m_z + hsZ );
		
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
	final public boolean doesOverlap( final BoundingBox box, Vector3 outIntersectPos )
	{
		boolean overlaps = !( m_minX > box.m_maxX || m_maxX < box.m_minX || m_minY > box.m_maxY || m_maxY < box.m_minY || m_minZ > box.m_maxZ || m_maxZ < box.m_minZ );
		
		if ( overlaps && outIntersectPos != null )
		{
			float minItrsctX = m_minX < box.m_minX ? m_minX : box.m_minX;
			float maxItrsctX = m_maxX > box.m_maxX ? m_maxX : box.m_maxX;
			float minItrsctY = m_minY < box.m_minY ? m_minY : box.m_minY;
			float maxItrsctY = m_maxY > box.m_maxY ? m_maxY : box.m_maxY;
			float minItrsctZ = m_minZ < box.m_minZ ? m_minZ : box.m_minZ;
			float maxItrsctZ = m_maxZ > box.m_maxZ ? m_maxZ : box.m_maxZ;
			outIntersectPos.set( ( minItrsctX + maxItrsctX ) * 0.5f, ( minItrsctY + maxItrsctY ) * 0.5f, ( minItrsctZ + maxItrsctZ ) * 0.5f );
		}
		
		return overlaps;
	}

	@Override
	public boolean doesOverlap( final BoundingSphere sphere, Vector3 outIntersectPos ) 
	{
		// the code's implemented in the BoundingSphere class - so let's use it
		return sphere.doesOverlap( this, outIntersectPos );
	}
	
	@Override
	final public boolean doesOverlap( final Vector3 pos, Vector3 outIntersectPos )
	{
		boolean overlaps = ( m_minX <= pos.m_x && m_maxX >= pos.m_x && m_minY <= pos.m_y && m_maxY >= pos.m_y && m_minZ <= pos.m_z && m_maxZ >= pos.m_z );
		if ( overlaps )
		{
			outIntersectPos.set( pos );
		}
		return overlaps;
	}
	
	@Override 
	public boolean doesOverlap( final Ray ray, Vector3 outIntersectPos )
	{
		return ray.doesOverlap( this, outIntersectPos );
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
		
		m_minX = node.getFloatValue( "minX" );
		m_minY = node.getFloatValue( "minY" );
		m_minZ = node.getFloatValue( "minZ" );
		m_maxX = node.getFloatValue( "maxX" );
		m_maxY = node.getFloatValue( "maxY" );
		m_maxZ = node.getFloatValue( "maxZ" );
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
