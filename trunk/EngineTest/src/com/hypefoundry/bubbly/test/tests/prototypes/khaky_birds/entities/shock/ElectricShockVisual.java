package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.shock;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.core.Texture;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.math.BoundingShape;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;


/**
 * Visual representation of an electric shock entity.
 * 
 * @author paksas
 *
 */
public class ElectricShockVisual extends EntityVisual 
{
	private ElectricShock	m_electricShock;
	private TextureRegion	m_pixmap;
	
	
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
		
		Texture atlas = resMgr.getResource( Texture.class, "khaky_birds_prototype/atlas.png" );
		m_pixmap = new TextureRegion( atlas, 627, 169, 31, 92 );
	}

	@Override
	public void draw( SpriteBatcher batcher, float deltaTime ) 
	{
		Vector3 pos = m_electricShock.getPosition();
		BoundingShape bs = m_electricShock.getBoundingShape();
		
		batcher.drawSprite( pos.m_x, pos.m_y, bs.getWidth(), bs.getHeight(), m_pixmap );
	}

}
