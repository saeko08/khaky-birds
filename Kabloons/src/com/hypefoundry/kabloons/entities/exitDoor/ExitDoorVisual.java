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
import com.hypefoundry.engine.renderer2D.animation.Animation;
import com.hypefoundry.engine.renderer2D.animation.AnimationPlayer;
import com.hypefoundry.engine.world.Entity;


/**
 * @author Paksas
 *
 */
public class ExitDoorVisual extends EntityVisual 
{
	private ExitDoor 			m_exitDoor;
	private AnimationPlayer		m_player;
	
	private int					m_openAnim;
	private int					m_closedAnim;
	
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
		
		m_player = new AnimationPlayer();
		
		Animation openDoorAnim = resMgr.getResource( Animation.class, m_exitDoor.m_openDoorTexturePath );
		m_openAnim = m_player.addAnimation( openDoorAnim );
		
		Animation closedDoorAnim = resMgr.getResource( Animation.class, m_exitDoor.m_closedDoorTexturePath );
		m_closedAnim = m_player.addAnimation( closedDoorAnim );
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
				m_player.select( m_openAnim );
				break;
			}
			
			case Closed:
			{
				m_player.select( m_closedAnim );
				break;
			}
		}
		
		batcher.drawSprite( pos, bs, m_player.getTextureRegion( deltaTime ) );
	}
}

// TODO: add a door switch ballons have to push first in order for the door to open
