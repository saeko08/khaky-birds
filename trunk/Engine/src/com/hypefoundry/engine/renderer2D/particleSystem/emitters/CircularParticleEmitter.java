/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.emitters;

import android.util.FloatMath;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleEmitter;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * This emitter will create new particles on a circular rim surrounding the emiter's
 * position and will make them move outwards or inwards, depending on the sign of the speed parameter.
 * Negative sign emits the particle inwards, while a positive one emits them outwards.
 * 
 * @author Paksas
 */
public class CircularParticleEmitter extends ParticleEmitter 
{
	private Vector3 	m_position = new Vector3();
	private float		m_radius;
	private float		m_speed;

	
	@Override
	protected void initialize( int particleIdx, Particle particle ) 
	{
		// randomize the position on the rim
		float randAngle = (float)( Math.random() * Math.PI * 2.0f );
		particle.m_position.m_x = FloatMath.cos( randAngle );
		particle.m_position.m_y = FloatMath.sin( randAngle );
		particle.m_position.m_z = 0.0f;
		
		// set particle speed - right now particle's position is a unit vector
		// from the emiter's origin, so use that as velocity's direction
		particle.m_velocity.set( particle.m_position ).scale( m_speed );
		
		// finalize position calculations
		particle.m_position.scale( m_radius ).add( m_position );
		
	}

	@Override
	protected void onLoad( DataLoader loader ) 
	{
		m_position.load( "position", loader );
		m_radius = loader.getFloatValue( "radius", 1.0f );
		m_speed = loader.getFloatValue( "speed", 1.0f );
	}

}
