package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.cables.ElectricCables;
import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.game.World;


/**
 * The game's main character. A nasty bird that jumps on the cables
 * trying to avoid electrocution and make the lives of the pedestrians
 * as miserable as possible.
 * 
 * @author paksas
 *
 */
public class Bird extends Entity 
{
	private ElectricCables		m_cables	= null;
	
	/**
	 * Constructor.
	 */
	public Bird()
	{
		setPosition( 0, 0, 0 );
	}
	
	@Override
	public void onAddedToWorld( World hostWorld )
	{
		m_cables = (ElectricCables)hostWorld.findEntity( ElectricCables.class );
	}
	
	@Override
	public void onRemovedFromWorld( World hostWorld ) 
	{
		m_cables = null;
	}
	
	@Override
	public void onCollision( Entity colider ) 
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Moves the bird to the next cable to its left. 
	 */
	public void jumpToLeftCable() 
	{
		if ( m_cables == null )
		{
			return;
		}
	}

	/**
	 * Moves the bird to the next cable to its right. 
	 */
	public void jumpToRightCable() 
	{
		if ( m_cables == null )
		{
			return;
		}
		
	}

	/**
	 * Moves the bird down the cable it's sitting on. 
	 */
	public void moveDownTheCable() 
	{
		if ( m_cables == null )
		{
			return;
		}
		
	}

	/**
	 * Moves the bird up the cable it's sitting on. 
	 */
	public void moveUpTheCable() 
	{
		if ( m_cables == null )
		{
			return;
		}
		
	}

}
