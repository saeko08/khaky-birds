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
	private ExitDoor 			m_exitDoor;
	private TextureRegion		m_openDoorTexture;
	private TextureRegion		m_closedDoorTexture;
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param exitDoorEntity
	 */
	public ExitDoorVisual( ResourceManager resMgr, Entity exitDoorEntity )
	{
		super( exitDoorEntity );
		
		m_exitDoor = (ExitDoor)exitDoorEntity;
		
		m_openDoorTexture = resMgr.getResource( TextureRegion.class, m_exitDoor.m_openDoorTexturePath );
		m_closedDoorTexture = resMgr.getResource( TextureRegion.class, m_exitDoor.m_closedDoorTexturePath );
	}
	
	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		Vector3 pos = m_entity.getPosition();
		BoundingBox bs = m_entity.getBoundingShape();
		
		switch( m_exitDoor.m_state )
		{
			case Open:
			{
				batcher.drawSprite( pos, bs, m_openDoorTexture );
				break;
			}
			
			case Closed:
			{
				batcher.drawSprite( pos, bs, m_closedDoorTexture );
				break;
			}
		}
	}
}

// TODO: add a door switch ballons have to push first in order for the door to open
