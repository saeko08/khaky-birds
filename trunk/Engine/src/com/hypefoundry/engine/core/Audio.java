package com.hypefoundry.engine.core;


/**
 * Interface for instantiating music and sound assets.
 * 
 * @author paksas
 *
 */
public interface Audio {
	
	public Music newMusic(String filename);
	
	public Sound newSound(String filename);
	
}

