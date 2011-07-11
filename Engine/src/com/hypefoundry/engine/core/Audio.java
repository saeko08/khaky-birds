package com.hypefoundry.engine.core;


/**
 * Interface for instantiating music and sound assets.
 * 
 * @author paksas
 *
 */
public interface Audio 
{
	
	/**
	 * Instantiate a streamed music asset.
	 * 
	 * @param filename		path to the asset relative to the root 'assets' dir 
	 * @return
	 */
	public Music newMusic( String filename );
	
	/**
	 * Instantiate a sound asset.
	 * 
	 * @param filename		path to the asset relative to the root 'assets' dir 
	 * @return
	 */
	public Sound newSound( String filename );
	
}

