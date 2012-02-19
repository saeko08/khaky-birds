/**
 * 
 */
package com.hypefoundry.kabloons.entities.exitDoor;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.renderer2D.Color;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.Spline;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.animation.Animation;
import com.hypefoundry.engine.renderer2D.animation.AnimationPlayer;
import com.hypefoundry.engine.renderer2D.fx.LightningFX;
import com.hypefoundry.engine.world.Entity;


/**
 * @author Paksas
 *
 */
public class ExitDoorVisual extends EntityVisual 
{
	private ExitDoor 				m_exitDoor;
	private AnimationPlayer			m_player;
	
	private Spline					m_lightningPoints[] 		= new Spline[8];
	private LightningFX[]			m_closedDoorLightnings 		= new LightningFX[8];
	
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
		
		Animation openDoorAnim = resMgr.getResource( Animation.class, m_exitDoor.m_openAnim );
		m_player.addAnimation( openDoorAnim );
		
		// create the effect for the locked door
		Vector3 pos = m_entity.getPosition();
		
		// create the points the lightning will connect
		{
			m_lightningPoints[0] = new Spline();
			m_lightningPoints[0].addPoint( pos );
			m_lightningPoints[0].addPoint( ( new Vector3( 0.46f, -0.45f, 0 ) ).add( pos ) );
			
			m_lightningPoints[1] = new Spline();
			m_lightningPoints[1].addPoint( pos );
			m_lightningPoints[1].addPoint( ( new Vector3( -0.42f, -0.45f, 0 ) ).add( pos ) );
			
			m_lightningPoints[2] = new Spline();
			m_lightningPoints[2].addPoint( pos );
			m_lightningPoints[2].addPoint( ( new Vector3( -1.16f, -0.26f, 0 ) ).add( pos ) );
			
			m_lightningPoints[3] = new Spline();
			m_lightningPoints[3].addPoint( pos );
			m_lightningPoints[3].addPoint( ( new Vector3( -1.21f, 0.23f, 0 ) ).add( pos ) );
			
			m_lightningPoints[4] = new Spline();
			m_lightningPoints[4].addPoint( pos );
			m_lightningPoints[4].addPoint( ( new Vector3( -0.45f, 0.43f, 0 ) ).add( pos ) );
			
			m_lightningPoints[5] = new Spline();
			m_lightningPoints[5].addPoint( pos );
			m_lightningPoints[5].addPoint( ( new Vector3( 0.41f, 0.43f, 0 ) ).add( pos ) );
			
			m_lightningPoints[6] = new Spline();
			m_lightningPoints[6].addPoint( pos );
			m_lightningPoints[6].addPoint( ( new Vector3( 1.23f, 0.23f, 0 ) ).add( pos ) );
			
			m_lightningPoints[7] = new Spline();
			m_lightningPoints[7].addPoint( pos );
			m_lightningPoints[7].addPoint( ( new Vector3( 1.13f, -0.26f, 0 ) ).add( pos ) );
		}

		for ( int i = 0; i < m_closedDoorLightnings.length; ++i )
		{
			// create the lightning
			m_closedDoorLightnings[i] = new LightningFX( m_lightningPoints[i], m_lightningPoints[i].m_lengths[0], 0.1f, 10, Color.RED );
		}
	}
	
	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		Vector3 pos = m_entity.getPosition();
		BoundingBox bs = m_entity.getBoundingShape();
		batcher.drawSprite( pos, bs, m_player.getTextureRegion( deltaTime ) );
		
		if ( m_exitDoor.m_state == ExitDoor.State.Closed )
		{
			// draw the effects for the closed door
			for ( int i = 0; i < m_closedDoorLightnings.length; ++i )
			{
				m_closedDoorLightnings[i].draw( batcher, 0 );
			}
		}
	
	}
}

// TODO: add a door switch ballons have to push first in order for the door to open
