/**
 * 
 */
package com.hypefoundry.kabloons.entities.toggle;

import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityOperation;
import com.hypefoundry.engine.world.World;

/**
 * @author Paksas
 *
 */
public class Toggle extends Entity 
{
	String				m_controlledEntityTag;
	Toggled 			m_controlledEntity;
	String 				m_onAnimPath;
	String 				m_offAnimPath;
	
	
	@Override
	public void onLoad( DataLoader loader )
	{
		m_controlledEntityTag = loader.getStringValue( "entityTag" );
			
		m_onAnimPath = loader.getStringValue( "onAnim" );
		m_offAnimPath = loader.getStringValue( "offAnim" );
	}
	
	@Override
	public void onAddedToWorld( World hostWorld )
	{
		// look for the entity this toggle operates
		hostWorld.executeOperation( new EntityOperation() {

			@Override
			public void visit( Entity entity ) 
			{
				if ( entity instanceof Toggled == false )
				{
					return;
				}
				
				Toggled toggledEntity = (Toggled)entity;
				if ( toggledEntity.getTag().equals( m_controlledEntityTag ) )
				{
					m_controlledEntity = toggledEntity;
				}
			}
		
		} );
	}
}
