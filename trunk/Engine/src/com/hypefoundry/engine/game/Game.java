package com.hypefoundry.engine.game;

import com.hypefoundry.engine.core.*;

/**
 * The interface responsible for running the game and providing
 * the ability to query the core engine systems.
 * 
 * The game manages the game screens @see Screen.
 * 
 * @author paksas
 *
 */
public interface Game {
	public Input getInput();
	public FileIO getFileIO();
	public Graphics getGraphics();
	public Audio getAudio();
	public void setScreen(Screen screen);
	public Screen getCurrentScreen();
	public Screen getStartScreen();
}
