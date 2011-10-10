package com.hypefoundry.engine.renderer2D;

import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.util.SpatialGridObject;


/* The visuals of an entity.
 * 
 * @author paksas
 *
 */
public abstract class EntityVisual implements SpatialGridObject
{
	protected Entity			m_entity = null;
	
	/**
	 * Constructor.
	 * 
	 * @param entity			represented entity.
	 */
	public EntityVisual( Entity entity )
	{
		m_entity = entity;
	}
	
	/**
	 * Checks if the visual represents the specified entity.
	 * 
	 * @param entity
	 * @return
	 */
	public boolean isVisualOf( Entity entity )
	{
		return m_entity.equals( entity );
	}
	
	/**
	 * Draw self.
	 * 
	 * @param batcher
	 * @param camera
	 * @param deltaTime
	 */
	public abstract void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime );

	/**
	 * Returns the position in the Z buffer. 
	 * 
	 * Numbers towards the negative values are closer to the screen,
	 * and the numbers towards the positive values are farther from the screen.
	 * 
	 * @return
	 */
	final float getZ()
	{
		return m_entity.getPosition().m_z;
	}
	
	@Override
	public BoundingBox getBounds()
	{
		return m_entity.getWorldBounds();
	}
}
