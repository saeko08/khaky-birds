package com.hypefoundry.engine.controllers;


import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.util.ObjectFactory;

/**
 * Creates an instance of a EntityController.
 * 
 * @author paksas
 *
 */
public interface EntityControllerFactory extends ObjectFactory< Entity, EntityController >
{
	/**
	 * The factory method.
	 * 
	 * @param parentEntity
	 * @return
	 */
	EntityController instantiate( Entity parentEntity );
}
