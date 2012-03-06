/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.affectors;

import com.hypefoundry.engine.renderer2D.Color;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleAffector;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * Scales the particles according to the selected scaling policy ( relative
 * to a particle's lifetime or using a fixed scaling ratio ) 
 * 
 * @author Paksas
 */
public class LifetimeAffector implements ParticleAffector 
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
	
	enum Modifier
	{
		None,
		Sin,
		AbsSin,
		SqSin,
		Abs,
	}
	
	enum AffectedValue
	{
		Scale,
		Alpha
	}
	
	private Mode				m_mode = Mode.Lin;
	private Modifier			m_modifier = Modifier.None;
	private AffectedValue		m_value = AffectedValue.Scale;

	@Override
	public void update( float deltaTime, Particle particle ) 
	{
		float lt = particle.getLifetimePercent();
		float val = 0.0f;
		
		// calculate the value
		switch( m_mode )
		{
			case Lin:
			{
				val = lt; 
				break;
			}
			
			case Exp:
			{
				val = lt*lt;
				break;
			}
			
			case Log:
			{
				val = (float) Math.log10( lt * 9.0f + 1.0f );
				break;
			}
			
			case InvLin:
			{
				val = 1.0f - lt; 
				break;
			}
			
			case InvExp:
			{
				val = 1.0f - lt*lt;
				break;
			}
			
			case InvLog:
			{
				val = 1.0f - (float) Math.log10( lt * 9.0f + 1.0f );
				break;
			}
		}
		
		// apply the value modifier
		switch( m_modifier )
		{
			case Sin:
			{
				val = (float)Math.sin( val * Math.PI );
				break;
			}
			
			case AbsSin:
			{
				val = Math.abs( (float)Math.sin( val * Math.PI ) );
				break;
			}
			
			case SqSin:
			{
				val = (float)Math.sin( val * Math.PI );
				val *= val;
				break;
			}
			
			case Abs:
			{
				val = Math.abs( val );
				break;
			}
		}
		
		// set the value - and this means SET IT, not MODULATE it with the existing value
		switch( m_value )
		{
			case Scale:
			{
				particle.m_scale = val;
				break;
			}
			
			case Alpha:
			{
				particle.m_color.m_vals[ Color.Alpha ] = val;
				break;
			}
		}
	}

	@Override
	public void load(DataLoader loader) 
	{
		String modeStr = loader.getStringValue( "mode" );
		m_mode = Mode.valueOf( modeStr );
		
		String modifierStr = loader.getStringValue( "modifier" );
		if ( modifierStr.length() > 0 )
		{
			m_modifier = Modifier.valueOf( modifierStr );
		}

		String valueStr = loader.getStringValue( "value" );
		m_value = AffectedValue.valueOf( valueStr );
	}

}
