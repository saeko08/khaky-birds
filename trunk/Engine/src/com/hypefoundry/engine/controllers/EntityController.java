/**
 * 
 */
package com.hypefoundry.engine.controllers;

import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.game.Updatable;

/**
 * Entity controller.
 * 
 * @author paksas
 *
 */
public abstract class EntityController implements Updatable 
{
	public Entity		m_entity;
	
	/**
	 * Constructor.
	 * 
	 * @param entity			controlled entity
	 */
	protected EntityController( Entity entity )
	{
		m_entity = entity;
	}
	
	/**
	 * Checks if this controller controls the specified entity.
	 * 
	 * @param entity
	 * @return
	 */
	public final boolean isControllerOf( Entity entity )
	{
		return m_entity.equals( entity );
	}
}
