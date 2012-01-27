/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.hunter;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.world.Entity;

/**
 * @author azagor
 *
 */
public class BulletVisual  extends EntityVisual
{
	private TextureRegion 	m_pixmap;
	private	Vector3 		m_velocity 		 = new Vector3();
	private	DynamicObject	m_dynObj;
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param entity
	 */
	public BulletVisual( ResourceManager resMgr, Entity entity ) 
	{
		super( entity );

		m_pixmap = resMgr.getResource( TextureRegion.class, "textures/bullet.xml" );
		
		m_dynObj = entity.query( DynamicObject.class );
		
		m_velocity.set( m_dynObj.m_linearSpeed, 0, 0 );
		m_velocity.rotateZ( m_entity.getFacing() );
	}

	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		Vector3 pos = m_entity.getPosition();
		BoundingBox bs = m_entity.getBoundingShape();
		
		batcher.drawSprite( pos, bs, m_pixmap );
		
		// set the flight speed
		m_dynObj.m_velocity.set( m_velocity );
	}
}

