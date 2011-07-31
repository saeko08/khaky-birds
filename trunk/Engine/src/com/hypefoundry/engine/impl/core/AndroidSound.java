package com.hypefoundry.engine.impl.core;

import android.media.SoundPool;

import com.hypefoundry.engine.core.Sound;

/**
 * An implementation using the Android API.
 * 
 * @author paksas
 *
 */
public class AndroidSound implements Sound 
{
	private int 		m_soundId;
	private SoundPool 	m_soundPool;
	
	/**
	 * Constructor.
	 * 
	 * @param soundPool		sound pool in which the sound was created
	 * @param soundId		id of the sound in the pool
	 */
	public AndroidSound( SoundPool soundPool, int soundId ) 
	{
		this.m_soundId = soundId;
		this.m_soundPool = soundPool;
	}
	
	@Override
	public void play( float volume ) 
	{
		m_soundPool.play( m_soundId, volume, volume, 0, 0, 1 );

	}

	@Override
	public void dispose() 
	{
		m_soundPool.unload( m_soundId );
	}

}
