package com.hypefoundry.engine.impl.core.input;

import java.util.List;

import com.hypefoundry.engine.core.Input.TouchEvent;

import android.view.View.OnTouchListener;


/**
 * A common interface for handling the touch events. 
 * 
 * @author paksas
 *
 */
public interface TouchHandler extends OnTouchListener 
{
	/**
	 * Checks if the specified finger is pressed against the screen.
	 * 
	 * @param pointer
	 * @return
	 */
	public boolean isTouchDown( int pointer );
	
	/**
	 * Returns the X coordinate of the touching finger.
	 * 
	 * @param pointer
	 * @return
	 */
	public int getTouchX( int pointer );
	
	/**
	 * Returns the Y coordinate of the touching finger.
	 * 
	 * @param pointer
	 * @return
	 */
	public int getTouchY( int pointer );
	
	/**
	 * Returns a list of all touch-related events that happened.
	 * 
	 * @return
	 */
	public List<TouchEvent> getTouchEvents();
}
