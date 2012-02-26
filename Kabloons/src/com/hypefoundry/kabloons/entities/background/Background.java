/**
 * 
 */
package com.hypefoundry.kabloons.entities.background;

import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;

/**
 * Background images and animations.
 * 
 * @author Paksas
 *
 */
public class Background extends Entity 
{
	String			m_path;
	float			m_rotationSpeed;
	
	@Override
	public void onLoad( DataLoader loader ) 
	{
		m_path = loader.getStringValue( "path" );
		m_rotationSpeed = loader.getFloatValue( "rotationSpeed", 0.0f );
	}
}
