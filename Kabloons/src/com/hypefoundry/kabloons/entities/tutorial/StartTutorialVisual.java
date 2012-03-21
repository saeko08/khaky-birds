/**
 * 
 */
package com.hypefoundry.kabloons.entities.tutorial;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.kabloons.entities.tutorial.StartTutorial.State;

/**
 * @author Paksas
 *
 */
public class StartTutorialVisual extends EntityVisual 
{

	private TextureRegion[]		m_stateImages = new TextureRegion[State.values().length];
	private StartTutorial		m_tutorial;
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param entity
	 */
	public StartTutorialVisual( ResourceManager resMgr, Entity entity ) 
	{
		super( entity );
		
		m_tutorial = (StartTutorial)entity;
		for ( int i = 0; i < m_tutorial.m_stateImagePaths.length; ++i )
		{
			if ( m_tutorial.m_stateImagePaths[i] != null && m_tutorial.m_stateImagePaths[i].length() > 0 )
			{
				m_stateImages[i] = resMgr.getResource( TextureRegion.class, m_tutorial.m_stateImagePaths[i] );
			}
		}
	}

	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{		
		int stateIdx = m_tutorial.m_state.ordinal();
		TextureRegion currRegion = m_stateImages[ stateIdx ];
		if ( currRegion == null )
		{
			return;
		}
		
		Vector3 pos = m_tutorial.m_stateImagePos[stateIdx];
		BoundingBox bs = m_tutorial.m_stateImageBounds[stateIdx];			
		batcher.drawSprite( pos, bs, currRegion );
	}

}
