/**
 * 
 */
package com.hypefoundry.engine.hud.widgets;

/**
 * STate the button is in.
 * 
 * @author Paksas
 */
public enum ButtonState 
{
	RELEASED(0),
	HIGHLIGHTED(1),
	PRESSED(2);
	
	public int			m_value;
	
	/**
	 * Constructor.
	 * 
	 * @param val
	 */
	private ButtonState( int val )
	{
		m_value = val;
	}
}
