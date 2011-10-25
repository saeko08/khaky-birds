/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.decoration;

import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;

/**
 * A simple entity with no logic that just plays an animation.
 * 
 * @author Paksas
 */
public class AnimatedDecoration extends Entity 
{
	String		m_animationPath;
	
	
	@Override
	public void onLoad( DataLoader loader ) 
	{
		m_animationPath = loader.getStringValue( "animationPath" );
	}
}
