/**
 * 
 */
package com.hypefoundry.engine.hud;

/**
 * A widget that can receive messages from a button visual.
 * 
 * @author Paksas
 */
public interface ClickableHudWidget 
{
	/**
	 * Called when the button gets pressed.
	 * 
	 * @param id
	 */
	void onButtonPressed( int id );
}
