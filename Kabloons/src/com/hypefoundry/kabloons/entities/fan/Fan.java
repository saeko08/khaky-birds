/**
 * 
 */
package com.hypefoundry.kabloons.entities.fan;

import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.BoundingShape;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public class Fan extends Entity 
{
	// Fan direction
	public enum Direction
	{
		None( 0 ),
		Left( 1 ),
		Right( 2 );
		
		public int m_idx;
		Direction( int idx )
		{
			m_idx = idx;
		}
	}
		
	Vector3						m_blowForce;
	BoundingBox					m_windFieldBoundsWorldSpace = new BoundingBox();
	String 						m_anim;
	String 						m_windFx;
	public boolean 				m_wasCreatedByUser;
	
	/**
	 * Manual placement constructor.
	 * 
	 * @param pos
	 */
	public Fan( Vector3 pos )
	{
		m_wasCreatedByUser = true;
		setPosition( pos );
		
		// adjust fan's Z position
		getPosition().m_z = 60;
		
	}
	
	/**
	 * Serialization constructor.
	 * 
	 * @param assetsFactory
	 */
	public Fan()
	{
		m_wasCreatedByUser = false;
	}

	/**
	 * Initializes the fan.
	 * 
	 * @param localBounds
	 * @param windFieldBoundsLocalSpace
	 * @param anim
	 * @param windFx
	 * @param blowForce
	 */
	public void initialize( BoundingBox localBounds, BoundingBox windFieldBoundsLocalSpace, String anim, String windFx, Vector3 blowForce ) 
	{
		setBoundingBox( localBounds );
		
		m_anim = anim;
		m_windFx = windFx;
		
		m_blowForce = blowForce;
		
		m_windFieldBoundsWorldSpace.set( windFieldBoundsLocalSpace );
		m_windFieldBoundsWorldSpace.translate( getPosition() );
	}

	@Override
	public void onLoad( DataLoader loader ) 
	{
		BoundingBox windFieldBoundsLocalSpace = new BoundingBox();
		windFieldBoundsLocalSpace.load( "windFieldBounds", loader );
		
		m_windFieldBoundsWorldSpace.set( windFieldBoundsLocalSpace );
		m_windFieldBoundsWorldSpace.translate( getPosition() );
		
		m_anim = loader.getStringValue( "anim" );
		m_windFx = loader.getStringValue( "windFx" );
		
		m_blowForce = new Vector3();
		m_blowForce.load( "BlowForce", loader );
		
		// adjust fan's Z position
		getPosition().m_z = 60;
	}

	/**
	 * Returns id of the direction in which the fan blows.
	 * 
	 * @return
	 */
	public Direction getBlowDirection() 
	{
		if ( m_blowForce.m_x < 0 )
		{
			return Direction.Left;
		}
		else
		{
			return Direction.Right;
		}
	}

	/**
	 * Returns a bounding box that embraces the wind field ( in world space coordinates ).
	 *  
	 * @return
	 */
	public BoundingShape getWindBounds() 
	{
		return m_windFieldBoundsWorldSpace;
	}
}
