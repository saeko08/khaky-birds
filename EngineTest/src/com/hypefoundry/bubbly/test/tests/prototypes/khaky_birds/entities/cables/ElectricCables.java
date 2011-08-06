package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.cables;

import java.util.*;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird.CableProvider;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.World;
import com.hypefoundry.engine.math.BoundingBox;


/**
 * Electric cables the bird jumps on.
 * 
 * @author paksas
 *
 */
public class ElectricCables extends Entity implements CableProvider
{
	
	private float  m_cablePositions[] = new float[0];

	/**
	 * Constructor.
	 */
	public ElectricCables()
	{
		float halfWidth = 4.8f; // TODO: config
		float halfHeight = 4.8f; // TODO: config
		
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
		float cablePositions[] = new float[ m_cablePositions.length + 1 ];
		for( int i = 0; i < m_cablePositions.length; ++i )
		{
			cablePositions[i] = m_cablePositions[i];
		}
		
		cablePositions[ m_cablePositions.length ] = x;
		m_cablePositions = cablePositions;
		
		Arrays.sort(m_cablePositions );
	}
	
	@Override
	public int getStartCableIdx() 
	{
		return 0;
	}

	@Override
	public float getPositionOnCable( int cableIdx, float y ) 
	{
		return m_cablePositions[ cableIdx ];
	}

	@Override
	public int getLeftCable( int cableIdx ) 
	{
		int newIdx = cableIdx - 1;
		if ( newIdx <= 0 )
		{
			newIdx = 0;
		}
		return newIdx;	
	}
	
	@Override
	public int getRightCable(int cableIdx) 
	{
		int newIdx = cableIdx + 1;
		if ( newIdx >= ( m_cablePositions.length - 1 ) )
		{
			newIdx = m_cablePositions.length - 1;
		}
		return newIdx;	
	}
	
}
