package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.cables;

import com.hypefoundry.engine.game.Entity;


/**
 * Electric cables the bird jumps on.
 * 
 * @author paksas
 *
 */
public class ElectricCables extends Entity
{
	
	/**
	 * Constructor.
	 */
	public ElectricCables()
	{
		setPosition( 0, 0, 10 );
	}
	
	@Override
	public void onCollision( Entity colider ) 
	{
		// TODO Auto-generated method stub
	}
	
}
