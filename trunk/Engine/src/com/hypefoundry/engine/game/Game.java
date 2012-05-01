package com.hypefoundry.engine.game;

import com.hypefoundry.engine.core.*;


/** Main game interface, providing the ability to query the core engine systems.
 * 
 * The game manages the game screens @see Screen.
 * 
 * @author paksas
 *
 */
public interface Game 
{
	/**
	 * Returns an instance of the input manager.
	 * @return
	 */
	public Input getInput();
	
	/**
	 * Returns an instance of the filesystem manager. 
	 * @return
	 */
	public FileIO getFileIO();
	
	/**
	 * Returns an instance of the graphics system.
	 * @return
	 */
	public GLGraphics getGraphics();
	
	/**
	 * Returns an instance of the sound system. 
	 * @return
	 */
	public Audio getAudio();
	
	/**
	 * Sets a new active screen.
	 * @param screen
	 */
	public void setScreen( Screen screen );
	
	/**
	 * Returns the currently running screen.
	 * @return
	 */
	public Screen getCurrentScreen();
	
	/**
	 * Returns the startup screen. 
	 * 
	 * Implement this in order to set your startup screen, and then enclose the entire 
	 * screen management logics within the screens themselves.
	 * @return
	 */
	public Screen getStartScreen();
	
	/**
	 * Closes the game application.
	 */
	public void closeGame();

	/**
	 * Called when a back button is pressed.
	 */
	public void onBackPressed();
}
