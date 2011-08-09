/**
 * 
 */
package com.hypefoundry.engine.util;

import android.util.Log;

/**
 * A helper tool for measuring the application's framerate.
 * 
 * @author paksas
 *
 */
public class FPSCounter 
{
	private long 			m_startTime = System.nanoTime();
	private int 			m_frames = 0;
	private StringBuilder	m_str = new StringBuilder();
	
	/**
	 * Logs the framerate to the CatLog.
	 */
	public void logFrame() 
	{
		m_frames++;
		if( System.nanoTime() - m_startTime >= 1000000000 ) 
		{
			m_str.delete( 0, m_str.length() );
			m_str.append( "fps: " );
			m_str.append( m_frames );
			Log.d( "FPSCounter", m_str.toString() );
			
			m_frames = 0;
			m_startTime = System.nanoTime();
		}
	}
}
