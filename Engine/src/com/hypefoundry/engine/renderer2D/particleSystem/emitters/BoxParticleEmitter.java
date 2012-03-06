/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.emitters;


import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleEmitter;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * Emits the particles inside the specified box-shaped area.
 * 
 * @author Paksas
 */
public class BoxParticleEmitter  extends ParticleEmitter 
{
	private BoundingBox 	m_box 						= new BoundingBox();
	private boolean			m_uniformDistribution 		= false;
	private Vector3 		m_direction					= new Vector3();
	private float			m_nominalForce				= 0.0f;
	
	private int				m_numParticlesPerRow		= 0;
	private float			m_horizDistribution			= 0.0f;
	private float			m_vertDistribution			= 0.0f;
	
	@Override
	protected void initialize( int particleIdx, Particle particle ) 
	{
		if ( m_uniformDistribution )
		{
			int colIdx = particleIdx / m_numParticlesPerRow;
			int rowIdx = particleIdx - colIdx * m_numParticlesPerRow;
			
			particle.m_position.m_x = m_box.m_minX + ( (float)rowIdx + (float)Math.random() * 0.4f ) * m_horizDistribution;
			particle.m_position.m_y = m_box.m_minY + ( (float)colIdx + (float)Math.random() * 0.4f )  * m_vertDistribution;
		}
		else
		{
			// randomize particle's position
			particle.m_position.m_x = m_box.m_minX + (float)Math.random() * m_box.getWidth();
			particle.m_position.m_y = m_box.m_minY + (float)Math.random() * m_box.getHeight();
		}
		
		float randForceFactor = 0.5f + (float)Math.random() * 0.5f;
		particle.m_velocity.set( m_direction ).scale( m_nominalForce * randForceFactor);
	}

	@Override
	protected void onLoad( DataLoader loader ) 
	{
		m_box.load( "localBounds", loader );
		
		m_direction.load( "direction", loader );
		m_direction.normalize2D();
		m_nominalForce = loader.getFloatValue( "force" );
		
		m_uniformDistribution = loader.getStringValue( "uniformDistribution" ).equalsIgnoreCase( "true" );
		if ( m_uniformDistribution )
		{
			// we want the whole are uniformly covered
			m_horizDistribution = loader.getFloatValue( "horizDistribution" );
			m_vertDistribution = loader.getFloatValue( "vertDistribution" );
			
			m_numParticlesPerRow = (int)( m_box.getWidth() / m_horizDistribution ) + 1;
			int numPerCol = (int)( m_box.getHeight() / m_vertDistribution ) + 1;
			m_particlesCount = m_numParticlesPerRow * numPerCol;
			
			setEmissionFrequency( 0.1f );
			setAmountEmittedEachTick( m_particlesCount );
		}
	}
}
