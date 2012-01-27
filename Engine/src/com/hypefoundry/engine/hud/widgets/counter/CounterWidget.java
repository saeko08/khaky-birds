/**
 * 
 */
package com.hypefoundry.engine.hud.widgets.counter;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.hud.ClickableHudWidget;
import com.hypefoundry.engine.hud.HudWidget;
import com.hypefoundry.engine.util.serialization.DataLoader;


/**
 * A simple counter widget.
 * 
 * @author Paksas
 */
public class CounterWidget extends HudWidget implements ClickableHudWidget
{
	public static int			INCREASED = 0;
	public static int			DECREASED = 1;
	
	private int					m_value;
	private int					m_minValue;
	private int					m_maxValue;

	/**
	 * Sets new limits in the counter.
	 * 
	 * @param min
	 * @param max
	 */
	public void setLimits( int min, int max )
	{
		if ( min > max )
		{
			// invalid range specified
			return;
		}
		
		m_minValue = min;
		m_maxValue = max;
		
		if ( m_value < m_minValue )
		{
			m_value = m_minValue;
		}
		else if ( m_value > m_maxValue )
		{
			m_value = m_maxValue;
		}
	}
	
	/**
	 * Returns the current counter value.
	 * 
	 * @return
	 */
	public int getValue()
	{
		return m_value;
	}
	
	@Override
	public void onLoad( ResourceManager resMgr, DataLoader loader ) 
	{
		m_minValue = loader.getIntValue( "min" );
		m_maxValue = loader.getIntValue( "max" );
		m_value = m_minValue;
	}

	@Override
	public void onButtonPressed( int id )
	{
		if ( id == INCREASED )
		{
			if ( m_value < m_maxValue )
			{
				++m_value;
			}
		}
		else if ( id == DECREASED )
		{
			if ( m_value > m_minValue )
			{
				--m_value;
			}
		}
	}
}
