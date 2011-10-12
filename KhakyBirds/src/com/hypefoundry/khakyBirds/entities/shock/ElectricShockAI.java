/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.shock;

import com.hypefoundry.engine.controllers.EntityController;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.world.Entity;


/**
 * Electric shock entity controller.
 * 
 * @author paksas
 *
 */
public class ElectricShockAI extends EntityController 
{
	private ElectricShock		m_shock;
	private final float			m_moveSpeed = 1.0f;
	
	private Vector3				m_tmpPos = new Vector3();
	
	
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
		// start moving the shock up the screen
		m_shock.m_offset += m_moveSpeed * deltaTime;
		
		m_shock.m_hostWire.getPosition( m_shock.m_offset, m_tmpPos );
		
		float z = m_shock.getPosition().m_z;
		m_shock.setPosition( m_tmpPos.m_x, m_tmpPos.m_y, z );
	}

}
