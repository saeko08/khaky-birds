package com.hypefoundry.engine.game;

import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;

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
	public final void setBoundingBox( BoundingBox bb )
	{
		m_bb = bb;
		
		updateWorldBounds();
	}

	/**
	 * Updates the world bounds of the entity.
	 */
	private void updateWorldBounds() 
	{
		// update the world bounding box
		m_worldBB.m_minX = m_bb.m_minX + m_pos.m_x;
		m_worldBB.m_maxX = m_bb.m_maxX + m_pos.m_x;
		m_worldBB.m_minY = m_bb.m_minY + m_pos.m_y;
		m_worldBB.m_maxY = m_bb.m_maxY + m_pos.m_y;
		m_worldBB.m_minZ = m_bb.m_minZ + m_pos.m_z;
		m_worldBB.m_maxZ = m_bb.m_maxZ + m_pos.m_z;
	}
	
	/**
	 * Returns the world bounds of the entity.
	 * @return
	 */
	public final BoundingBox getWorldBounds()
	{
		return m_worldBB;
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
		
		updateWorldBounds();
	}
	
	/**
	 * Sets the entity's position.
	 * 
	 * @param rhs
	 */
	public final void setPosition( Vector3 rhs )
	{
		m_pos.m_x = rhs.m_x;
		m_pos.m_y = rhs.m_y;
		m_pos.m_z = rhs.m_z;
		
		updateWorldBounds();
	}
	
	
	/**
	 * Translates the entity by the specified vector.
	 * 
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void translate( float dx, float dy, float dz )
	{
		m_pos.m_x += dx;
		m_pos.m_y += dy;
		m_pos.m_z += dz;
		
		updateWorldBounds();
	}
	
	/**
	 * Translates the entity by the specified vector.
	 * 
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void translate( Vector3 ds )
	{
		m_pos.add( ds );
		
		updateWorldBounds();
	}

	/**
	 * Informs that the entity collided with another entity.
	 * 
	 * @param colider		who did the entity collide with
	 */
	public abstract void onCollision( Entity colider );

	/**
	 * Informs the entity that it's been added to the world.
	 * 
	 * @param hostWorld
	 */
	public void onAddedToWorld( World hostWorld ) {}
	
	/**
	 * Informs the entity that it's been removed from the world.
	 * 
	 * @param hostWorld
	 */
	public void onRemovedFromWorld( World hostWorld ) {}

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
