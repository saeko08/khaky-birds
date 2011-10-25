package com.hypefoundry.khakyBirds.entities.ground;

import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;

/**
 * Represents the ground on which the unsuspecting people wander.
 * 
 * @author paksas
 *
 */
public class Ground extends Entity
{
	String			m_texturePath;
	
	@Override
	public void onLoad( DataLoader loader ) 
	{
		m_texturePath = loader.getStringValue( "texturePath" );
	}
}

