/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.affectors;

import java.util.Random;

import com.hypefoundry.engine.math.MathLib;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleAffector;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * @author Paksas
 *
 */
public class RotationAffector implements ParticleAffector
{
	private float	m_angularSpeedStart = 0.0f;
	private float	m_angularSpeed = 0.0f;
	private Random	m_random = new Random();
	
	@Override
	public void update( float deltaTime, Particle particle ) 
	{
		int seed = particle.hashCode();
		m_random.setSeed( seed );
		float randVal = m_random.nextFloat();
		
		float rot = m_angularSpeedStart + m_angularSpeed * randVal * deltaTime;
		particle.m_orientation = MathLib.normalizeAngle( particle.m_orientation + rot );
	}

	@Override
	public void load(DataLoader loader) 
	{
		m_angularSpeed = loader.getFloatValue( "angularSpeed", 0.0f );
		m_angularSpeed *= Math.PI * 2.0f;
		
		m_angularSpeedStart = m_angularSpeed * -0.5f;
	}

}
