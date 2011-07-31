/**
 * 
 */
package com.hypefoundry.engine.physics;

import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.util.ObjectFactory;


/**
 * Creates an instance of a PhysicalBody.
 * 
 * @author Paksas
 *
 */
public interface PhysicalBodyFactory extends ObjectFactory< Entity, PhysicalBody > 
{
	/**
	 * The factory method.
	 * 
	 * @param parentEntity
	 * @return
	 */
	PhysicalBody instantiate( Entity parentEntity );
}
