package com.hypefoundry.engine.renderer2D;

import java.util.*;

import com.hypefoundry.engine.core.Graphics;
import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.game.WorldView;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.EntityVisualFactory;
import com.hypefoundry.engine.util.GenericFactory;


/**
 * An operation that renders the world contents.
 * 
 * @author paksas
 *
 */
public class Renderer2D extends GenericFactory< Entity, EntityVisual > implements WorldView
{
	private Graphics 				m_graphics;
	private List< EntityVisual >	m_visuals;
	
	
	/**
	 * Constructor.
	 * 
	 * @param graphics			graphics subsystem
	 */
	public Renderer2D( Graphics graphics )
	{
		m_graphics = graphics;
		m_visuals = new ArrayList< EntityVisual >();
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
	
	@Override
	public void onEntityAdded( Entity entity ) 
	{
		EntityVisual visual = findVisualFor( entity );
		if ( visual != null )
		{
			// the entity already has a visual created
			return;
		}
		
		try
		{
			visual = create( entity );
		}
		catch( IndexOutOfBoundsException e )
		{
			// ups... - no visual representation defined - notify about it
			throw new RuntimeException( "Visual representation not defined for entity '" + entity.getClass().getName() + "'" );
		}
		
		// add the visual to the render list
		m_visuals.add( visual );
		Collections.sort( m_visuals, new Comparator< EntityVisual >() {

			@Override
			public int compare( EntityVisual object1, EntityVisual object2) 
			{
				float val = object2.getZ() - object1.getZ();
				return ( val < 1e-3 ) ? -1 : ( ( val > 1e-3 ) ? 1 : 0 ); 
			}
		} );
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
