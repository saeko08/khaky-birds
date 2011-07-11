package com.hypefoundry.engine.impl.core;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import com.hypefoundry.engine.core.Graphics;
import com.hypefoundry.engine.core.Pixmap;


/**
 * An implementation using the Android API.
 * 
 * @author paksas
 *
 */
public class AndroidGraphics implements Graphics 
{
	AssetManager 	m_assets;
	Bitmap 			m_frameBuffer;
	Canvas 			m_canvas;
	Paint 			m_paint;
	Rect 			m_srcRect = new Rect();
	Rect 			m_dstRect = new Rect();
	
	/**
	 * Constructor.
	 * 
	 * @param assets
	 * @param frameBuffer
	 */
	public AndroidGraphics( AssetManager assets, Bitmap frameBuffer ) 
	{
		m_assets = assets;
		m_frameBuffer = frameBuffer;
		m_canvas = new Canvas( frameBuffer );
		m_paint = new Paint();
	}
	
	
	@Override
	public Pixmap newPixmap( String fileName, PixmapFormat format ) 
	{
		Config config = null;
		
		// adapt to the proper bitmap format
		if ( format == PixmapFormat.RGB565 )
		{
			config = Config.RGB_565;
		}
		else if ( format == PixmapFormat.ARGB4444 )
		{
			config = Config.ARGB_4444;
		}
		else
		{
			config = Config.ARGB_8888;
		}
		
		// load the bitmap
		Options options = new Options();
		options.inPreferredConfig = config;
		InputStream in = null;
		Bitmap bitmap = null;
		try 
		{
			in = m_assets.open( fileName );
			bitmap = BitmapFactory.decodeStream( in );
			if ( bitmap == null )
			{
				throw new RuntimeException( "Couldn't load bitmap from asset '" + fileName + "'" );
			}
		} 
		catch (IOException e) 
		{
			throw new RuntimeException( "Couldn't load bitmap from asset '" + fileName + "'" );
		} 
		finally 
		{
			// close the file stream
			if ( in != null ) 
			{
				try 
				{
					in.close();
				} 
				catch (IOException e) {}
			}
		}
		
		// readapt the format of the loaded bitmap
		if ( bitmap.getConfig() == Config.RGB_565 )
		{
			format = PixmapFormat.RGB565;
		}
		else if ( bitmap.getConfig() == Config.ARGB_4444 )
		{
			format = PixmapFormat.ARGB4444;
		}
		else
		{
			format = PixmapFormat.ARGB8888;
		}
		
		// create the pixmap implementation
		return new AndroidPixmap( bitmap, format );
	}

	@Override
	public void clear( int color ) 
	{
		m_canvas.drawRGB( ( color & 0xff0000 ) >> 16, ( color & 0xff00 ) >> 8, ( color & 0xff ) );
	}

	@Override
	public void drawPixel( int x, int y, int color ) 
	{
		m_paint.setColor( color );
		m_canvas.drawPoint( x, y, m_paint );
	}

	@Override
	public void drawLine( int x, int y, int x2, int y2, int color ) 
	{
		m_paint.setColor( color );
		m_canvas.drawLine( x, y, x2, y2, m_paint );
	}

	@Override
	public void drawRect( int x, int y, int width, int height, int color )
	{
		m_paint.setColor( color );
		m_paint.setStyle( Style.FILL );
		m_canvas.drawRect( x, y, x + width - 1, y + width - 1, m_paint );
	}

	@Override
	public void drawPixmap( Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight ) 
	{
		m_srcRect.left = srcX;
		m_srcRect.top = srcY;
		m_srcRect.right = srcX + srcWidth - 1;
		m_srcRect.bottom = srcY + srcHeight - 1;
		m_dstRect.left = x;
		m_dstRect.top = y;
		m_dstRect.right = x + srcWidth - 1;
		m_dstRect.bottom = y + srcHeight - 1;
		m_canvas.drawBitmap( ( (AndroidPixmap)pixmap ).m_bitmap, m_srcRect, m_dstRect, null );
	}

	@Override
	public void drawPixmap( Pixmap pixmap, int x, int y ) 
	{
		m_canvas.drawBitmap( ( (AndroidPixmap)pixmap ).m_bitmap, x, y, null );
	}

	@Override
	public int getWidth() 
	{
		return m_frameBuffer.getWidth();
	}

	@Override
	public int getHeight() 
	{
		return m_frameBuffer.getHeight();
	}

}
