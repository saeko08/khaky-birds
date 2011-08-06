package com.hypefoundry.engine.renderer2D;

import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.util.ObjectFactory;

/**
 * Creates an instance of a VisualEntity.
 * 
 * @author paksas
 *
 */
public interface EntityVisualFactory extends ObjectFactory< Entity, EntityVisual >
{
	/**
	 * The factory method.
	 * 
	 * @param parentEntity
	 * @return
	 */
	EntityVisual instantiate( Entity parentEntity );
}
