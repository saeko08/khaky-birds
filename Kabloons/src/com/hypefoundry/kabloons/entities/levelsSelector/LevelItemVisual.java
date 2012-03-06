/**
 * 
 */
package com.hypefoundry.kabloons.entities.levelsSelector;

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
public class LevelItemVisual extends EntityVisual 
{
	private	LevelItem				m_levelItem;
	
	private AnimationPlayer			m_player = new AnimationPlayer();
	private int[]					m_animIndices = new int[ LevelItem.State.values().length ];
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param levelItemEntity
	 */
	public LevelItemVisual( ResourceManager resMgr, Entity levelItemEntity ) 
	{
		super( levelItemEntity );
		
		m_levelItem = (LevelItem)levelItemEntity;
		
		LevelItem.State[] itemStates = LevelItem.State.values();
		int count = itemStates.length;
		for ( int i = 0; i < count; ++i )
		{
			Animation anim = resMgr.getResource( Animation.class, itemStates[i].m_animPath );
			m_animIndices[i] = m_player.addAnimation( anim );
		}
	}

	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		Vector3 pos = m_entity.getPosition();
		BoundingBox bs = m_entity.getBoundingShape();

		m_player.select( m_animIndices[ m_levelItem.m_state.ordinal() ] );
		TextureRegion region = m_player.getTextureRegion( deltaTime );
		batcher.drawSprite( pos, bs, region );
	}

}
