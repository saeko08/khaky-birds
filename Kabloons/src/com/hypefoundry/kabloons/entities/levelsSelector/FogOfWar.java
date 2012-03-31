/**
 * 
 */
package com.hypefoundry.kabloons.entities.levelsSelector;

import java.util.*;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleSystem;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleSystemListener;
import com.hypefoundry.engine.renderer2D.particleSystem.affectors.ClearSkyAffector;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.World;

/**
 * @author Paksas
 *
 */
public class FogOfWar extends Entity implements ParticleSystemListener
{
	ResourceManager				m_resMgr;
	ParticleSystem 				m_particleSystem;
	
	// additional affectors
	List< ClearSkyAffector >	m_affectors = new ArrayList< ClearSkyAffector >();
	
	
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
		m_particleSystem.attachListener( this );
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
			ClearSkyAffector affector = new ClearSkyAffector( area, 0.3f );
			m_affectors.add( affector );
			
			if ( m_particleSystem != null )
			{
				m_particleSystem.addAffector( affector, this );
			}
		}
	}

	@Override
	public void onSystemInitialized() 
	{	
		m_particleSystem.addAffectors( m_affectors, this );
	}

	@Override
	public void onSystemReleased() 
	{	
		// add the additional affectors
	}
	
	@Override
	public void onRemovedFromWorld( World world )
	{
		// detach the listener
		if ( m_particleSystem != null )
		{
			m_particleSystem.detachListener( this );
		}
	}
}
