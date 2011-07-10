package com.hypefoundry.engine.game;

/**
 * This abstract class represents a single screen of the running game.
 * 
 * Example of a screen would be the main menu screen, the options screen, 
 * the main game screen etc.
 *  
 * @author paksas
 *
 */
public abstract class Screen {
	
	/// Host game instance
	protected final Game 	m_game;
	
	public Screen(Game game) {
		this.m_game = game;
	}
	
	/**
	 * Update the state of the screen.
	 * 
	 * @param deltaTime
	 */
	public abstract void update(float deltaTime);
	
	/**
	 * Draw the screen in the frame buffer.
	 * 
	 * @param deltaTime
	 */
	public abstract void present(float deltaTime);
	
	/**
	 * Called when the screen's functionality should be paused.
	 */
	public abstract void pause();
	
	/**
	 * Called when the screen's functionality should resume 
	 * after it's been paused.
	 */
	public abstract void resume();
	
	/**
	 * Called when the screen is about to be closed. 
	 * This is the place where you want to clean up after the screen.
	 */
	public abstract void dispose();
}
