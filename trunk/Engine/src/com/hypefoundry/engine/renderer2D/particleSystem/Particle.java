/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.TextureRegion;

/**
 * A single particle.
 * 
 * @author Paksas
 *
 */
public class Particle 
{
	public Vector3			m_position = new Vector3();
	public float			m_orientation = 0;
	public float			m_width = 0;
	public float			m_height = 0;
	public TextureRegion	m_textureRegion = null;
}
