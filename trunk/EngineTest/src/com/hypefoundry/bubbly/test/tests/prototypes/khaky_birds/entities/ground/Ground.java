package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.ground;

import com.hypefoundry.engine.game.Entity;

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
		setPosition( 0, 0, 100 );
	}
	
	@Override
	public void onCollision( Entity colider ) 
	{
		// nothing to do here
	}
}

