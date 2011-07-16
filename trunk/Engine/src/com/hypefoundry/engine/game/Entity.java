package com.hypefoundry.engine.game;

import com.hypefoundry.engine.util.BoundingBox;
import com.hypefoundry.engine.util.Vector3;


/**
 * A game entity. Can be an agent, a piece of decoration - anything
 * that we want to place in the game world. 
 * 
 * @author paksas
 *
 */
public abstract class Entity 
{
	private BoundingBox			m_bb;
	private BoundingBox			m_worldBB;
	private Vector3				m_pos;
	
	/**
	 * Constructor.
	 */
	protected Entity()
	{
		m_bb = new BoundingBox( 0, 0, 0, 0, 0, 0 );
		m_worldBB = new BoundingBox( 0, 0, 0, 0, 0, 0 );
		m_pos = new Vector3();
	}
	
	/**
	 * Sets the bounding box of the entity.
	 * 
	 * @param bb
	 */
	protected final void setBoundingBox( BoundingBox bb )
	{
		m_bb = bb;
	}
	
	/**
	 * Sets the entity's position.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public final void setPosition( float x, float y, float z )
	{
		m_pos.m_x = x;
		m_pos.m_y = y;
		m_pos.m_z = z;
		
		// update the world bounding box
		m_worldBB.m_minX = m_bb.m_minX + x;
		m_worldBB.m_maxX = m_bb.m_maxX + x;
		m_worldBB.m_minY = m_bb.m_minY + y;
		m_worldBB.m_maxY = m_bb.m_maxY + y;
		m_worldBB.m_minZ = m_bb.m_minZ + z;
		m_worldBB.m_maxZ = m_bb.m_maxZ + z;
	}
	
	/**
	 * Returns the position in the Z buffer. 
	 * 
	 * Numbers towards the negative values are closer to the screen,
	 * and the numbers towards the positive values are farther from the screen.
	 * 
	 * @return
	 */
	public final Vector3 getPosition()
	{
		return m_pos;
	}

	/**
	 * Informs that the entity collided with another entity.
	 * 
	 * @param colider		who did the entity collide with
	 */
	public abstract void onCollision( Entity colider );


	/**
	 * Checks if two entities overlap
	 * 
	 * @param e2
	 * @return
	 */
	public final boolean doesOverlap( Entity e2 ) 
	{
		return m_worldBB.doesOverlap( e2.m_worldBB );
	}
}
