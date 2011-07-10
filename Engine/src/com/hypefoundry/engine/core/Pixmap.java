package com.hypefoundry.engine.core;

import com.hypefoundry.engine.core.Graphics.PixmapFormat;

/**
 * An interface that represents a bitmap we can draw in a frame buffer.
 * @author paksas
 *
 */

public interface Pixmap {
	public int getWidth();
	public int getHeight();
	public PixmapFormat getFormat();
	public void dispose();
}
