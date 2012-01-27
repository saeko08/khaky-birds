/**
 * 
 */
package com.hypefoundry.khakyBirds.campaign;

import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;


/**
 * Level 01 script.
 * 
 * @author Paksas
 */
public class Level01 extends Entity
{	
	// movement tutorial
	float 			m_minMovementTutorialDistance;
	float 			m_movementTutorialTimer;
	
	// crapping tutorial
	float			m_crappingTutorialTimer;
	
	
	@Override
	public void onLoad( DataLoader loader ) 
	{
		DataLoader node;
			
		if ( ( node = loader.getChild( "MovementTutorial" ) ) != null )
		{
			m_movementTutorialTimer = node.getFloatValue( "pokeTimer" );
			m_minMovementTutorialDistance = node.getFloatValue( "minDist" );
		}
		
		if ( ( node = loader.getChild( "CrappingTutorial" ) ) != null )
		{
			m_crappingTutorialTimer = loader.getFloatValue( "pokeTimer" );
		}
	}
}

