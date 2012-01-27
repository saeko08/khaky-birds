package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.ground;

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
		setBoundingBox( new BoundingBox( -halfWidth, -halfHeight, halfWidth, halfHeight ) );
	}
	
}

