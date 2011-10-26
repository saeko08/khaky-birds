/**
 * 
 */
package com.hypefoundry.engine.renderer2D.animation.frameDeserializers;

import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.renderer2D.animation.Animation;
import com.hypefoundry.engine.renderer2D.animation.FrameDeserializer;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * Deserializes frames using linear interpolationbetween two texture regions.
 * 
 * @author Paksas
 */
public class LinearInterpolation extends FrameDeserializer 
{
	static String[] 	m_attribs	= { "x", "y", "w", "h" };
	
	private float[]		m_from		= new float[ m_attribs.length ];
	private float[]		m_to		= new float[ m_attribs.length ];
	private float[]		m_step		= new float[ m_attribs.length ];
	private float[]		m_region	= new float[ m_attribs.length ];
	
	
	@Override
	public void deserialize( DataLoader loader, Animation animation ) 
	{
		int framesCount = getFramesCount( loader, animation );
		
		DataLoader fromNode = loader.getChild( "From" );
		DataLoader toNode = loader.getChild( "To" );
		if ( fromNode == null || toNode == null )
		{
			// one of the required nodes is not defined
			throw new RuntimeException( "Required LinearInterpolation nodes 'From' and 'To' are not defined" );
		}

		// read the texture regions specifications & calculate the interpolation steps 
		for ( int i = 0; i < m_attribs.length; ++i )
		{
			m_from[i] = fromNode.getFloatValue( m_attribs[i] );
			m_to[i] = toNode.getFloatValue( m_attribs[i] );

			m_step[i] = ( m_to[i] - m_from[i] ) / (float)framesCount;
		}

		// create frames
		for ( int i = 0; i < m_attribs.length; ++i )
		{
			m_region[i] = m_from[i];
		}
		for ( int i = 0; i < framesCount; ++i )
		{
			TextureRegion region = new TextureRegion( animation.m_renderState, m_region[0], m_region[1], m_region[2], m_region[3] );
			animation.appendFrame( region );
			
			// update the region
			for ( int j = 0; j < m_attribs.length; ++j )
			{
				m_region[j] += m_step[j];
			}
		}
	}

	@Override
	public int getFramesCount( DataLoader loader, Animation animation ) 
	{
		float duration = loader.getFloatValue( "duration" );
		float frameDuration = animation.getFrameDuration();
		
		return (int)( frameDuration > 0 ? ( duration / frameDuration ) : 0 ) + 1;
	}

}
