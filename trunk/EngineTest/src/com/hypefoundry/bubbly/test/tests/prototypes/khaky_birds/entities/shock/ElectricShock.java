package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.shock;

import com.hypefoundry.engine.game.Entity;


/**
 * An electricity spike running through the electric cables
 * that may shock and eventually kill our bird.
 * 
 * @author paksas
 *
 */
public class ElectricShock extends Entity 
{

	/**
	 * Constructor.
	 * 
	 * @param cableYOffset
	 */
	public ElectricShock( float cableXOffset ) 
	{
		setPosition( cableXOffset, 0, 5 );
	}

	@Override
	public void onCollision( Entity collider ) 
	{
		if ( collider instanceof Shockable )
		{
			( (Shockable)collider ).getShocked();
		}
	}
}
