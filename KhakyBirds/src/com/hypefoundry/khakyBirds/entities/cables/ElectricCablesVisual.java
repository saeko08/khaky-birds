package com.hypefoundry.khakyBirds.entities.cables;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.renderer2D.Color;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.RenderState;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;


/**
 * Visual representation of an electric cables entity.
 * @author paksas
 *
 */
public class ElectricCablesVisual extends EntityVisual
{
	private ElectricCables		m_cables;
	private RenderState			m_rs = new RenderState();
	
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param entity
	 */
	public ElectricCablesVisual( ResourceManager resMgr, Entity entity )
	{
		super( entity );
		
		m_rs.setLineWidth( 1.5f );
		m_cables = (ElectricCables)entity;
	}

	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		for ( int i = 0; i < m_cables.m_wires.length; ++i )
		{
			batcher.drawSpline( m_cables.m_wires[i], Color.BLUE, m_rs );
		}
	}
}
