/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.affectors;

import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleAffector;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * Scales the particles according to the selected scaling policy ( relative
 * to a particle's lifetime or using a fixed scaling ratio ) 
 * 
 * @author Paksas
 */
public class ScaleAffector implements ParticleAffector 
{
	enum Mode
	{
		Lin,
		Exp,
		Log,
		InvLin,
		InvExp,
		InvLog
	}
	private Mode			m_mode = Mode.Lin;

	@Override
	public void update( float deltaTime, Particle particle ) 
	{
		float lt = particle.getLifetimePercent();
		
		switch( m_mode )
		{
			case Lin:
			{
				particle.m_scale = lt; 
				break;
			}
			
			case Exp:
			{
				particle.m_scale = lt*lt;
				break;
			}
			
			case Log:
			{
				particle.m_scale = (float) Math.log10( lt * 9.0f + 1.0f );
				break;
			}
			
			case InvLin:
			{
				particle.m_scale = 1.0f - lt; 
				break;
			}
			
			case InvExp:
			{
				particle.m_scale = 1.0f - lt*lt;
				break;
			}
			
			case InvLog:
			{
				particle.m_scale = 1.0f - (float) Math.log10( lt * 9.0f + 1.0f );
				break;
			}
		}
	}

	@Override
	public void load(DataLoader loader) 
	{
		String modeStr = loader.getStringValue( "mode" );
		m_mode = Mode.valueOf( modeStr );

	}

}
