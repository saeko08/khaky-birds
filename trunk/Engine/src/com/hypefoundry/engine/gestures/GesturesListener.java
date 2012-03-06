/**
 * 
 */
package com.hypefoundry.engine.gestures;

/**
 * Recognizable gestures listener.
 * 
 * @author Paksas
 */
public interface GesturesListener 
{
	/**
	 * Called when a gesture is recognized.
	 * 
	 * @param gesture
	 */
	void onGestureRecognized( Gesture gesture );
}
