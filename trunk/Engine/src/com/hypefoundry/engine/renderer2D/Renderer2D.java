package com.hypefoundry.engine.renderer2D;

import java.util.*;

import com.hypefoundry.engine.core.Graphics;
import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.game.WorldView;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.EntityVisualFactory;


/**
 * An operation that renders the world contents.
 * 
 * @author paksas
 *
 */
public class Renderer2D implements WorldView
{
	private Graphics 				m_graphics;
	private List< EntityVisual >	m_visuals;
	
	private static class VisualAssociation 
	{
		Class						m_entityType;
		EntityVisualFactory			m_visualFactory;
		
		VisualAssociation( Class entityType, EntityVisualFactory visualFactory )
		{
			m_entityType = entityType;
			m_visualFactory = visualFactory;
		}
		
	}
	private List< VisualAssociation > 		m_associations;
	
	
	/**
	 * Constructor.
	 * 
	 * @param graphics			graphics subsystem
	 */
	public Renderer2D( Graphics graphics )
	{
		m_graphics = graphics;
		m_visuals = new ArrayList< EntityVisual >();
		m_associations = new ArrayList< VisualAssociation >();
	}
	
	/**
	 * Draws the contents of the view.
	 */
	public void draw()
	{
		// draw the visuals
		for ( EntityVisual visual : m_visuals )
		{
			visual.draw( m_graphics );
		}
	}
	
	/**
	 * Registers the class of a visual that should be created
	 * when an entity of the specified type is added.
	 * 
	 * @param entityType
	 * @param visualType
	 */
	public void registerVisual( Class entityType, EntityVisualFactory visualFactory )
	{
		for ( VisualAssociation association : m_associations )
		{
			if ( association.m_entityType.equals( entityType ) )
			{
				// a definition already exists - change it
				association.m_visualFactory = visualFactory;
				return;
			}
		}
		
		// if we got so far, it means there definition wasn't found - so create one
		m_associations.add( new VisualAssociation( entityType, visualFactory ) );
	}

	@Override
	public void onEntityAdded( Entity entity ) 
	{
		EntityVisual visual = findVisualFor( entity );
		if ( visual != null )
		{
			// the entity already has a visual created
			return;
		}
		
		// locate a visual class for the entity
		Class entityType = entity.getClass();
		for ( VisualAssociation association : m_associations )
		{
			if ( association.m_entityType.equals( entityType ) )
			{
				// create the visual
				visual = association.m_visualFactory.instantiate( entity );
				m_visuals.add( visual );
				return;
			}
		}

		// ups... - no visual representation defined - notify about it
		throw new RuntimeException( "Visual representation not defined for entity '" + entityType.getName() + "'" );
	}

	@Override
	public void onEntityRemoved( Entity entity ) 
	{
		EntityVisual visual = findVisualFor( entity );
		if ( visual != null )
		{
			m_visuals.remove( visual );
		}
	}
	
	/**
	 * Looks for a registered visual for the specified entity
	 * 
	 * @param entity
	 * @return
	 */
	private EntityVisual findVisualFor( Entity entity )
	{
		for ( EntityVisual visual : m_visuals )
		{
			if ( visual.isVisualOf( entity ) )
			{
				return visual;
			}
		}
		
		return null;
	}

}
