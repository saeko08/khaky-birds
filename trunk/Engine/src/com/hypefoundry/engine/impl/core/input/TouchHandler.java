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
	boolean isTouchDown( int pointer );
	
	/**
	 * Returns the X coordinate of the touching finger.
	 * 
	 * @param pointer
	 * @return
	 */
	int getTouchX( int pointer );
	
	/**
	 * Returns the Y coordinate of the touching finger.
	 * 
	 * @param pointer
	 * @return
	 */
	int getTouchY( int pointer );
	
	/**
	 * Tells how long has a finger been touching the screen ( in seconds )
	 * 
	 * @param pointer
	 * @return
	 */
	float getTouchDuriation( int pointer );
	
	/**
	 * Clears previously registered duration events.
	 */
	void clearTouchDuration();
	
	/**
	 * Returns a list of all touch-related events that happened.
	 * 
	 * @return
	 */
	List<TouchEvent> getTouchEvents();
	
	/**
	 * Clears the touch input.
	 */
	void clear();
	
	/**
	 * Update of the internal timers.
	 * 
	 * @param deltaTime
	 */
	void update( float deltaTime );
}
