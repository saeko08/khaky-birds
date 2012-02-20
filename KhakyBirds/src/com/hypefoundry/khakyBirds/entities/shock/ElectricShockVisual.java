package com.hypefoundry.khakyBirds.entities.shock;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.renderer2D.Color;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.fx.LightningFX;


/**
 * Visual representation of an electric shock entity.
 * 
 * @author paksas
 *
 */
public class ElectricShockVisual extends EntityVisual 
{
	private ElectricShock	m_electricShock;
	private LightningFX		m_fx;

	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param entity
	 */
	public ElectricShockVisual( ResourceManager resMgr, Entity entity ) 
	{
		super( entity );
		
		m_electricShock = (ElectricShock)entity;

		m_fx = new LightningFX( m_electricShock.m_hostWire, 1.0f, 0.2f, 10, Color.RED );
		m_electricShock.setBoundingBox( new BoundingBox( -0.1f, 0, 0.1f, 1.0f ) );
	}

	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{        
		m_fx.draw( batcher, m_electricShock.m_offset );
	}

}
