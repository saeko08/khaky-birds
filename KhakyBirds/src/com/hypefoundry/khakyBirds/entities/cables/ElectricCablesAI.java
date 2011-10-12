/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.cables;

import com.hypefoundry.khakyBirds.entities.shock.ElectricShock;
import com.hypefoundry.engine.controllers.EntityController;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.World;

/**
 * Electric cables entity controller.
 * 
 * @author paksas
 *
 */
public class ElectricCablesAI extends EntityController 
{
	private World 					m_world;
	private ElectricCables			m_cables;
	
	private int						m_cablesCount = 4;
	private int						m_prevShockedCableId = 0;
	private final float				m_shockSpawnTimeout = 10.0f;
	private float					m_shockSpawnTimeElapsed = 0;
	/**
	 * Constructor.
	 * 
	 * @param world
	 * @param entity
	 */
	public ElectricCablesAI( World world, Entity entity ) 
	{
		super(entity);
		
		m_world = world;
		m_cables = (ElectricCables)entity;
	}

	@Override
	public void update( float deltaTime ) 
	{
		// the cables spawn electric shocks from time to time - so countdown to the next shock spawn
		m_shockSpawnTimeElapsed -= deltaTime;
		
		if ( m_shockSpawnTimeElapsed <= 0.0f )
		{
			// select a cable on which the shock should be spawned
			m_prevShockedCableId = selectShockedCable();
			
			// the spawn time has passed - spawn an electric charge
			m_world.addEntity( new ElectricShock( m_cables.m_wires[ m_prevShockedCableId ] ) );
			
			// reset the spawn timer
			m_shockSpawnTimeElapsed = m_shockSpawnTimeout;
		}
	}

	private int selectShockedCable() 
	{
		return ( m_prevShockedCableId + 1 ) % m_cablesCount;
	}

}
