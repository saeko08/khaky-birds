/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.hideout;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.perkPedestrian.PerkPedestrian;
import com.hypefoundry.engine.controllers.EntityController;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.World;
import com.hypefoundry.engine.world.WorldView;

/**
 * @author azagor
 *
 */
public class HideoutAI extends EntityController
{
	private World 					m_world;
	private Hideout					m_hideout;
	
	private final float 			m_pedestrianRespawnTime	= 10f;
	private float					m_pedestrianCounter		= 0f;
	private final float 			m_perkPedestrianRespawnTime	= 10f;
	private float					m_perkPedestrianCounter		= 0f;
	private int						m_maxPerkPedestrianNumber	= 2;

	public HideoutAI( World world, Entity entity ) 
	{
		super(entity);
		
		m_world = world;
		m_hideout = (Hideout)entity;
	}

	@Override
	public void update(float deltaTime) 
	{
		m_pedestrianCounter += deltaTime;
		if (m_pedestrianCounter >= m_pedestrianRespawnTime )
		{
			if(m_hideout.m_pedestrians > 0)
			{
				m_hideout.goOut();
				m_hideout.m_pedestrians -= 1;
				m_pedestrianCounter = 0;
				
			}
			else
			{
				m_pedestrianCounter = 0;
			}
		}
		if(m_maxPerkPedestrianNumber > 0)
		{
			m_perkPedestrianCounter += deltaTime;
			
			if (m_perkPedestrianCounter >= m_perkPedestrianRespawnTime )
			{
				
				if(m_hideout.m_perkPedestrians < m_maxPerkPedestrianNumber)
				{
					m_hideout.perkPedestrianGoOut();
					m_hideout.m_perkPedestrians += 1;
					m_perkPedestrianCounter = 0;
					
				}
				else
				{
					m_perkPedestrianCounter = 0;
				}
			}
		}
		
	}

}
