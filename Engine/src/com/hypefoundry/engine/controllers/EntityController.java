/**
 * 
 */
package com.hypefoundry.engine.controllers;

import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.game.Updatable;

/**
 * Entity controller.
 * 
 * @author paksas
 *
 */
public abstract class EntityController implements Updatable 
{
	protected Entity		m_entity;
	
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
	 * Checks if this controller controlls the specified entity.
	 * 
	 * @param entity
	 * @return
	 */
	public final boolean isControllerOf( Entity entity )
	{
		return m_entity.equals( entity );
	}
}
