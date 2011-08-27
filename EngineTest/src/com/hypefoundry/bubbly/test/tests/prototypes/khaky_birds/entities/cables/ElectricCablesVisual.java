package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.cables;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.core.Texture;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.math.BoundingShape;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;


/**
 * Visual representation of an electric cables entity.
 * @author paksas
 *
 */
public class ElectricCablesVisual extends EntityVisual
{
	private ElectricCables		m_cables;
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param entity
	 */
	public ElectricCablesVisual( ResourceManager resMgr, Entity entity )
	{
		super( entity );
		
		m_cables = (ElectricCables)entity;
	}

	@Override
	public void draw( SpriteBatcher batcher, float deltaTime ) 
	{
		for ( int i = 0; i < m_cables.m_wires.length; ++i )
		{
			batcher.drawSpline( m_cables.m_wires[i] );
		}
	}
}
