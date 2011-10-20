/**
 * 
 */
package com.hypefoundry.engine.hud;

import javax.microedition.khronos.opengles.GL10;

import com.hypefoundry.engine.core.GLGraphics;
import com.hypefoundry.engine.game.Game;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;

/**
 * Renders a HUD layout.
 * @author Paksas
 *
 */
public class HudRenderer 
{
	private final int			MAX_SPRITES = 512;			// TODO: config
	private final float 		VIEWPORT_WIDTH = 480;		// TODO: config
	private final float 		VIEWPORT_HEIGHT = 960;		// TODO: config
	
	private HudLayout			m_layout;
	private GLGraphics 			m_graphics;
	private SpriteBatcher		m_batcher;
	
	/**
	 * Constructor.
	 * 
	 * @param game
	 */
	public HudRenderer( Game game )
	{
		m_graphics = game.getGraphics();
		m_batcher = new SpriteBatcher( m_graphics, MAX_SPRITES );
	}
	
	/**
	 * Sets a layout to render.
	 * 
	 * @param layout
	 */
	public void setLayout( HudLayout layout )
	{
		m_layout = layout;
	}
	
	/**
	 * Renders the currently set layout.
	 * 
	 * @param deltaTime
	 */
	public void draw( float deltaTime )
	{
		if ( m_layout == null )
		{
			return;
		}
		
		setRenderingMatrices();
		
		// render the layout
		m_layout.draw( m_batcher, deltaTime );
		m_batcher.flush();
	}
	
	/**
	 * Sets rendering matrices.
	 */
	private void setRenderingMatrices()
	{
		// set the viewport
		GL10 gl = m_graphics.getGL();
				
		gl.glViewport( 0, 0, m_graphics.getWidth(), m_graphics.getHeight() );
		gl.glMatrixMode( GL10.GL_PROJECTION );
		gl.glLoadIdentity();
		gl.glOrthof( 0, VIEWPORT_WIDTH, 0, VIEWPORT_HEIGHT, 1, -1 );
				
		gl.glMatrixMode( GL10.GL_MODELVIEW );
		gl.glLoadIdentity();
	}
}
