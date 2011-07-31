package com.hypefoundry.engine.impl.core;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import com.hypefoundry.engine.core.Audio;
import com.hypefoundry.engine.core.Music;
import com.hypefoundry.engine.core.Sound;

/**
 * An implementation using the Android API.
 * 
 * @author paksas
 *
 */
public class AndroidAudio implements Audio 
{
	AssetManager 	m_assets;
	SoundPool 		m_soundPool;
	
	public AndroidAudio( Activity activity ) 
	{
		activity.setVolumeControlStream( AudioManager.STREAM_MUSIC );
		m_assets = activity.getAssets();
		m_soundPool = new SoundPool( 20, AudioManager.STREAM_MUSIC, 0 );
	}
	
	@Override
	public Music newMusic( String filename ) 
	{
		try 
		{
			AssetFileDescriptor assetDescriptor = m_assets.openFd( filename );
			return new AndroidMusic( assetDescriptor );
		} 
		catch (IOException e) 
		{
			throw new RuntimeException( "Couldn't load music '" + filename + "'" );
		}
	}

	@Override
	public Sound newSound( String filename ) 
	{
		try 
		{
			AssetFileDescriptor assetDescriptor = m_assets.openFd(filename);
			int soundId = m_soundPool.load( assetDescriptor, 0 );
			return new AndroidSound( m_soundPool, soundId );
		} 
		catch (IOException e) 
		{
			throw new RuntimeException( "Couldn't load sound '" + filename + "'" );
		}
	}

}
