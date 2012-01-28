/**
 * 
 */
package com.hypefoundry.kabloons.entities.fan;

import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.kabloons.utils.AssetsFactory;

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
	
	public enum State
	{
		On,
		Off
	}
		
	AssetsFactory				m_assetsFactory;
	Vector3						m_blowForce;
	String 						m_onAnim;
	String 						m_offAnim;
	State						m_state;
	public boolean 				m_wasCreatedByUser;
	
	/**
	 * Manual placement constructor.
	 * 
	 * @param pos
	 */
	public Fan( Vector3 pos )
	{
		m_wasCreatedByUser = true;
		m_assetsFactory = null;
		setPosition( pos );
		
		// adjust fan's Z position
		getPosition().m_z = 60;
		
		// set fan's initial state
		m_state = State.On;
	}
	
	/**
	 * Serialization constructor.
	 * 
	 * @param assetsFactory
	 */
	public Fan( AssetsFactory assetsFactory )
	{
		m_wasCreatedByUser = false;
		m_assetsFactory = assetsFactory;
		m_state = State.On;
	}

	/**
	 * Initializes the fan.
	 * 
	 * @param localBounds
	 * @param onAnim
	 * @param offAnim
	 * @param blowForce
	 */
	public void initialize( BoundingBox localBounds, String onAnim, String offAnim, Vector3 blowForce ) 
	{
		setBoundingBox( localBounds );
		
		m_onAnim = onAnim;
		m_offAnim = offAnim;
		
		m_blowForce = blowForce;
	}

	@Override
	public void onLoad( DataLoader loader ) 
	{
		Direction direction = AssetsFactory.loadFanDirection( loader );
		m_assetsFactory.initializeFan( this, direction );
		
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
}
