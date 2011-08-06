/**
 * 
 */
package com.hypefoundry.engine.physics.events;

import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityEvent;

/**
 * An event received by entities with a physical representation.
 * 
 * @author Paksas
 *
 */
public class CollisionEvent implements EntityEvent 
{
	public Entity		m_collider = null;
}
