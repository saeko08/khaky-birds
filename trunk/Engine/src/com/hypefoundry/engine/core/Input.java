package com.hypefoundry.engine.core;

import java.util.List;

/**
 * Interface responsible for handling user input.
 * 
 * @author paksas
 */
public interface Input {
	
	public static class KeyEvent {
		public static final int KEY_DOWN = 0;
		public static final int KEY_UP = 1;
		public int type;
		public int keyCode;
		public char keyChar;
	}
	
	public static class TouchEvent {
		public static final int TOUCH_DOWN = 0;
		public static final int TOUCH_UP = 1;
		public static final int TOUCH_DRAGGED = 2;
		public static final int TOUCH_DOUBLE_TAP = 3;
		public int type;
		public int x, y;
		public int pointer;
	}
	
	/**
	 * Checks if the specified key is pressed.
	 * 
	 * @param keyCode
	 * @return
	 */
	public boolean isKeyPressed(int keyCode);
	
	/**
	 * Checks if the specified finger is pressed against the screen.
	 * 
	 * @param pointer
	 * @return
	 */
	public boolean isTouchDown(int pointer);
	
	/**
	 * Returns the X coordinate of the touching finger.
	 * 
	 * @param pointer
	 * @return
	 */
	public int getTouchX(int pointer);
	
	/**
	 * Returns the Y coordinate of the touching finger.
	 * 
	 * @param pointer
	 * @return
	 */
	public int getTouchY(int pointer);
	
	/**
	 * Tells how long has a finger been touching the screen ( in seconds )
	 * 
	 * @param pointer
	 * @return
	 */
	public float getTouchDuriation( int pointer );
	
	/**
	 * Clears previously registered duration events.
	 */
	public void clearTouchDuration();
	
	/**
	 * Returns the X axis factor of the accelerometer.
	 *  
	 * @return
	 */
	public float getAccelX();
	
	/**
	 * Returns the Y axis factor of the accelerometer.
	 *  
	 * @return
	 */
	public float getAccelY();
	
	/**
	 * Returns the Z axis factor of the accelerometer.
	 *  
	 * @return
	 */
	public float getAccelZ();
	
	/**
	 * Returns a list of all key-related events that happened.
	 * 
	 * @return
	 */
	public List<KeyEvent> getKeyEvents();
	
	/**
	 * Returns a list of all touch-related events that happened.
	 * 
	 * @return
	 */
	public List<TouchEvent> getTouchEvents();
	
	/**
	 * Clears current input data.
	 */
	public void clear();
}
