/**
 * 
 */
package com.hypefoundry.engine.game;

import com.hypefoundry.engine.core.Input;

/**
 * User input handler.
 * 
 * @author Paksas
 */
public interface InputHandler 
{
	/**
	 * Implementation of this method should handle user's input, returning 'true'
	 * if it doesn't want any other handlers to process this input, or 'false'
	 * if other handlers should also have a chance to handle it.
	 * 
	 * @param input
	 * @param deltaTime		time elapsed since the last call to this method
	 */
	boolean handleInput( Input input, float deltaTime  );

}
