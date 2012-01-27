/**
 * 
 */
package com.hypefoundry.engine.hud.widgets.checkbox;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.hud.HudRenderer;
import com.hypefoundry.engine.hud.HudWidget;
import com.hypefoundry.engine.hud.HudWidgetVisual;
import com.hypefoundry.engine.hud.HudWidgetVisualTemplate;
import com.hypefoundry.engine.renderer2D.RenderState;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * @author Paksas
 *
 */
public class CustomCheckboxVisualTemplate implements HudWidgetVisualTemplate 
{
	private TextureRegion[]		m_regions	= new TextureRegion[ CheckboxWidget.State.values().length ];
	
	@Override
	public void load( ResourceManager resMgr, DataLoader loader ) 
	{
		RenderState rs = new RenderState();
		rs.deserialize( resMgr, loader );
		
		CheckboxWidget.State[] states = CheckboxWidget.State.values();
		DataLoader elemNode;
		for ( int i = 0; i < states.length; ++i )
		{
			if ( ( elemNode = loader.getChild( states[i].m_xmlTag ) ) != null )
			{
				m_regions[ states[i].m_value ] = new TextureRegion( rs );
				m_regions[ states[i].m_value ].deserializeCoordinates( elemNode );
			}
		}
	}

	@Override
	public HudWidgetVisual instantiate( HudRenderer renderer, HudWidget widget ) 
	{
		return new CheckboxVisual( widget, this );
	}
	
	/**
	 * Draws a button with a caption.
	 * 
	 * @param batcher
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param isChecked
	 */
	public void draw( SpriteBatcher batcher, float x, float y, float width, float height, CheckboxWidget.State state )
	{
		if ( m_regions[ state.m_value ] != null )
		{
			batcher.drawUnalignedSprite( x, y, width, height, m_regions[ state.m_value ] );
		}
	}
}
