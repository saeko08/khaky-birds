/**
 * 
 */
package com.hypefoundry.kabloons.entities.baloon;

import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.PhysicalBody;
import com.hypefoundry.engine.world.Entity;


/**
 * @author Paksas
 *
 */
public class BaloonPhysicalBody extends PhysicalBody 
{
	private BoundingBox		m_physicalBounds = new BoundingBox();
	private BoundingBox		m_physicalBoundsWorldSpace = new BoundingBox();
	
	/**
	 * Constructor.
	 * 
	 * @param entity
	 */
	public BaloonPhysicalBody( Entity entity ) 
	{
		super( entity, true );
		
		BoundingBox entityBB = entity.getBoundingShape();
		m_physicalBounds.set( entityBB.m_minX * 0.6f, entityBB.m_minY * 0.6f, entityBB.m_maxX * 0.6f, entityBB.m_maxY * 0.9f );
		
		setCollisionShape( m_physicalBoundsWorldSpace );
	}
	
	@Override
	protected void preCalculateCollisionShapes( float deltaTime ) 
	{
		m_physicalBoundsWorldSpace.set( m_physicalBounds );
		m_physicalBoundsWorldSpace.translate( m_entity.getPosition() );
	}

	@Override
	protected void respondToCollision( PhysicalBody collider, Vector3 collisionPoint )
	{
		// do nothing
	}

}
