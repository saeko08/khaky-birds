/**
 * 
 */
package com.hypefoundry.engine.hud;

import java.util.*;

import javax.microedition.khronos.opengles.GL10;

import com.hypefoundry.engine.core.GLGraphics;
import com.hypefoundry.engine.core.Input;
import com.hypefoundry.engine.game.Game;
import com.hypefoundry.engine.game.InputHandler;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;

/**
 * Renders a HUD layout.
 * @author Paksas
 *
 */
public class HudRenderer implements InputHandler
{
	private final int					MAX_SPRITES = 512;			// TODO: config
	
	public GLGraphics 					m_graphics;
	private Input						m_input;
	private SpriteBatcher				m_batcher;
	private Hud 						m_hud; 
	
	private List< HudWidgetVisual >		m_visuals = new ArrayList< HudWidgetVisual >();
	private boolean						m_contentsInvalidated = false;
	
	/**
	 * Constructor.
	 * 
	 * @param game
	 * @param hud
	 */
	public HudRenderer( Game game, Hud hud )
	{
		m_graphics = game.getGraphics();
		m_input = game.getInput();
		m_hud = hud;
		m_batcher = new SpriteBatcher( m_graphics, MAX_SPRITES );
	}
	
	/**
	 * Called when a layout is loaded.
	 * 
	 * @param widgets
	 */
	public void onLayoutLoaded( List< HudWidget > widgets )
	{		
		m_visuals.clear();
		m_contentsInvalidated = true;
		
		if ( m_hud != null )
		{
			int count = widgets.size();
			for ( int i = 0; i < count; ++i )
			{
				HudWidget widget = widgets.get( i );
				
				HudWidgetVisual visual = m_hud.createVisual( this, widget );
				if ( visual != null )
				{
					// find the parent for the visual
					if ( widget.m_parent != null )
					{
						HudWidgetVisual parentVisual = findVisualFor( widget.m_parent );
						visual.setParent( parentVisual );
					}
					
					m_visuals.add( visual );
				}
			}
		}
	}
	
	/**
	 * Finds a visual that corresponds to the specified widget.
	 * 
	 * @param widget
	 * @return
	 */
	private HudWidgetVisual findVisualFor( HudWidget widget ) 
	{
		int count = m_visuals.size();
		for ( int i = 0; i < count; ++i )
		{
			HudWidgetVisual visual = m_visuals.get(i);
			if ( visual.m_widget == widget )
			{
				return visual;
			}
		}
		
		return null;
	}

	/**
	 * Called when the layout is released.
	 */
	public void onLayoutReleased()
	{
		m_visuals.clear();
		m_contentsInvalidated = true;
	}
	
	/**
	 * Renders the currently set layout.
	 * 
	 * @param deltaTime
	 */
	public void draw( float deltaTime )
	{
		int count = m_visuals.size();
		if ( count <= 0 )
		{
			return;
		}
		
		// clear temp render buffers and set the camera matrices
		GL10 gl = m_graphics.getGL();
		gl.glClear( GL10.GL_STENCIL_BUFFER_BIT );
		setRenderingMatrices();
		
		// render the layout
		count = m_visuals.size();
		for ( int i = 0; i < count; ++i )
		{
			HudWidgetVisual visual = m_visuals.get(i);
			if ( visual != null && visual.m_widget.m_isVisible )
			{
				visual.draw( m_batcher, deltaTime );
			}
		}
		m_batcher.flush();
	}
	
	/**
	 * Sets rendering matrices.
	 */
	private void setRenderingMatrices()
	{
		// set the viewport
		GL10 gl = m_graphics.getGL();
				
		int viewportWidth = m_graphics.getWidth();
		int viewportHeight = m_graphics.getHeight();
		
		gl.glViewport( 0, 0, viewportWidth, viewportHeight );
		gl.glMatrixMode( GL10.GL_PROJECTION );
		gl.glLoadIdentity();
		gl.glOrthof( 0, 1, 1, 0, 1, -1 );
				
		gl.glMatrixMode( GL10.GL_MODELVIEW );
		gl.glLoadIdentity();
	}
	
	// ------------------------------------------------------------------------
	// Input handling
	// ------------------------------------------------------------------------
		
	@Override
	public boolean handleInput( Input input, float deltaTime )
	{
		boolean inputHandled = false;
		
		int count = m_visuals.size();
		for ( int i = 0; i < count; ++i )
		{
			HudWidgetVisual visual = m_visuals.get(i);
			if ( visual != null )
			{
				boolean wasInputHandled = visual.handleInput( m_input, this, deltaTime );
				inputHandled |= wasInputHandled;
				
				if ( m_contentsInvalidated == true )
				{
					// an input command caused the hud to be invalidated - break out
					break;
				}
			}
		}

		m_contentsInvalidated = false;
		
		return inputHandled;
	}
	
	/**
	 * Converts the touch position to the layout position.
	 * 
	 * @param x
	 * @param y
	 * @param outTouchPos
	 */
	public void getTouchPos( int x, int y, Vector3 outTouchPos )
	{
		outTouchPos.set( (float)x / (float)m_graphics.getWidth(), (float)y / (float)m_graphics.getHeight(), 0 );
	}
}
