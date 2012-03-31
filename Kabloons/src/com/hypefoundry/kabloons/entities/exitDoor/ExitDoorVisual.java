/**
 * 
 */
package com.hypefoundry.kabloons.entities.exitDoor;

import com.hypefoundry.engine.core.GLGraphics;
import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.renderer2D.fx.LightShaft;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleSystem;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleSystemPlayer;
import com.hypefoundry.engine.world.Entity;


/**
 * @author Paksas
 *
 */
public class ExitDoorVisual extends EntityVisual 
{
	private ExitDoor 				m_exitDoor;
	private TextureRegion			m_backImage;
	private TextureRegion			m_frontImage;
	private ParticleSystemPlayer	m_shaftsFxPlayer;
	
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @parma graphics
	 * @param exitDoorEntity
	 */
	public ExitDoorVisual( ResourceManager resMgr, GLGraphics graphics, Entity exitDoorEntity )
	{
		super( exitDoorEntity );
		
		m_exitDoor = (ExitDoor)exitDoorEntity;
		
		m_backImage = resMgr.getResource( TextureRegion.class, "gameplay/exitPortal/portalBackImage.xml" );
		m_frontImage = resMgr.getResource( TextureRegion.class, "gameplay/exitPortal/portalFrontImage.xml" );
		
		ParticleSystem shaftsFx = resMgr.getResource( ParticleSystem.class, "gameplay/exitPortal/lightShafts.xml" );
		m_shaftsFxPlayer = new ParticleSystemPlayer( shaftsFx, true );
		
		// create the effect for the locked door
		Vector3 pos = m_entity.getPosition();
	}
	
	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		Vector3 pos = m_entity.getPosition();
		BoundingBox bs = m_entity.getBoundingShape();
		
		// first draw the back image
		batcher.drawSprite( pos, bs, m_backImage );
		
		// then the light shafts ( if applicable )
		if ( m_exitDoor.m_state == ExitDoor.State.Open )
		{
			m_shaftsFxPlayer.draw( pos.m_x, pos.m_y, batcher, deltaTime );
		}
		
		// and then the front image
		batcher.drawSprite( pos, bs, m_frontImage );
	
	}
	
	@Override
	public void onRemoved()
	{
		if ( m_shaftsFxPlayer != null )
		{
			m_shaftsFxPlayer.release();
		}
	}
}
