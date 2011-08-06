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
		float halfWidth = 4.8f; 	// TODO: config
		float halfHeight = 2.4f; 	// TODO: config
		setPosition( halfWidth, halfHeight, 100 );
		setBoundingBox( new BoundingBox( -halfWidth, -halfHeight, 0.0f, halfWidth, halfHeight, 0.0f ) );
	}
	
}

