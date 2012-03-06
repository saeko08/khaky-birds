/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.affectors;

import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.MathLib;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Color;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleAffector;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * This affector will reduce the alpha of a particle to zero within the specified area, making
 * it seam as if there was a patch of 'clear sky' in the emitted particles field.
 * 
 * @author Paksas
 */
public class ClearSkyAffector implements ParticleAffector 
{
	private BoundingBox 	m_box;
	private float			m_maxVertPenetrationDepth	= 0.0f;
	private float			m_maxHorizPenetrationDepth	= 0.0f;
	private Vector3			m_intersectPos				= new Vector3();
	
	
	/**
	 * Default constructor.
	 */
	public ClearSkyAffector()
	{
	}
	
	/**
	 * Manual constructor.
	 * 
	 * @param area
	 * @param borderThickness
	 */
	public ClearSkyAffector( BoundingBox area, float borderThickness )
	{
		m_box = area;
		calculatePenetrationDepths( borderThickness );
	}
	
	@Override
	public void update( float deltaTime, Particle particle ) 
	{
		if ( m_box.doesOverlap( particle.m_position, null ) == false )
		{
			// affect only the overlapping particles
			return;
		}
		
		// calculate the particle's penetration depth and modulate particle's alpha accordingly
		m_box.getNearestPointFromInside( particle.m_position, m_intersectPos );
		
		float vertPenetrationDepth = Math.abs( m_intersectPos.m_y - particle.m_position.m_y );
		float horizPenetrationDepth = Math.abs( m_intersectPos.m_x - particle.m_position.m_x );
		
		float vertScale = MathLib.clamp( ( m_maxVertPenetrationDepth - vertPenetrationDepth ) / m_maxVertPenetrationDepth, 0.0f, 1.0f );
		float horizScale = MathLib.clamp( ( m_maxHorizPenetrationDepth - horizPenetrationDepth ) / m_maxHorizPenetrationDepth, 0.0f, 1.0f );

		float scale = Math.max( vertScale, horizScale );
		
		// we want to modulate the value with the value that comes from other affectors - this means
		// that we need to somehow reset the alpha value each frame - like by using the 'LifetimeAffector' or
		// 'AlphaAffector'
		particle.m_color.m_vals[ Color.Alpha ] = Math.min( particle.m_color.m_vals[ Color.Alpha ], scale );

	}

	@Override
	public void load( DataLoader loader ) 
	{
		m_box = new BoundingBox();
		m_box.load( "localBounds", loader );
		
		float borderThickness = loader.getFloatValue( "borderFactor" );
		calculatePenetrationDepths( borderThickness );
	}
	
	/**
	 * The border thickness factor is specified in range [0..1], where 0 means
	 * that the particle should immediately disappear as soon as it enters the area,
	 * and 1 means that it will disappear completely only when it reaches the geometrical center
	 * of the area.
	 * 
	 * @param borderThickness
	 */
	private void calculatePenetrationDepths( float borderThickness )
	{
		m_maxVertPenetrationDepth = m_box.getHeight() * 0.5f * borderThickness;
		m_maxHorizPenetrationDepth = m_box.getWidth() * 0.5f * borderThickness;
	}

}
