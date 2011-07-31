package com.hypefoundry.engine.impl.core;


import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.hypefoundry.engine.core.Music;

/**
 * An implementation using the Android API.
 * 
 * @author paksas
 *
 */
public class AndroidMusic implements Music, OnCompletionListener
{
	private MediaPlayer 		m_mediaPlayer;
	private boolean 			m_isPrepared = false;
	
	/**
	 * Constructor.
	 * 
	 * @param assetDescriptor	descriptor of the music asset.
	 */
	public AndroidMusic( AssetFileDescriptor assetDescriptor ) 
	{
		m_mediaPlayer = new MediaPlayer();
		try 
		{
			m_mediaPlayer.setDataSource( assetDescriptor.getFileDescriptor(), assetDescriptor.getStartOffset(), assetDescriptor.getLength() );
			m_mediaPlayer.prepare();
			m_isPrepared = true;
			m_mediaPlayer.setOnCompletionListener( this );
		} 
		catch (Exception e) 
		{
			throw new RuntimeException( "Couldn't load music" );
		}
	}
	
	@Override
	public void play() 
	{
		if ( m_mediaPlayer.isPlaying() )
		{
			return;
		}
		
		try 
		{
			synchronized ( this ) 
			{
				if ( !m_isPrepared )
				{
					m_mediaPlayer.prepare();
				}
				m_mediaPlayer.start();
			}
		} 
		catch (IllegalStateException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void stop() 
	{
		m_mediaPlayer.stop();
		synchronized ( this ) 
		{
			m_isPrepared = false;
		}
	}

	@Override
	public void pause() 
	{
		m_mediaPlayer.pause();
	}

	@Override
	public void setLooping( boolean looping ) 
	{
		m_mediaPlayer.setLooping( looping );
	}

	@Override
	public void setVolume(float volume) 
	{
		m_mediaPlayer.setVolume( volume, volume );
	}

	@Override
	public boolean isPlaying() 
	{
		return m_mediaPlayer.isPlaying();
	}

	@Override
	public boolean isStopped() 
	{
		return !m_isPrepared;
	}

	@Override
	public boolean isLooping() 
	{
		return m_mediaPlayer.isLooping();
	}

	@Override
	public void dispose() 
	{
		if ( m_mediaPlayer.isPlaying() )
		{
			m_mediaPlayer.stop();
		}
		m_mediaPlayer.release();
	}
	
	@Override
	public void onCompletion( MediaPlayer player ) 
	{
		synchronized ( this ) 
		{
			m_isPrepared = false;
		}
	}
}
