/**
 * 
 */
package com.hypefoundry.khakyBirds.campaign;

import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;


/**
 * Level 01 script.
 * 
 * @author Paksas
 */
public class Level01 extends Entity
{
	float			m_gameplayTime;
	
	
	@Override
	public void onLoad( DataLoader loader ) 
	{
		m_gameplayTime = loader.getFloatValue( "gameplayTime" );
	}
}

