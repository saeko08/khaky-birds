/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.affectors;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleAffector;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * Scatters particles further away from the axis of air current, simulating a wind-blow like effect.
 * 
 * @author Paksas
 */
public class BlowMovementAffector implements ParticleAffector 
{
	private Vector3			m_origin 				= new Vector3();
	private Vector3			m_blowAxisDirection	 	= new Vector3();
	private float			m_horizRange;	// range along the axis
	private float			m_vertRangeSq;	// range perpendicular to the axis ( squared value )
	private float			m_force;		// force with which the affector "blows" the particles away
	
	private Vector3			m_tmpLocalParticlePos	= new Vector3();
	private Vector3			m_tmpVel				= new Vector3();
	
	
	@Override
	public void update( float deltaTime, Particle particle ) 
	{		
		// we're gonna calculate the velocity based on how far a particle
		// is away from the blow axis - the further away the more velocity that's 
		// perpendicular to the blow axis it will receive
		
		// cast the particle onto the blow axis and see how far is it away from it
		Vector3 particlePos = particle.m_position;
		m_tmpLocalParticlePos.set(particlePos).sub( m_origin );
		float distFromOrigToPartSq = m_tmpLocalParticlePos.magSq2D();
		
		float projParticlePos = m_blowAxisDirection.dot2D( particlePos );
		if ( projParticlePos < 0.0f || projParticlePos > m_horizRange )
		{
			// the particle is either behind the affector, or too far away from it
			return;
		}
		
		// calculate the distance from particle to the axis
		float distFromPartToAxisSq = distFromOrigToPartSq * ( 1.0f - projParticlePos*projParticlePos );
		float blowFactor = m_vertRangeSq - distFromPartToAxisSq;
		if ( blowFactor < 0.0f )
		{
			// the particle's too far away to be affected
			return;
		}
		
		// create a velocity vector that's perpendicular to the axis and scale it according
		// to the distance from the axis
		m_tmpVel.set( m_blowAxisDirection.m_y, -m_blowAxisDirection.m_x, 0 );
		
		// check on which side of the axis the particle is located
		float sideFactor = m_tmpLocalParticlePos.dot2D( m_tmpVel );
		m_tmpVel.m_y *= sideFactor >= 0.0f ? 1.0f : -1.0f;
		
		// and finally add to particle's velocity
		m_tmpVel.scale( m_force * blowFactor * deltaTime );
		particle.m_velocity.add( m_tmpVel );
	}

	@Override
	public void load( DataLoader loader ) 
	{
		m_origin.load( "origin", loader );
		
		m_blowAxisDirection.load( "blowAxis", loader );
		m_horizRange = m_blowAxisDirection.mag2D();			
		m_blowAxisDirection.normalize2D();
		
		m_vertRangeSq = loader.getFloatValue( "range" );
		m_vertRangeSq *= m_vertRangeSq;
		
		m_force = loader.getFloatValue( "force" );
	}

}
