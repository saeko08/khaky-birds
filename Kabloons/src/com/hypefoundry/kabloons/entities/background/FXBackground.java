/**
 * 
 */
package com.hypefoundry.kabloons.entities.background;

import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public class FXBackground extends Entity 
{
	String		m_path;
	
	@Override
	public void onLoad( DataLoader loader ) 
	{
		m_path = loader.getStringValue( "path" );
	}
}
