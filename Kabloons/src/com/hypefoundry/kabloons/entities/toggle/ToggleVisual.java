/**
 * 
 */
package com.hypefoundry.kabloons.entities.toggle;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.animation.Animation;
import com.hypefoundry.engine.renderer2D.animation.AnimationPlayer;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public class ToggleVisual extends EntityVisual 
{

	private Toggle	 			m_toggle;
	
	private AnimationPlayer		m_player;
	private int					m_onAnim;
	private int					m_offAnim;
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param exitDoorEntity
	 */
	public ToggleVisual( ResourceManager resMgr, Entity toggleEntity )
	{
		super( toggleEntity );
		
		m_toggle = (Toggle)toggleEntity;
		
		Animation onAnim = resMgr.getResource( Animation.class, m_toggle.m_onAnimPath );
		Animation offAnim = resMgr.getResource( Animation.class, m_toggle.m_offAnimPath );
		
		m_player = new AnimationPlayer();
		m_onAnim = m_player.addAnimation( onAnim );
		m_offAnim = m_player.addAnimation( offAnim );
	}
	
	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{		
		if ( m_toggle.m_controlledEntity != null && m_toggle.m_controlledEntity.isSwitchedOn() )
		{
			m_player.select( m_onAnim );
		}
		else
		{
			m_player.select( m_offAnim );
		}
	
		Vector3 pos = m_entity.getPosition();
		BoundingBox bs = m_entity.getBoundingShape();
		batcher.drawSprite( pos, bs, m_player.getTextureRegion( deltaTime ) );
	}
}
