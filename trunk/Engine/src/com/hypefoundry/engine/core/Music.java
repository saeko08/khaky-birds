package com.hypefoundry.engine.core;

/**
 * An interface responsible for playing the music.
 * 
 * @author paksas
 *
 */
public interface Music {
	public void play();
	public void stop();
	public void pause();
	public void setLooping(boolean looping);
	public void setVolume(float volume);
	public boolean isPlaying();
	public boolean isStopped();
	public boolean isLooping();
	public void dispose();
}
