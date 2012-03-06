/**
 * 
 */
package com.hypefoundry.engine.test.renderer2D;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Color;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleEmitter;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleSystem;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleSystemPlayer;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticlesFactory;
import com.hypefoundry.engine.renderer2D.particleSystem.affectors.ClearSkyAffector;
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
		int maxAliveParticles = 5;
		float emissionFrequency = 0.1f;
		float timeToLive = 10;
		ParticleSystem ps = new ParticleSystem();
		ps.addEmitter( new RadialParticleEmitter( new Vector3(), 0 )
							.setAmountEmittedEachTick( 1 )
							.setEmissionFrequency( emissionFrequency )
							.setParticlesCount( maxAliveParticles, new ParticlesFactoryStub() )
							.setTimeToLive( timeToLive ) );
		Particle[] particles = new Particle[ps.m_maxParticles];
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
		ParticleSystem ps = new ParticleSystem();
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
		
		Particle[] particles = new Particle[ps.m_maxParticles];
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
		ParticleSystem ps = new ParticleSystem();
		ps.addEmitter( new DirectionalParticleEmitter( new Vector3(), new Vector3( 1, 0, 0 ), 0.0f )							
							.setAmountEmittedEachTick( 1 )
							.setEmissionFrequency( 0.1f )
							.setParticlesCount( 1, new ParticlesFactoryStub() )
							.setTimeToLive( 10 ) );
		
		Particle[] particles = new Particle[ps.m_maxParticles];
		ParticleSystemPlayer player = new ParticleSystemPlayer( ps, true );
		
		player.simulate( 0.2f );
		assertEquals( 1, player.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 0.1f, 0, 0 ) ) < 1e-3 );
		
		player.simulate( 0.1f );
		assertEquals( 1, player.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 0.2f, 0, 0 ) ) < 1e-3 );
	}
	
	public void testParticleAffector()
	{
		int maxParticles = 1;		
		ParticleSystem ps = new ParticleSystem();
		ps.addEmitter( new RadialParticleEmitter( new Vector3(), 0 )							
							.setAmountEmittedEachTick( 1 )
							.setEmissionFrequency( 0.1f )
							.setParticlesCount( maxParticles, new ParticlesFactoryStub() )
							.setTimeToLive( 10 ) );
		ps.addAffector( new LinearMovementAffector( new Vector3( 10, 0, 0 ) ) );
		Particle[] particles = new Particle[ps.m_maxParticles];
		ParticleSystemPlayer player = new ParticleSystemPlayer( ps, true );
		
		player.simulate( 0.2f );
		assertEquals( 1, player.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 0.1f, 0, 0 ) ) < 1e-3 );
		
		player.simulate( 0.1f );
		assertEquals( 1, player.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 0.3f, 0, 0 ) ) < 1e-3 );
	}
	
	public void testParticleRebirth()
	{
		int maxParticles = 1;
		ParticleSystem ps = new ParticleSystem( );
		ps.addEmitter( new RadialParticleEmitter( new Vector3(), 0 )							
							.setAmountEmittedEachTick( 1 )
							.setEmissionFrequency( 0.1f )
							.setParticlesCount( maxParticles, new ParticlesFactoryStub() )
							.setTimeToLive( 1 ) );
		ps.addAffector( new LinearMovementAffector( new Vector3( 10, 0, 0 ) ) );
		Particle[] particles = new Particle[ps.m_maxParticles];
		ParticleSystemPlayer player = new ParticleSystemPlayer( ps, true );
		
		player.simulate( 0.2f );
		assertEquals( 1, player.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 0.1f, 0, 0 ) ) < 1e-3 );
		
		// it's moving
		player.simulate( 0.1f );
		assertEquals( 1, player.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 0.3f, 0, 0 ) ) < 1e-3 );
		
		// ... and moving
		player.simulate( 0.8f );
		assertEquals( 1, player.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 8.3f, 0, 0 ) ) < 1e-3 );
				
		// ... and after a second - it's brought back to its initial position
		player.simulate( 0.2f );
		assertEquals( 1, player.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 0.1f, 0, 0 ) ) < 1e-3 );
	}
	
	public void testUnloopedPlayback()
	{
		int maxParticles = 1;		
		ParticleSystem ps = new ParticleSystem();
		ps.addEmitter( new RadialParticleEmitter( new Vector3(), 0 )							
							.setAmountEmittedEachTick( 1 )
							.setEmissionFrequency( 0.1f )
							.setParticlesCount( maxParticles, new ParticlesFactoryStub() )
							.setTimeToLive( 1 ) );
		ps.addAffector( new LinearMovementAffector( new Vector3( 10, 0, 0 ) ) );
		Particle[] particles = new Particle[ps.m_maxParticles];
		ParticleSystemPlayer player = new ParticleSystemPlayer( ps, false );
		
		player.simulate( 0.2f );
		assertEquals( 1, player.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 0.1f, 0, 0 ) ) < 1e-3 );
		
		// it's moving
		player.simulate( 0.1f );
		assertEquals( 1, player.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 0.3f, 0, 0 ) ) < 1e-3 );
		
		// ... and moving
		player.simulate( 0.8f );
		assertEquals( 1, player.getActiveParticles( particles ) );
		assertTrue( particles[0].m_position.dist( new Vector3( 8.3f, 0, 0 ) ) < 1e-3 );
				
		// ... but it's not being restored
		player.simulate( 0.2f );
		assertEquals( 0, player.getActiveParticles( particles ) );
	}
	

	class FixedPositionMockParticleEmitter extends ParticleEmitter
	{
		private Vector3 		m_emissionPos;
		
		FixedPositionMockParticleEmitter( Vector3 emissionPos )
		{
			m_emissionPos = emissionPos;
			
			setAmountEmittedEachTick( 1 );
			setEmissionFrequency( 0.1f );
			setParticlesCount( 1, new ParticlesFactoryStub() );
			setTimeToLive( 100000000 );
		}
		
		@Override
		protected void initialize(int particleIdx, Particle particle) 
		{
			particle.m_position = m_emissionPos;
		}

		@Override
		protected void onLoad(DataLoader loader) {}
		
	};
	
	public void testClearSkyAffector()
	{	
		// setup the system
		Vector3 particlePos = new Vector3( 1.5f, 0, 0 );
		ParticleSystem ps = new ParticleSystem();
		ps.addEmitter( new FixedPositionMockParticleEmitter( particlePos ) );
		ps.addAffector( new ClearSkyAffector( new BoundingBox( 0, 0, 1, 1 ), 0.5f ) );
		
		// start the simulation - first emit the particles
		Particle[] particles = new Particle[ps.m_maxParticles];
		ParticleSystemPlayer player = new ParticleSystemPlayer( ps, false );
		
		player.simulate( 0.2f );
		assertEquals( 1, player.getActiveParticles( particles ) );
		
		// initially the particle is emitted outside the  clear sky area, so it's alpha should be set to 1
		// Test various cases of that
		{
			assertTrue( particles[0].m_color.m_vals[ Color.Alpha ] >= 1.0f );
			
			particlePos.set( -0.5f, 0, 0 );
			player.simulate( 0.1f );
			assertTrue( particles[0].m_color.m_vals[ Color.Alpha ] >= 1.0f );
			
			particlePos.set( 0, 1.5f, 0 );
			player.simulate( 0.1f );
			assertTrue( particles[0].m_color.m_vals[ Color.Alpha ] >= 1.0f );
			
			particlePos.set( 0, -0.5f, 0 );
			player.simulate( 0.1f );
			assertTrue( particles[0].m_color.m_vals[ Color.Alpha ] >= 1.0f );
		}
		
		// Now test what happends if the particle is on the border of the area - it should
		// maintain an alpha value equal to 1
		{
			particlePos.set( 0, 0.5f, 0 );
			player.simulate( 0.1f );
			assertTrue( particles[0].m_color.m_vals[ Color.Alpha ] >= 1.0f );
			
			particlePos.set( 1.0f, 0.5f, 0 );
			player.simulate( 0.1f );
			assertTrue( particles[0].m_color.m_vals[ Color.Alpha ] >= 1.0f );
			
			particlePos.set( 0.5f, 0, 0 );
			player.simulate( 0.1f );
			assertTrue( particles[0].m_color.m_vals[ Color.Alpha ] >= 1.0f );
			
			particlePos.set( 0.5f, 1.0f, 0 );
			player.simulate( 0.1f );
			assertTrue( particles[0].m_color.m_vals[ Color.Alpha ] >= 1.0f );
		}
		
		// Test what happends if the particle enters the area, but is half way inside the border value
		{
			particlePos.set( 0.125f, 0.5f, 0 );
			player.simulate( 0.1f );
			assertTrue( Math.abs( particles[0].m_color.m_vals[ Color.Alpha ] - 0.5f ) < 1e-3 );
			
			// we need to reset the alpha value, 'cause this affector only modulates the existing alpha value.
			// We'll be doing so from now on
			particles[0].m_color.m_vals[ Color.Alpha ] = 1.0f;
			
			particlePos.set( 0.875f, 0.5f, 0 );
			player.simulate( 0.1f );
			assertTrue( Math.abs( particles[0].m_color.m_vals[ Color.Alpha ] - 0.5f ) < 1e-3 );
			particles[0].m_color.m_vals[ Color.Alpha ] = 1.0f;
			
			particlePos.set( 0.875f, 0.01f, 0 );
			player.simulate( 0.1f );
			assertTrue( Math.abs( particles[0].m_color.m_vals[ Color.Alpha ] - 0.96f ) < 1e-3 );
			particles[0].m_color.m_vals[ Color.Alpha ] = 1.0f;
		}		
	}
	
	public void testMultipleClearSkyAffector()
	{	
		// setup the system
		Vector3 particlePos = new Vector3( 2.0f, 0, 0 );
		ParticleSystem ps = new ParticleSystem();
		ps.addEmitter( new FixedPositionMockParticleEmitter( particlePos ) );
		
		// two overlapping clear sky affectors
		ps.addAffector( new ClearSkyAffector( new BoundingBox( 0, 0, 1, 1 ), 0.5f ) );
		ps.addAffector( new ClearSkyAffector( new BoundingBox( 0.5f, 0, 1.5f, 1 ), 0.5f ) );
		
		// start the simulation - first emit the particles
		Particle[] particles = new Particle[ps.m_maxParticles];
		ParticleSystemPlayer player = new ParticleSystemPlayer( ps, false );
		
		player.simulate( 0.2f );
		assertEquals( 1, player.getActiveParticles( particles ) );
		
		// Particle outside both areas
		{
			assertTrue( Math.abs( particles[0].m_color.m_vals[ Color.Alpha ] - 1.0f ) < 1e-3 );
			particles[0].m_color.m_vals[ Color.Alpha ] = 1.0f;
			
			particlePos.set( -0.5f, 0, 0 );
			player.simulate( 0.1f );
			assertTrue( Math.abs( particles[0].m_color.m_vals[ Color.Alpha ] - 1.0f ) < 1e-3 );
			particles[0].m_color.m_vals[ Color.Alpha ] = 1.0f;
			
			particlePos.set( 0, 1.5f, 0 );
			player.simulate( 0.1f );
			assertTrue( Math.abs( particles[0].m_color.m_vals[ Color.Alpha ] - 1.0f ) < 1e-3 );
			particles[0].m_color.m_vals[ Color.Alpha ] = 1.0f;
			
			particlePos.set( 0, -0.5f, 0 );
			player.simulate( 0.1f );
			assertTrue( Math.abs( particles[0].m_color.m_vals[ Color.Alpha ] - 1.0f ) < 1e-3 );
			particles[0].m_color.m_vals[ Color.Alpha ] = 1.0f;
		}
		
		// Particle outside the first area, but inside the second one
		{
			particlePos.set( 1.2f, 0.5f, 0 );
			player.simulate( 0.1f );
			assertTrue( particles[0].m_color.m_vals[ Color.Alpha ] <= 0.0f );
			particles[0].m_color.m_vals[ Color.Alpha ] = 1.0f;
		}
		
		// Particle outside the second area, but inside the first one
		{
			particlePos.set( 0.3f, 0.5f, 0 );
			player.simulate( 0.1f );
			assertTrue( particles[0].m_color.m_vals[ Color.Alpha ] <= 0.0f );
			particles[0].m_color.m_vals[ Color.Alpha ] = 1.0f;
		}
	}
}
