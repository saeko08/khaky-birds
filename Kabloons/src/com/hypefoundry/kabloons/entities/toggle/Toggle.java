/**
 * 
 */
package com.hypefoundry.kabloons.entities.toggle;

import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityOperation;
import com.hypefoundry.engine.world.World;
import com.hypefoundry.kabloons.utils.AssetsFactory;

/**
 * @author Paksas
 *
 */
public class Toggle extends Entity 
{
	String				m_controlledEntityTag;
	Toggled 			m_controlledEntity;
	public String 		m_onTexturePath;
	public String 		m_offTexturePath;
	
	
	/**
	 * Constructor.
	 * 
	 * @param assetsFactory
	 */
	public Toggle( AssetsFactory assetsFactory )
	{
		assetsFactory.initializeToggle( this );		
	}
	
	@Override
	public void onLoad( DataLoader loader )
	{
		m_controlledEntityTag = loader.getStringValue( "entityTag" );
		
		// adjust position
		getPosition().m_z = 70.0f;
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
