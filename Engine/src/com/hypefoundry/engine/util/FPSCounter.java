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
	private long 		m_startTime = System.nanoTime();
	private int 		m_frames = 0;
	
	/**
	 * Logs the framerate to the CatLog.
	 */
	public void logFrame() 
	{
		m_frames++;
		if( System.nanoTime() - m_startTime >= 1000000000 ) 
		{
			Log.d("FPSCounter", "fps: " + m_frames);
			
			m_frames = 0;
			m_startTime = System.nanoTime();
		}
	}
}
