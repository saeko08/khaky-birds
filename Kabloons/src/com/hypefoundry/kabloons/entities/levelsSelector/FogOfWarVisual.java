/**
 * 
 */
package com.hypefoundry.kabloons.entities.levelsSelector;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleSystemPlayer;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public class FogOfWarVisual extends EntityVisual 
{

	private ParticleSystemPlayer	m_player;
	
	/**
	 * Constructor.
	 * 
	 * @param entity
	 */
	public FogOfWarVisual( Entity entity ) 
	{
		super( entity );
		
		FogOfWar fogOfWar = (FogOfWar)entity;
		
		m_player = new ParticleSystemPlayer( fogOfWar.m_particleSystem, true );
		
		// add a presimulation period so that all the particles get spawned before we actually show the screen 
		m_player.simulate( 5.0f );
	}

	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		Vector3 pos = m_entity.getPosition();
		m_player.draw( pos.m_x, pos.m_y, batcher, deltaTime );
	}
	
	@Override
	public void onRemoved()
	{
		if ( m_player != null )
		{
			m_player.release();
		}
	}

}
