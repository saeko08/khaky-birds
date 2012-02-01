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
		particle.m_position.scale( (float)Math.random() );
		particle.m_position.add( m_lineStart );
		
		// initialize particle's velocity
		particle.m_velocity.set( m_velocity );
	}

	@Override
	protected void onLoad( DataLoader loader ) 
	{
		m_lineStart.load( "lineStart", loader );
		m_lineEnd.load( "lineEnd", loader );
		m_velocity.load( "velocity", loader );
	}

}
