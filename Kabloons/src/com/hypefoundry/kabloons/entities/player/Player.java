/**
 * 
 */
package com.hypefoundry.kabloons.entities.player;

import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.kabloons.entities.fan.Fan;

/**
 * @author Paksas
 *
 */
public class Player extends Entity 
{
	int[]						m_fansLeft 					= new int[Fan.Direction.values().length];
	
	
	@Override
	public void onLoad( DataLoader loader ) 
	{
		m_fansLeft[Fan.Direction.Left.m_idx] = loader.getIntValue( "leftFans", 0 );
		m_fansLeft[Fan.Direction.Right.m_idx] = loader.getIntValue( "rightFans", 0 );
	}
}
