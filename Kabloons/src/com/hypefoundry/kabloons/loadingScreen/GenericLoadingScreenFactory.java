/**
 * 
 */
package com.hypefoundry.kabloons.loadingScreen;

import com.hypefoundry.engine.impl.game.LoadingScreenFactory;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public class GenericLoadingScreenFactory extends LoadingScreenFactory 
{
	@Override
	public EntityVisual instantiate( Entity parentEntity ) 
	{
		return new GenericLoadingScreenVisual( m_resMgr, parentEntity );
	}

}
