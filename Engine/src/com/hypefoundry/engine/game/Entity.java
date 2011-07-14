package com.hypefoundry.engine.game;

import com.hypefoundry.engine.util.BoundingBox;
import com.hypefoundry.engine.util.Vector2;


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
	private Vector2				m_pos;
	
	
	protected Entity()
	{
		m_bb = new BoundingBox( 0, 0, 0, 0 );
		m_worldBB = new BoundingBox( 0, 0, 0, 0 );
		m_pos = new Vector2();
	}
	
	protected final void setBoundingBox( BoundingBox bb )
	{
		m_bb = bb;
	}
	
	public final void setPosition( float x, float y )
	{
		m_pos.m_x = x;
		m_pos.m_y = y;
		
		// update the world bounding box
		m_worldBB.m_minX = m_bb.m_minX + x;
		m_worldBB.m_maxX = m_bb.m_maxX + x;
		m_worldBB.m_minY = m_bb.m_minY + y;
		m_worldBB.m_maxY = m_bb.m_maxY + y;
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
