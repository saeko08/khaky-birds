/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.emitters;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleEmitter;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * An emitter that emits the particles from a line.
 * 
 * @author Paksas
 */
public class LineParticleEmitter extends ParticleEmitter 
{

	private Vector3 	m_lineStart;
	private Vector3 	m_lineEnd;
	private Vector3 	m_velocity;
	private boolean		m_uniformDistribution;
	private float		m_uniformDistributionSpacing;
	
	/**
	 * Default constructor.
	 */
	public LineParticleEmitter()
	{
		m_lineStart = new Vector3();
		m_lineEnd = new Vector3();
		m_velocity = new Vector3();
	}

	
	@Override
	protected void initialize( int particleIdx, Particle particle ) 
	{
		// calculate a random position on the line from which to emit the particle
		particle.m_position.set( m_lineEnd );
		particle.m_position.sub( m_lineStart );	
		if ( m_uniformDistribution )
		{
			particle.m_position.normalize2D();
			particle.m_position.scale( (float)particleIdx * m_uniformDistributionSpacing );
		}
		else
		{
			particle.m_position.scale( (float)Math.random() );
		}
		particle.m_position.add( m_lineStart );
		
		// initialize particle's velocity
		particle.m_velocity.set( m_velocity );
	}

	@Override
	protected void onLoad( DataLoader loader ) 
	{
		m_lineStart.load( "lineStart", loader );
		m_lineEnd.load( "lineEnd", loader );
		
		m_uniformDistributionSpacing = loader.getFloatValue( "uniformDistribution", 0.0f );
		m_uniformDistribution = m_uniformDistributionSpacing > 0.0f;
		if ( m_uniformDistribution )
		{
			// we'll use the velocity vector for the computations - and load its value afterwards.
			// need to save that memory after all :)
			
			m_velocity.set( m_lineEnd ).sub( m_lineStart );
			m_particlesCount = (int)( m_velocity.mag2D() / m_uniformDistributionSpacing );

			setAmountEmittedEachTick( m_particlesCount );
		}
		
		m_velocity.load( "velocity", loader );
	}

}
