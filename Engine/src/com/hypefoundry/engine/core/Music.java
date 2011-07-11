package com.hypefoundry.engine.core;

/**
 * An interface responsible for playing the music.
 * 
 * @author paksas
 *
 */
public interface Music 
{
	/**
	 * Starts playing the music.
	 */
	public void play();
	
	/**
	 * Stops playing the music.
	 */
	public void stop();
	
	/**
	 * Pauses the music playback.
	 */
	public void pause();
	
	/**
	 * Defines whether the music should play continuously, or stop
	 * once the entire clip is played.
	 * 
	 * @param looping
	 */
	public void setLooping( boolean looping );
	
	/**
	 * Sets the music volume.
	 * 
	 * @param volume		range 0..1
	 */
	public void setVolume( float volume );
	
	/**
	 * Tells whether the music is playing at the moment.
	 * 
	 * @return is the music playing?
	 */
	public boolean isPlaying();
	
	/**
	 * Tells whether the music is stopped at the moment.
	 * 
	 * @return is the music stopped?
	 */
	public boolean isStopped();
	
	/**
	 * Tells if the music is set to played in a looped mode.
	 * 
	 * @return is the music looped?
	 */
	public boolean isLooping();
	
	/**
	 * Disposes of the music resource, freeing up the memory it used.
	 */
	public void dispose();
}
