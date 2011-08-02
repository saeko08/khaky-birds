/**
 * 
 */
package com.hypefoundry.engine.renderer2D;

import com.hypefoundry.engine.core.Texture;

/**
 * A region in an atlas that specifies a texture.
 * 
 * @author paksas
 *
 */
public class TextureRegion
{
	public final float 			m_u1, m_v1;
	public final float 			m_u2, m_v2;
	
	public final Texture 		m_texture;
	
	/**
	 * Constructor.
	 * 
	 * @param texture
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public TextureRegion( Texture texture, float x, float y, float width, float height ) 
	{
		if ( texture != null )
		{
			m_u1 = x / texture.getWidth();
			m_v1 = y / texture.getHeight();
			m_u2 = m_u1 + width / texture.getWidth();
			m_v2 = m_v1 + height / texture.getHeight();
		}
		else
		{
			m_u1 = 0;
			m_v1 = 0;
			m_u2 = 0;
			m_v2 = 0;
		}
		
		m_texture = texture;
	}
}
