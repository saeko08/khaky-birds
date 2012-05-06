/**
 * 
 */
package com.hypefoundry.engine.movies;

import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public class MovieEntity extends Entity 
{
	public String		m_path;

	@Override
	public void onLoad( DataLoader loader ) 
	{
		m_path = loader.getStringValue( "path" );
	}
}
