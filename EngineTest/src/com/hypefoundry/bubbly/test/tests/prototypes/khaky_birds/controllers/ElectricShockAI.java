/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.controllers;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.ElectricShock;
import com.hypefoundry.engine.controllers.EntityController;
import com.hypefoundry.engine.game.Entity;


/**
 * Electric shock entity controller.
 * 
 * @author paksas
 *
 */
public class ElectricShockAI extends EntityController 
{
	private ElectricShock		m_shock;
	
	/**
	 * Constructor.
	 * 
	 * @param entity
	 */
	public ElectricShockAI( Entity entity ) 
	{
		super(entity);
		
		m_shock = (ElectricShock)entity;
	}

	@Override
	public void update( float deltaTime ) 
	{
		// TODO Auto-generated method stub

	}

}
