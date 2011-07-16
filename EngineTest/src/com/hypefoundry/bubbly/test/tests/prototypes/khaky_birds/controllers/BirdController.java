/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.controllers;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.Bird;
import com.hypefoundry.engine.controllers.EntityController;
import com.hypefoundry.engine.game.Entity;


/**
 * Bird entity controller.
 * 
 * @author paksas
 *
 */
public class BirdController extends EntityController 
{
	private Bird			m_bird;
	
	/**
	 * Constructor.
	 * 
	 * @param entity
	 */
	public BirdController( Entity entity ) 
	{
		super( entity );
		m_bird = (Bird)entity;
	}

	@Override
	public void update( float deltaTime ) 
	{
		// TODO Auto-generated method stub
	}

}
