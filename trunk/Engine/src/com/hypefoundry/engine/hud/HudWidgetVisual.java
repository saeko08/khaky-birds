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
	private HudWidgetVisual		m_parentVisual;
	HudWidget					m_widget;
	
	private Vector3				m_drawingOffset		= new Vector3();
	protected float				m_width				= 1;
	protected float				m_height			= 1;
	protected Vector3			m_globalPos			= new Vector3();
	protected BoundingBox		m_bb				= new BoundingBox();
	
	
	/**
	 * Default constructor.
	 */
	protected HudWidgetVisual()
	{
	}
	
	/**
	 * Constructor.
	 * 
	 * @param parentVisual
	 * @param widget
	 */
	protected HudWidgetVisual( HudWidget widget )
	{
		m_parentVisual = null;
		m_widget = widget;
		
		// calculate the global position
		m_globalPos.set( m_drawingOffset ).add( m_widget.m_position );
		m_width = m_widget.m_width;
		m_height = m_widget.m_height;
		
		recalculateBounds();
	}
	
	/**
	 * Sets the parent visual.
	 * 
	 * @param parentVisual
	 */
	public void setParent( HudWidgetVisual parentVisual )
	{
		m_parentVisual = parentVisual;
		
		// calculate the global position
		m_globalPos.set( m_drawingOffset );
		if ( m_widget != null )
		{
			m_globalPos.add( m_widget.m_position );
			m_width = m_widget.m_width;
			m_height = m_widget.m_height;
		}
		else
		{
			m_width = 1;
			m_height = 1;
		}
				
		recalculateBounds();
	}
	
	/**
	 * Sets the drawing offset.
	 * 
	 * @param x
	 * @param y
	 */
	public HudWidgetVisual setOffset( float x, float y )
	{
		m_drawingOffset.set( x, y, 0 );
		
		m_globalPos.set( m_drawingOffset );
		if ( m_widget != null )
		{
			m_globalPos.add( m_widget.m_position );
			m_width = m_widget.m_width;
			m_height = m_widget.m_height;
		}
		else
		{
			m_width = 1;
			m_height = 1;
		}
		
		recalculateBounds();
		
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
		// recalculate the global position and size
		m_globalPos.set( m_drawingOffset );
		if ( m_widget != null )
		{
			m_globalPos.add( m_widget.m_position );
		}
		m_width = width;
		m_height = height;
		
		recalculateBounds();
				
		return this;
	}
	
	private void recalculateBounds()
	{
		if ( m_parentVisual != null )
		{
			m_width *= m_parentVisual.m_width;
			m_height *= m_parentVisual.m_height;
					
			m_globalPos.scale( m_parentVisual.m_width, m_parentVisual.m_height, 0 ).add( m_parentVisual.m_globalPos );
		}
		
		// calculate the bounding box
		m_bb.set( m_globalPos.m_x, m_globalPos.m_y, -1, m_globalPos.m_x + m_width, m_globalPos.m_y + m_height, 1 );
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
