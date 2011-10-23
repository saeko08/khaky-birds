/**
 * 
 */
package com.hypefoundry.engine.hud.visuals;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.hud.HudRenderer;
import com.hypefoundry.engine.hud.HudWidget;
import com.hypefoundry.engine.hud.HudWidgetVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * A frame visual template that allows you to substitute a custom bitmap for a default frame border.
 * 
 * @author Paksas
 */
public class CustomFrameVisualTemplate implements FrameVisualTemplate 
{
	private TextureRegion			m_region;
	

	@Override
	public void load( ResourceManager resMgr, DataLoader loader ) 
	{
		String texturePath = loader.getStringValue( "texturePath" );
		m_region = resMgr.getResource( TextureRegion.class, texturePath );
	}

	@Override
	public HudWidgetVisual instantiate( HudRenderer renderer, HudWidget widget ) 
	{
		return new FrameVisual( widget, this );
	}

	@Override
	public void draw( SpriteBatcher batcher, float x, float y, float width, float height, float deltaTime ) 
	{
		if ( m_region != null )
		{
			batcher.drawUnalignedSprite( x, y, width, height, m_region );
		}
	}

}
