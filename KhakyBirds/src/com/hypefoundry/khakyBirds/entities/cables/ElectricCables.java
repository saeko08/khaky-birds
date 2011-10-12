package com.hypefoundry.khakyBirds.entities.cables;


import com.hypefoundry.khakyBirds.entities.bird.CableProvider;
import com.hypefoundry.engine.renderer2D.Spline;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;


/**
 * Electric cables the bird jumps on.
 * 
 * @author paksas
 *
 */
public class ElectricCables extends Entity implements CableProvider
{
	private float		m_modelWidth 			= 4.8f;  // TODO: config
	private float		m_modelHeight 			= 9.6f;  // TODO: config
	
	Spline				m_wires[] 				= new Spline[0];

	/**
	 * Constructor.
	 */
	public ElectricCables()
	{
		float halfWidth = m_modelWidth / 2.0f;
		float halfHeight = m_modelHeight / 2.0f;
		
		setPosition( halfWidth, halfHeight, 10 );
		setBoundingBox( new BoundingBox( -halfWidth, -halfHeight, 0, halfWidth, halfHeight, 0 ) );
	}
	
	/**
	 * Adds a new cable and specifies its X offset.
	 * 
	 * @param x
	 */
	public void addCable( float x )
	{
		// resize the array
		Spline wires[] = new Spline[ m_wires.length + 1 ];
		for( int i = 0; i < m_wires.length; ++i )
		{
			wires[i] = m_wires[i];
		}
		m_wires = wires;
		
		// create a new cable at the specified position
		Spline newWire = new Spline();
		newWire.addPoint( new Vector3( x, 0.0f, 10 ) );
		newWire.addPoint( new Vector3( x, m_modelHeight, 10 ) );
		
		// and append it to the array
		wires[ m_wires.length - 1 ] = newWire;
	}
	
	private Vector3 tmpCableQueryPos = new Vector3();
	@Override
	public int getNearestCableIdx( Vector3 position )
	{
		float smallestDist = 100000;
		int nearestCableIdx = -1;
		
		for ( int i = 0; i < m_wires.length; ++i )
		{
			float dist = m_wires[i].getNearestPosition( position, tmpCableQueryPos );
			
			if ( dist < smallestDist )
			{
				smallestDist = dist;
				nearestCableIdx = i;
			}
		}
		
		return nearestCableIdx;
	}

	@Override
	public void getPositionOnCable( int cableIdx, Vector3 currPos, Vector3 outPos )
	{
		m_wires[cableIdx].getNearestPosition( currPos, outPos );
	}

	// ------------------------------------------------------------------------
	// Serialization support
	// ------------------------------------------------------------------------
	
	@Override
	public void onLoad( DataLoader loader ) 
	{
		float pixelHeight = loader.getFloatValue("textureWidth");
		for ( DataLoader child = loader.getChild( "Cable" ); child != null; child = child.getSibling() )
		{
		   int pixelPos = child.getIntValue( "pos" );
		   float modelPos = pixelPos * m_modelWidth / pixelHeight;
		   addCable( modelPos );
		}
	}
	
}
