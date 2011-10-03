/**
 * 
 */
package com.hypefoundry.engine.physics;

import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityEventException;
import com.hypefoundry.engine.world.EventFactory;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.BoundingShape;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.events.CollisionEvent;
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
	protected Entity 						m_entity;
	protected DynamicObject					m_dynamicObjectAspect;
	final boolean							m_checkCollisions;
	
	protected BoundingShape					m_collisionShape			= null;
	private BoundingShape					m_extrudedCollisionShape 	= null;
	private BoundingBox						m_runtimeWorldBounds		= new BoundingBox();
	private Vector3							m_tmpVelocity				= new Vector3();
	private Vector3							m_tmpCollisionPoint 		= new Vector3();
	
	
	/**
	 * Constructor.
	 * 
	 * @param entity				represented entity
	 * @param collisionShape		collision shape for the body
	 * @param checkCollisions		checks collisions with other entities
	 */
	public PhysicalBody( Entity entity, BoundingShape collisionShape, boolean checkCollisions ) 
	{
		m_checkCollisions = checkCollisions;
		m_entity = entity;
		m_dynamicObjectAspect = m_entity.query( DynamicObject.class );
		m_collisionShape = collisionShape;
		
		// register events
		m_entity.registerEvent( CollisionEvent.class, new EventFactory< CollisionEvent >() { @Override public CollisionEvent createObject() { return new CollisionEvent(); } } );
	}
	
	/**
	 * Constructor.
	 * 
	 * @param entity				represented entity
	 * @param checkCollisions		checks collisions with other entities
	 */
	public PhysicalBody( Entity entity, boolean checkCollisions ) 
	{
		this( entity, entity.getWorldBounds(), checkCollisions );
	}

	@Override
	public final BoundingBox getBounds()
	{
		return m_runtimeWorldBounds;
	}

	/**
	 * Checks if two bodies collide ( narrow phase collision detection ).
	 * 
	 * @param collider
	 */
	void checkCollision( PhysicalBody collider ) 
	{
		if ( m_extrudedCollisionShape == null || collider.m_extrudedCollisionShape == null )
		{
			return;
		}
		
		boolean doOverlap = m_extrudedCollisionShape.doesOverlap( collider.m_extrudedCollisionShape, m_tmpCollisionPoint );
		if ( doOverlap )
		{
			try
			{
				CollisionEvent event = m_entity.sendEvent( CollisionEvent.class );
				if ( event != null )
				{
					event.m_collider = collider.m_entity;
				}
			}
			catch ( EntityEventException ex )
			{
				// too many events - don't process
			}
			respondToCollision( collider, m_tmpCollisionPoint );
		}
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
	 * Calculates the collision shapes for this simulation frame.
	 * 
	 * @param deltaTime
	 */
	final void calculateCollisionShapes( float deltaTime ) 
	{
		if ( m_dynamicObjectAspect != null )
		{
			m_tmpVelocity.set( m_dynamicObjectAspect.m_velocity ).scale( deltaTime );
			
			m_extrudedCollisionShape = m_collisionShape.extrude( m_entity.getPosition(), m_tmpVelocity, m_extrudedCollisionShape );
			m_extrudedCollisionShape.getBoundingBox( m_runtimeWorldBounds );
		}
	}

	/**
	 * Simulates the forces that apply to this body.
	 * 
	 * @param deltaTime
	 */
	final void simulate( float deltaTime ) 
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
	 * @param collisionPoint
	 */
	protected abstract void respondToCollision( PhysicalBody collider, Vector3 collisionPoint );
}
