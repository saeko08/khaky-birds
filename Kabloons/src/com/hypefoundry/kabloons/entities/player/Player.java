/**
 * 
 */
package com.hypefoundry.kabloons.entities.player;

import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.kabloons.entities.fan.Fan;

/**
 * @author Paksas
 *
 */
public class Player extends Entity 
{
	int[]						m_fansLeft 					= new int[Fan.Direction.values().length];
	boolean						m_ghostReleaseEnabled		= true;
	boolean						m_fansRemovalEnabled		= true;
	PlayerListener				m_listener					= null;
	
	/**
	 * Attaches a player listener.
	 * 
	 * @param listener
	 */
	void attachListener( PlayerListener listener )
	{
		m_listener = listener;
	}
	
	/**
	 * Detaches a player listener.
	 * 
	 * @param listener
	 */
	void detachListener( PlayerListener listener )
	{
		m_listener = null;
	}
	
	@Override
	public void onLoad( DataLoader loader ) 
	{
		m_fansLeft[Fan.Direction.Left.m_idx] = loader.getIntValue( "leftFans", 0 );
		m_fansLeft[Fan.Direction.Right.m_idx] = loader.getIntValue( "rightFans", 0 );
	}
	
	/**
	 * Sets the number of fans available to the user.
	 * 
	 * @param left
	 * @param right
	 */
	public void setFansCount( int left, int right )
	{
		m_fansLeft[Fan.Direction.Left.m_idx] = left;
		m_fansLeft[Fan.Direction.Right.m_idx] = right;
		
		if ( m_listener != null )
		{
			m_listener.onFansCountChanged();
		}
	}
	
	/**
	 * Changes the number of fans by the specified amounts.
	 * 
	 * @param dLeft
	 * @param dRight
	 */
	public void changeFansCount( int dLeft, int dRight ) 
	{
		m_fansLeft[Fan.Direction.Left.m_idx] += dLeft;
		m_fansLeft[Fan.Direction.Right.m_idx] += dRight;
		
		if ( m_listener != null )
		{
			m_listener.onFansCountChanged();
		}
	}
	
	/**
	 * Toggles the ghost release mechanism
	 * 
	 * @param enable
	 */
	public void enableGhostRelease( boolean enable )
	{
		m_ghostReleaseEnabled = enable;
	}

	/**
	 * Toggles the ability to remove fans.
	 * 
	 * @param enable
	 */
	public void enableFanRemoval( boolean enable ) 
	{
		m_fansRemovalEnabled = enable;
		
	}
}
