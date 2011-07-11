package com.hypefoundry.engine.impl.core;

import android.graphics.Bitmap;

import com.hypefoundry.engine.core.Pixmap;
import com.hypefoundry.engine.core.Graphics.PixmapFormat;


/**
 * An implementation using the Android API.
 * 
 * @author paksas
 *
 */
public class AndroidPixmap implements Pixmap 
{
	Bitmap 			m_bitmap;
	PixmapFormat 	m_format;
	

	/**
	 * Constructor.
	 * 
	 * @param bitmap
	 * @param format
	 */
	public AndroidPixmap( Bitmap bitmap, PixmapFormat format ) 
	{
		m_bitmap = bitmap;
		m_format = format;
	}
	
	@Override
	public int getWidth() 
	{
		return m_bitmap.getWidth();
	}

	@Override
	public int getHeight() 
	{
		return m_bitmap.getHeight();
	}

	@Override
	public PixmapFormat getFormat() 
	{
		return m_format;
	}

	@Override
	public void dispose() 
	{
		m_bitmap.recycle();
	}

}
