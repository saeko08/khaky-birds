/**
 * 
 */
package com.hypefoundry.kabloons.entities.buzzSaw;

import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.EntityEvent;

/**
 * Event sent when something ran into a destructive device and should be destroyed.
 * 
 * @author Paksas
 */
public class Destroy implements EntityEvent 
{
	@Override
	public void deserialize( DataLoader loader ) {}

}
