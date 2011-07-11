package com.hypefoundry.engine.core;

/**
 * An interface representing a frame buffer in which
 * we can draw things.
 * 
 * @author paksas
 *
 */
public interface Graphics 
{
	
	public static enum PixmapFormat 
	{
		ARGB8888, ARGB4444, RGB565
	}
	
	/**
	 * Loads a bitmap from the specified asset file.
	 * 
	 * @param fileName
	 * @param format
	 * @return
	 */
	public Pixmap newPixmap( String fileName, PixmapFormat format );
	
	
	/**
	 * Clears the background with the specified color.
	 * @param color
	 */
	public void clear( int color );
	
	/**
	 * Draws a pixel in the specified color.
	 * 
	 * @param x
	 * @param y
	 * @param color
	 */
	public void drawPixel( int x, int y, int color );
	
	/**
	 * Draws a line in the specified color.
	 * 
	 * @param x
	 * @param y
	 * @param x2
	 * @param y2
	 * @param color
	 */
	public void drawLine( int x, int y, int x2, int y2, int color );
	
	/**
	 * Draws a filled rectangle in the specified color.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 */
	public void drawRect( int x, int y, int width, int height, int color );
	
	/**
	 * Draws the specified part of the bitmap on the specified framebuffer rectangle.
	 * This allows to introduce scaling etc.
	 * 
	 * @param pixmap
	 * @param x
	 * @param y
	 * @param srcX
	 * @param srcY
	 * @param srcWidth
	 * @param srcHeight
	 */
	public void drawPixmap( Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight );
	
	/**
	 * Draws a bitmap on the specified position in the framebuffer.
	 * 
	 * @param pixmap
	 * @param x
	 * @param y
	 */
	public void drawPixmap(Pixmap pixmap, int x, int y);
	
	/**
	 * Returns the framebuffer width.
	 * 
	 * @return
	 */
	public int getWidth();
	
	/**
	 * Returns the framebuffer height.
	 * 
	 * @return
	 */
	public int getHeight();
}

