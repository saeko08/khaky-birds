/**
 * 
 */
package com.hypefoundry.engine.impl.game;

/**
 * A high level operation the game is currently performing.
 * 
 * @author Paksas
 *
 */
interface GameOperation 
{
	/**
	 * Updates the state of the operation.
	 * 
	 * @param deltaTime
	 * @param game
	 */
	void update( float deltaTime, GLGame game );
}
