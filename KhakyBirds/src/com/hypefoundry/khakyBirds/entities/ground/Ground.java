package com.hypefoundry.khakyBirds.entities.ground;

import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.math.BoundingBox;

/**
 * Represents the ground on which the unsuspecting people wander.
 * 
 * @author paksas
 *
 */
public class Ground extends Entity
{

	/**
	 * Constructor.
	 */
	public Ground()
	{
		float halfWidth = 2.4f; 	// TODO: config
		float halfHeight = 4.8f; 	// TODO: config
		setPosition( halfWidth, halfHeight, 100 );
		setBoundingBox( new BoundingBox( -halfWidth, -halfHeight, 0.0f, halfWidth, halfHeight, 0.0f ) );
	}
	
}

