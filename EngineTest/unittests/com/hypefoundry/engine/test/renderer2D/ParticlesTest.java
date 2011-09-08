/**
 * 
 */
package com.hypefoundry.engine.test.renderer2D;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleSystem;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticlesFactory;
import com.hypefoundry.engine.renderer2D.particleSystem.affectors.LinearMovementAffector;
import com.hypefoundry.engine.renderer2D.particleSystem.emitters.PointParticleEmitter;

import android.test.AndroidTestCase;


/**
 * @author Paksas
 *
 */
public class ParticlesTest extends AndroidTestCase 
{
	class ParticlesFactoryStub implements ParticlesFactory
	{
		@Override
		public Particle create() 
		{
			return new Particle();
		}	
	}
	
	// ------------------------------------------------------------------------
	
	public void testPointEmitter()
	{		
		int maxParticles = 5;
		Particle[] particles = new Particle[maxParticles];
		
		int maxAliveParticles = 5;
		float emissionFrequency = 0.1f;
		float timeToLive = 10;
		ParticleSystem ps = new ParticleSystem( maxParticles );
		ps.addEmitter( new PointParticleEmitter( new Vector3() )
							.setAmountEmittedEachTick( 1 )
							.setEmissionFrequency( emissionFrequency )
							.setParticlesCount( maxAliveParticles, new ParticlesFactoryStub() )
							.setTimeToLive( timeToLive ) );
		
		// no particles are spawned at first
		assertEquals( 0, ps.getActiveParticles( particles ) );
	
		// 1 sec. passes - we should have that many particles
		ps.simulate( 1 );
		assertEquals( 5, ps.getActiveParticles( particles ) );
	}
	
	public void testMultipleEmitters()
	{
		int maxParticles = 10;
		Particle[] particles = new Particle[maxParticles];
		
		ParticleSystem ps = new ParticleSystem( maxParticles );
		ps.addEmitter( new PointParticleEmitter( new Vector3( 1, 0, 0 ) )
							.setAmountEmittedEachTick( 1 )
							.setEmissionFrequency( 1 )
							.setParticlesCount( maxParticles / 2, new ParticlesFactoryStub() )
							.setTimeToLive( 10 ) );
		ps.addEmitter( new PointParticleEmitter( new Vector3( 2, 0, 0 ) )							
							.setAmountEmittedEachTick( 1 )
							.setEmissionFrequency( 1 )
							.setParticlesCount( maxParticles / 2, new ParticlesFactoryStub() )
							.setTimeToLive( 10 ) );
		
		// no particles are spawned at first
		assertEquals( 0, ps.getActiveParticles( particles ) );
	
		// 1 sec. passes - we should have that many particles
		ps.simulate( 1 );
		assertEquals( 2, ps.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 1, 0, 0 ) ) < 1e-3 );
		assertTrue( particles[1].m_position.dist( new Vector3( 2, 0, 0 ) ) < 1e-3 );
	}
	
	public void testParticleMovement()
	{
		int maxParticles = 1;
		Particle[] particles = new Particle[maxParticles];
		
		ParticleSystem ps = new ParticleSystem( maxParticles );
		ps.addEmitter( new PointParticleEmitter( new Vector3() )							
							.setAmountEmittedEachTick( 1 )
							.setEmissionFrequency( 0.1f )
							.setParticlesCount( maxParticles, new ParticlesFactoryStub() )
							.setTimeToLive( 10 ) );
		ps.addAffector( new LinearMovementAffector( new Vector3( 1, 0, 0 ) ) );
	
		ps.simulate( 0.2f );
		assertEquals( 1, ps.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 0, 0, 0 ) ) < 1e-3 );
		
		ps.simulate( 0.1f );
		assertEquals( 1, ps.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 0.1f, 0, 0 ) ) < 1e-3 );
	}
	
	public void testParticleRebirth()
	{
		int maxParticles = 1;
		Particle[] particles = new Particle[maxParticles];
		
		ParticleSystem ps = new ParticleSystem( maxParticles );
		ps.addEmitter( new PointParticleEmitter( new Vector3() )							
							.setAmountEmittedEachTick( 1 )
							.setEmissionFrequency( 0.1f )
							.setParticlesCount( maxParticles, new ParticlesFactoryStub() )
							.setTimeToLive( 1 ) );
		ps.addAffector( new LinearMovementAffector( new Vector3( 1, 0, 0 ) ) );
	
		ps.simulate( 0.2f );
		assertEquals( 1, ps.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 0, 0, 0 ) ) < 1e-3 );
		
		// it's moving
		ps.simulate( 0.1f );
		assertEquals( 1, ps.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 0.1f, 0, 0 ) ) < 1e-3 );
		
		// ... and moving
		ps.simulate( 0.8f );
		assertEquals( 1, ps.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 0.9f, 0, 0 ) ) < 1e-3 );
				
		// ... and after a second - it's brought back to its initial position
		ps.simulate( 0.1f );
		assertEquals( 1, ps.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 0, 0, 0 ) ) < 1e-3 );
	}
	
	public void testRadialEmitter()
	{
		// TODO
	}
	
}
