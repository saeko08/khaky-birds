package com.hypefoundry.engine.game;

/**
 * An interface that designates an object that wishes to be updated
 * in the main game loop.
 * 
 * @author paksas
 *
 */
public interface Updatable 
{
	/**
	 * Update the object.
	 * 
	 * @param deltaTime
	 */
	void update( float deltaTime );
}
