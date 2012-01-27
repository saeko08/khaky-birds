/**
 * 
 */
package com.hypefoundry.kabloons.entities.exitDoor;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public class ExitDoorVisual extends EntityVisual 
{
	private TextureRegion		m_pixmap;
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param exitDoorEntity
	 */
	public ExitDoorVisual( ResourceManager resMgr, Entity exitDoorEntity )
	{
		super( exitDoorEntity );
		
		ExitDoor exitDoor = (ExitDoor)exitDoorEntity;
		
		m_pixmap = resMgr.getResource( TextureRegion.class, exitDoor.m_path );
	}
	
	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		Vector3 pos = m_entity.getPosition();
		BoundingBox bs = m_entity.getBoundingShape();
		
		batcher.drawSprite( pos, bs, m_pixmap );
	}
}

// TODO: add a door switch ballons have to push first in order for the door to open
