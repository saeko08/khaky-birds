package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird;

import com.hypefoundry.engine.game.Entity;


/**
 * The game's main character. A nasty bird that jumps on the cables
 * trying to avoid electrocution and make the lives of the pedestrians
 * as miserable as possible.
 * 
 * @author paksas
 *
 */
public class Bird extends Entity 
{

	/**
	 * Constructor.
	 */
	public Bird()
	{
		setPosition( 0, 0, 0 );
	}
	
	@Override
	public void onCollision( Entity colider ) 
	{
		// TODO Auto-generated method stub

	}

}
