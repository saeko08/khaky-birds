/**
 * 
 */
package com.hypefoundry.engine.hud.visuals;

import com.hypefoundry.engine.core.Input;
import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.hud.HudRenderer;
import com.hypefoundry.engine.hud.HudWidget;
import com.hypefoundry.engine.hud.HudWidgetVisual;
import com.hypefoundry.engine.hud.HudWidgetVisualTemplate;
import com.hypefoundry.engine.hud.widgets.CounterWidget;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.font.Font;
import com.hypefoundry.engine.renderer2D.font.Text;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * A template for drawing a counter.
 * 
 * @author Paksas
 */
public class CounterVisualTemplate implements HudWidgetVisualTemplate 
{
	private FrameVisualTemplate 		m_frameTemplate 		= new DefaultFrameVisualTemplate();
	private ButtonVisualTemplate		m_incButtonTemplate		= new CustomButtonVisualTemplate( CounterWidget.INCREASED );
	private ButtonVisualTemplate		m_decButtonTemplate		= new CustomButtonVisualTemplate( CounterWidget.DECREASED );
	
	private Text						m_value 				= new Text();
	private StringBuilder				m_valueTranslator		= new StringBuilder();
	
	private float						m_buttonWidth, m_buttonHeight;
	
	
	/**
	 * Draws the counter.
	 * 
	 * @param batcher
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param value
	 */
	void draw( SpriteBatcher batcher, float x, float y, float width, float height, int value )
	{
		m_valueTranslator.delete( 0, m_valueTranslator.length() );
		m_valueTranslator.append( value );
		m_value.setText( m_valueTranslator.toString() );
		
		m_value.drawCentered( batcher, x, y, width, height );
	}
	
	@Override
	public void load( ResourceManager resMgr, DataLoader loader ) 
	{
		// load the frame & the buttons
		DataLoader frameNode = loader.getChild( "Frame" );
		if ( frameNode != null )
		{
			m_frameTemplate.load( resMgr, frameNode );
		}
		
		DataLoader incButtonNode = loader.getChild( "IncreaseButton" );
		if ( incButtonNode != null )
		{
			m_incButtonTemplate.load( resMgr, incButtonNode );
		}
		
		DataLoader decButtonNode = loader.getChild( "DecreaseButton" );
		if ( decButtonNode != null )
		{
			m_decButtonTemplate.load( resMgr, decButtonNode );
		}
		
		// load the font
		String fontPath = loader.getStringValue( "fontPath" );
		Font font = resMgr.getResource( Font.class, fontPath );
		m_value.setFont( font );
		
		// load the button dimensions
		m_buttonWidth = loader.getFloatValue( "buttonWidth" );
		m_buttonHeight = loader.getFloatValue( "buttonHeight" );
	}
	
	private class CompositeHudWidgetVisual extends HudWidgetVisual
	{
		private HudWidgetVisual[]		m_widgets = new HudWidgetVisual[0];
			
		/**
		 * Adds a new widget to the composite.
		 * 
		 * @param visual
		 */
		void addWidget( HudWidgetVisual visual )
		{
			if ( visual == null )
			{
				return;
			}
			
			HudWidgetVisual[] newArray = null;
			if ( m_widgets == null )
			{
				newArray = new HudWidgetVisual[1];
				newArray[0] = visual;
			}
			else
			{
				newArray = new HudWidgetVisual[ m_widgets.length + 1 ];
				for ( int i = 0; i < m_widgets.length; ++i )
				{
					newArray[i] = m_widgets[i];
				}
				newArray[m_widgets.length] = visual;
			}
			m_widgets = newArray;
		}
		
		@Override
		public void draw( SpriteBatcher batcher, float deltaTime ) 
		{
			for( int i = 0; i < m_widgets.length; ++i )
			{
				m_widgets[i].draw( batcher, deltaTime );
			}
		}

		@Override
		public void handleInput( Input input, HudRenderer renderer, float deltaTime ) 
		{
			for( int i = 0; i < m_widgets.length; ++i )
			{
				m_widgets[i].handleInput( input, renderer, deltaTime );
			}
		}
		
	}

	@Override
	public HudWidgetVisual instantiate( HudRenderer renderer, HudWidget widget ) 
	{
		float buttonWidth = m_buttonWidth / (float)renderer.m_graphics.getWidth();
		float buttonHeight = m_buttonHeight / (float)renderer.m_graphics.getHeight();
		
		CompositeHudWidgetVisual composite = new CompositeHudWidgetVisual();
		composite.addWidget( m_frameTemplate.instantiate( renderer, widget ) );
		composite.addWidget( new CounterVisual( widget, this ) );
		
		composite.addWidget( m_incButtonTemplate.instantiate( renderer, widget ).setOffset( widget.m_width, 0 ).resize( buttonWidth, buttonHeight ) );
		composite.addWidget( m_decButtonTemplate.instantiate( renderer, widget ).setOffset( widget.m_width, widget.m_height * 0.5f ).resize( buttonWidth, buttonHeight ) );
		
		return composite;
	}

}
