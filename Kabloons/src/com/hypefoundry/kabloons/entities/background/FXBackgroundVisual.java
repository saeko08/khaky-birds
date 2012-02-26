/**
 * 
 */
package com.hypefoundry.kabloons.entities.background;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleSystem;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleSystemPlayer;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public class FXBackgroundVisual extends EntityVisual 
{
	private ParticleSystemPlayer	m_player;
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param entity
	 */
	public FXBackgroundVisual( ResourceManager resMgr, Entity entity ) 
	{
		super( entity );
		
		FXBackground decoration = (FXBackground)entity;
		
		ParticleSystem ps = resMgr.getResource( ParticleSystem.class, decoration.m_path );
		m_player = new ParticleSystemPlayer( ps, true );
	}

	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		Vector3 pos = m_entity.getPosition();
		m_player.draw( pos.m_x, pos.m_y, batcher, deltaTime );
	}
}
