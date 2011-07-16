/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.controllers;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.Pedestrian;
import com.hypefoundry.engine.controllers.EntityController;
import com.hypefoundry.engine.game.Entity;


/**
 * AI of a pedestrian.
 * 
 * @author paksas
 *
 */
public class PedestrianAI extends EntityController
{
	private Pedestrian			m_pedestrian;
	
	
	/**
	 * Constructor.
	 * 
	 * @param pedestrian			controlled pedestrian
	 */
	public PedestrianAI( Entity pedestrian )
	{
		super( pedestrian );
		m_pedestrian = (Pedestrian)pedestrian;
	}
	
	@Override
	public void update( float deltaTime ) 
	{
		// TODO Auto-generated method stub
	}

}
