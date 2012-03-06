/**
 * 
 */
package com.hypefoundry.kabloons.entities.levelsSelector;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleSystem;
import com.hypefoundry.engine.renderer2D.particleSystem.affectors.ClearSkyAffector;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public class FogOfWar extends Entity
{
	ResourceManager		m_resMgr;
	ParticleSystem 		m_particleSystem;
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 */
	public FogOfWar( ResourceManager resMgr )
	{
		m_resMgr = resMgr;
	}
	
	@Override
	public void onLoad( DataLoader loader ) 
	{
		String path = loader.getStringValue( "path" );
		m_particleSystem = m_resMgr.getResource( ParticleSystem.class, path );
	}

	/**
	 * Adds a new clear sky area to the particle system.
	 * 
	 * @param area
	 */
	public void addClearSkyArea( BoundingBox area ) 
	{
		if ( area != null )
		{
			m_particleSystem.addAffector( new ClearSkyAffector( area, 0.3f ) );
		}
	}
}
