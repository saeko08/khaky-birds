/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.controllers;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.ElectricCables;
import com.hypefoundry.engine.controllers.EntityController;
import com.hypefoundry.engine.game.Entity;

/**
 * Electric cables entity controller.
 * 
 * @author paksas
 *
 */
public class ElectricCablesAI extends EntityController 
{
	private ElectricCables			m_cables;
	
	/**
	 * Constructor.
	 * 
	 * @param entity
	 */
	public ElectricCablesAI( Entity entity ) 
	{
		super(entity);
		m_cables = (ElectricCables)entity;
	}

	@Override
	public void update( float deltaTime ) 
	{
		// TODO Auto-generated method stub

	}

}
