/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.hideout;

import com.hypefoundry.engine.controllers.EntityController;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.World;

/**
 * @author azagor
 *
 */
public class HideoutAI extends EntityController 
{
	private World 					m_world;
	private Hideout					m_hideout;
	
	private final float 			m_respawnTime	= 10f;
	private float					m_counter		= 0f;

	public HideoutAI( World world, Entity entity ) 
	{
		super(entity);
		
		m_world = world;
		m_hideout = (Hideout)entity;
	}

	@Override
	public void update(float deltaTime) 
	{
		m_counter += deltaTime;
		if (m_counter >= m_respawnTime )
		{
			if(m_hideout.m_pedestrians > 0)
			{
				m_hideout.goOut();
				m_hideout.m_pedestrians -= 1;
				m_counter = 0;
				
			}
			else
			{
				m_counter = 0;
			}
		}
		
	}

}
