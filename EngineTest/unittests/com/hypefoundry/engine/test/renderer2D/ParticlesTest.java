/**
 * 
 */
package com.hypefoundry.engine.test.renderer2D;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleSystem;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleSystemPlayer;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticlesFactory;
import com.hypefoundry.engine.renderer2D.particleSystem.affectors.LinearMovementAffector;
import com.hypefoundry.engine.renderer2D.particleSystem.emitters.DirectionalParticleEmitter;
import com.hypefoundry.engine.renderer2D.particleSystem.emitters.RadialParticleEmitter;
import com.hypefoundry.engine.util.serialization.DataLoader;

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

		@Override
		public void load(DataLoader loader, ResourceManager resMgr) 
		{
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
		ps.addEmitter( new RadialParticleEmitter( new Vector3(), 0 )
							.setAmountEmittedEachTick( 1 )
							.setEmissionFrequency( emissionFrequency )
							.setParticlesCount( maxAliveParticles, new ParticlesFactoryStub() )
							.setTimeToLive( timeToLive ) );
		
		ParticleSystemPlayer player = new ParticleSystemPlayer( ps, true );
		
		// no particles are spawned at first
		assertEquals( 0, player.getActiveParticles( particles ) );
	
		// 1 sec. passes - we should have that many particles
		player.simulate( 1 );
		assertEquals( 5, player.getActiveParticles( particles ) );
	}
	
	public void testMultipleEmitters()
	{
		int maxParticles = 10;
		Particle[] particles = new Particle[maxParticles];
		
		ParticleSystem ps = new ParticleSystem( maxParticles );
		ps.addEmitter( new RadialParticleEmitter( new Vector3( 1, 0, 0 ), 0 )
							.setAmountEmittedEachTick( 1 )
							.setEmissionFrequency( 1 )
							.setParticlesCount( maxParticles / 2, new ParticlesFactoryStub() )
							.setTimeToLive( 10 ) );
		ps.addEmitter( new RadialParticleEmitter( new Vector3( 2, 0, 0 ), 0 )							
							.setAmountEmittedEachTick( 1 )
							.setEmissionFrequency( 1 )
							.setParticlesCount( maxParticles / 2, new ParticlesFactoryStub() )
							.setTimeToLive( 10 ) );
		
		ParticleSystemPlayer player = new ParticleSystemPlayer( ps, true );
		
		// no particles are spawned at first
		assertEquals( 0, player.getActiveParticles( particles ) );
	
		// 1 sec. passes - we should have that many particles
		player.simulate( 1 );
		assertEquals( 2, player.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 1, 0, 0 ) ) < 1e-3 );
		assertTrue( particles[1].m_position.dist( new Vector3( 2, 0, 0 ) ) < 1e-3 );
	}
	
	public void testParticleMovement()
	{
		int maxParticles = 1;
		Particle[] particles = new Particle[maxParticles];
		
		ParticleSystem ps = new ParticleSystem( maxParticles );
		ps.addEmitter( new DirectionalParticleEmitter( new Vector3(), new Vector3( 1, 0, 0 ) )							
							.setAmountEmittedEachTick( 1 )
							.setEmissionFrequency( 0.1f )
							.setParticlesCount( maxParticles, new ParticlesFactoryStub() )
							.setTimeToLive( 10 ) );
	
		ParticleSystemPlayer player = new ParticleSystemPlayer( ps, true );
		
		player.simulate( 0.2f );
		assertEquals( 1, player.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 0, 0, 0 ) ) < 1e-3 );
		
		player.simulate( 0.1f );
		assertEquals( 1, player.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 0.1f, 0, 0 ) ) < 1e-3 );
	}
	
	public void testParticleAffector()
	{
		int maxParticles = 1;
		Particle[] particles = new Particle[maxParticles];
		
		ParticleSystem ps = new ParticleSystem( maxParticles );
		ps.addEmitter( new RadialParticleEmitter( new Vector3(), 0 )							
							.setAmountEmittedEachTick( 1 )
							.setEmissionFrequency( 0.1f )
							.setParticlesCount( maxParticles, new ParticlesFactoryStub() )
							.setTimeToLive( 10 ) );
		ps.addAffector( new LinearMovementAffector( new Vector3( 10, 0, 0 ) ) );
	
		ParticleSystemPlayer player = new ParticleSystemPlayer( ps, true );
		
		player.simulate( 0.2f );
		assertEquals( 1, player.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 0, 0, 0 ) ) < 1e-3 );
		
		player.simulate( 0.1f );
		assertEquals( 1, player.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 0.1f, 0, 0 ) ) < 1e-3 );
	}
	
	public void testParticleRebirth()
	{
		int maxParticles = 1;
		Particle[] particles = new Particle[maxParticles];
		
		ParticleSystem ps = new ParticleSystem( maxParticles );
		ps.addEmitter( new RadialParticleEmitter( new Vector3(), 0 )							
							.setAmountEmittedEachTick( 1 )
							.setEmissionFrequency( 0.1f )
							.setParticlesCount( maxParticles, new ParticlesFactoryStub() )
							.setTimeToLive( 1 ) );
		ps.addAffector( new LinearMovementAffector( new Vector3( 10, 0, 0 ) ) );
	
		ParticleSystemPlayer player = new ParticleSystemPlayer( ps, true );
		
		player.simulate( 0.2f );
		assertEquals( 1, player.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 0, 0, 0 ) ) < 1e-3 );
		
		// it's moving
		player.simulate( 0.1f );
		assertEquals( 1, player.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 0.1f, 0, 0 ) ) < 1e-3 );
		
		// ... and moving
		player.simulate( 0.8f );
		assertEquals( 1, player.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 7.3f, 0, 0 ) ) < 1e-3 );
				
		// ... and after a second - it's brought back to its initial position
		player.simulate( 0.1f );
		assertEquals( 1, player.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 0, 0, 0 ) ) < 1e-3 );
	}
	
	public void testUnloopedPlayback()
	{
		int maxParticles = 1;
		Particle[] particles = new Particle[maxParticles];
		
		ParticleSystem ps = new ParticleSystem( maxParticles );
		ps.addEmitter( new RadialParticleEmitter( new Vector3(), 0 )							
							.setAmountEmittedEachTick( 1 )
							.setEmissionFrequency( 0.1f )
							.setParticlesCount( maxParticles, new ParticlesFactoryStub() )
							.setTimeToLive( 1 ) );
		ps.addAffector( new LinearMovementAffector( new Vector3( 10, 0, 0 ) ) );
	
		ParticleSystemPlayer player = new ParticleSystemPlayer( ps, false );
		
		player.simulate( 0.2f );
		assertEquals( 1, player.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 0, 0, 0 ) ) < 1e-3 );
		
		// it's moving
		player.simulate( 0.1f );
		assertEquals( 1, player.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 0.1f, 0, 0 ) ) < 1e-3 );
		
		// ... and moving
		player.simulate( 0.8f );
		assertEquals( 1, player.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 7.3f, 0, 0 ) ) < 1e-3 );
				
		// ... but it's not being restored
		player.simulate( 0.1f );
		assertEquals( 0, player.getActiveParticles( particles ) );
	}
}
