/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.affectors;

import com.hypefoundry.engine.renderer2D.Color;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleAffector;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * Sets the particle alpha to the desired value.
 * 
 * @author Paksas
 */
public class AlphaAffector implements ParticleAffector 
{
	private float			m_alphaVal;
	
	@Override
	public void update( float deltaTime, Particle particle ) 
	{
		particle.m_color.m_vals[ Color.Alpha ] = m_alphaVal;
	}

	@Override
	public void load (DataLoader loader ) 
	{
		m_alphaVal = loader.getFloatValue( "alpha" );
	}

}
