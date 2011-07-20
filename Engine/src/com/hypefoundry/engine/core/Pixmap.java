package com.hypefoundry.engine.core;

import com.hypefoundry.engine.core.Graphics.PixmapFormat;

/**
 * An interface that represents a bitmap we can draw in a frame buffer.
 * @author paksas
 *
 */

public interface Pixmap 
{
	/**
	 * Returns the width of a pixmap.
	 * @return
	 */
	public int getWidth();
	
	/**
	 * Returns the height of a pixmap.
	 * @return
	 */
	public int getHeight();
	
	/**
	 * Returns the pixmap format.
	 * @return
	 */
	public PixmapFormat getFormat();
	
	/**
	 * Releases the pixmap resource. 
	 */
	public void dispose();
}
