/**
 * 
 */
package com.hypefoundry.kabloons.entities.buzzSaw;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.MathLib;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleSystem;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleSystemPlayer;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public class BuzzSawVisual extends EntityVisual 
{
	private BuzzSaw					m_buzzSaw;
	private TextureRegion			m_stoppedImage;
	private TextureRegion			m_runningImage;
	private ParticleSystemPlayer	m_fxPlayer;
	
	private float					m_angularSpeed;
	private final float				m_acceleration = 45.0f;
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param entity
	 */
	public BuzzSawVisual( ResourceManager resMgr,  Entity buzzSawEntity ) 
	{
		super( buzzSawEntity );
		
		m_buzzSaw = (BuzzSaw)buzzSawEntity;
		
		// animation
		m_stoppedImage = resMgr.getResource( TextureRegion.class, m_buzzSaw.m_stoppedImagePath );
		m_runningImage = resMgr.getResource( TextureRegion.class, m_buzzSaw.m_runningImagePath );
		
		// effect
		if ( m_buzzSaw.m_fxPath.length() > 0 )
		{
			ParticleSystem fx = resMgr.getResource( ParticleSystem.class, m_buzzSaw.m_fxPath );
			m_fxPlayer = new ParticleSystemPlayer( fx, true );
		}
		
		m_angularSpeed = 0.0f;
	}

	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime )
	{
		Vector3 pos = m_entity.getPosition();
		BoundingBox bs = m_entity.getBoundingShape();
				
		switch( m_buzzSaw.m_state )
		{
			case Running:
			{
				// increase the speed
				m_angularSpeed += m_acceleration * deltaTime;
				
				if ( m_fxPlayer != null )
				{
					m_fxPlayer.enableEmitters( true );
				}
				break;
			}
			
			case SwitchedOff:
			{
				// decrease the speed
				m_angularSpeed -= m_acceleration * deltaTime;
				
				if ( m_fxPlayer != null )
				{
					m_fxPlayer.enableEmitters( false );
				}
				break;
			}
		}
		m_angularSpeed = MathLib.clamp( m_angularSpeed, 0.0f, 360.0f );
		
		// rotate the saw
		float facing = m_buzzSaw.getFacing() + m_angularSpeed * m_buzzSaw.m_rotationDir * deltaTime;
		m_buzzSaw.setFacing( facing );
		
		// select the saw image based on its rotation speed
		TextureRegion currImage = null;
		if ( m_angularSpeed > 90.0f )
		{
			currImage = m_runningImage;
		}
		else
		{
			currImage = m_stoppedImage;
		}
		
		// draw the saws
		batcher.drawSprite( pos, bs, facing, currImage );
		
		// draw the sparks
		if ( m_fxPlayer != null )
		{
			m_fxPlayer.draw( pos.m_x, pos.m_y, batcher, deltaTime );
		}

	}

}
