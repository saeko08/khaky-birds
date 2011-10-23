/**
 * 
 */
package com.hypefoundry.engine.hud;

import com.hypefoundry.engine.core.Input;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;


/**
 * Visual of a hud widget.
 * 
 * @author Paksas
 */
public abstract class HudWidgetVisual 
{
	private Vector3			m_drawingOffset		= new Vector3();
	private HudWidget		m_widget;
	
	protected float			m_width, m_height;
	protected Vector3		m_globalPos			= new Vector3();
	protected BoundingBox	m_bb				= new BoundingBox();
	
	
	/**
	 * Default constructor.
	 */
	protected HudWidgetVisual()
	{
	}
	
	/**
	 * Constructor.
	 * 
	 * @param widget
	 */
	protected HudWidgetVisual( HudWidget widget )
	{
		m_widget = widget;
		
		// calculate the global position
		m_globalPos.set( m_drawingOffset );
		
		HudWidget parent = m_widget;
		while( parent != null )
		{
			m_globalPos.add( parent.m_position );
			parent = parent.m_parent;
		}
		
		m_width = widget.m_width;
		m_height = widget.m_height;
		
		// calculate the bounding box
		m_bb.set( m_globalPos.m_x, m_globalPos.m_y, -1, m_globalPos.m_x + m_width, m_globalPos.m_y + m_height, 1 );
	}
	
	/**
	 * Sets the drawing offset.
	 * 
	 * @param x
	 * @param y
	 */
	public HudWidgetVisual setOffset( float x, float y )
	{
		m_globalPos.sub( m_drawingOffset );
		m_drawingOffset.set( x, y, 0 );
		m_globalPos.add( m_drawingOffset );
		
		// calculate the bounding box
		m_bb.set( m_globalPos.m_x, m_globalPos.m_y, -1, m_globalPos.m_x + m_width, m_globalPos.m_y + m_height, 1 );
		
		return this;
	}
	
	/**
	 * Sets the drawing offset.
	 * 
	 * @param x
	 * @param y
	 */
	public HudWidgetVisual resize( float width, float height )
	{
		m_width = width;
		m_height = height;
		
		// calculate the bounding box
		m_bb.set( m_globalPos.m_x, m_globalPos.m_y, -1, m_globalPos.m_x + m_width, m_globalPos.m_y + m_height, 1 );
				
		return this;
	}
	
	/**
	 * Draws the element on the screen.
	 * 
	 * @param batcher
	 * @param deltaTime
	 */
	public abstract void draw( SpriteBatcher batcher, float deltaTime );
	
	/**
	 * Handles the incoming input and changes the widget state accordingly.
	 * 
	 * @param input
	 * @param renderer
	 * @param deltaTime
	 */
	public abstract void handleInput( Input input, HudRenderer renderer, float deltaTime );
}
