/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.ground;

import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.math.BoundingShape;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;


/**
 * Visual representation of the ground entity.
 * 
 * @author paksas
 *
 */
public class GroundVisual extends EntityVisual 
{
	private TextureRegion		m_pixmap;
	
	/**
	 * Constructor.
	 * 
	 * @param graphics
	 * @param entity
	 */
	public GroundVisual( Entity entity ) 
	{
		super( entity );
	}

	@Override
	public void draw( SpriteBatcher batcher ) 
	{
		Vector3 pos = m_entity.getPosition();
		BoundingShape bs = m_entity.getBoundingShape();
		
		batcher.drawSprite( pos.m_x, pos.m_y, bs.getWidth(), bs.getHeight(), m_pixmap );
	}
}
