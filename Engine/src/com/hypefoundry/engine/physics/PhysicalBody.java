/**
 * 
 */
package com.hypefoundry.engine.physics;

import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.util.SpatialGridObject;


/**
 * Represents a physical body. Is responsible for running the forces
 * simulation on the body and for implementing all sorts of cool collision responses etc.
 * 
 * CAUTION: without a representation, an entity WON'T receive onCollision
 * notifications.
 * 
 * @author Paksas
 *
 */
public abstract class PhysicalBody implements SpatialGridObject
{
	protected Entity 			m_entity;
	protected DynamicObject		m_dynamicObjectAspect;
	
	/**
	 * Constructor.
	 * 
	 * @param entity		represented entity
	 */
	public PhysicalBody( Entity entity ) 
	{
		m_entity = entity;
		m_dynamicObjectAspect = m_entity.query( DynamicObject.class );
	}

	@Override
	public final BoundingBox getBounds()
	{
		return m_entity.getWorldBounds();
	}

	/**
	 * Checks if two bodies overlap.
	 * 
	 * @param collider
	 * @return
	 */
	public final boolean doesOverlap( PhysicalBody collider ) 
	{
		BoundingBox bb1 = m_entity.getWorldBounds();
		BoundingBox bb2 = collider.m_entity.getWorldBounds();
		return bb1.doesOverlap( bb2 );
	}

	/**
	 * Notifies the body that it's in collision with some other body.
	 * 
	 * @param collider
	 */
	public final void onCollision( PhysicalBody collider ) 
	{
		m_entity.onCollision( collider.m_entity );	
		respondToCollision( collider );
	}

	/**
	 * Checks if the body represents the specified entity.
	 * 
	 * @param entity
	 * @return
	 */
	public final boolean isBodyOf( Entity entity ) 
	{
		return m_entity == entity;
	}

	/**
	 * Simulates the forces that apply to this body.
	 * 
	 * @param deltaTime
	 */
	public final void simulate( float deltaTime ) 
	{
		if ( m_dynamicObjectAspect != null )
		{
			// only dynamic objects are influenced by the forces
			m_dynamicObjectAspect.simulate( deltaTime, m_entity );
		}
	}
	
	/**
	 * Collision response implementation.
	 * 
	 * @param collider
	 */
	public abstract void respondToCollision( PhysicalBody collider );
}
