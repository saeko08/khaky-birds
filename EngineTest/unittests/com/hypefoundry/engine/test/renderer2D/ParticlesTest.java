/**
 * 
 */
package com.hypefoundry.engine.test.renderer2D;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleSystem;
import com.hypefoundry.engine.renderer2D.particleSystem.emitters.PointParticleEmitter;

import android.test.AndroidTestCase;


/**
 * @author Paksas
 *
 */
public class ParticlesTest extends AndroidTestCase 
{
	public void testPointEmitter()
	{		
		int maxParticles = 100;
		int maxAliveParticles = 5;
		float emissionFrequency = 0.1f;
		ParticleSystem ps = new ParticleSystem( maxParticles );
		ps.addEmitter( new PointParticleEmitter( maxAliveParticles, emissionFrequency, new Vector3() ) );
		
		// no particles are spawned at first
		assertEquals( 0, ps.getParticlesCount() );
	
		// 1 sec. passes - we should have that many particles
		ps.simulate( 1 );
		assertEquals( 5, ps.getParticlesCount() );
	}
	
	public void testMultipleEmitters()
	{
		// TODO
	}
}
