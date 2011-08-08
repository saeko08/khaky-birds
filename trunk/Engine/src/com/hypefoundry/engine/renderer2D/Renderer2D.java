package com.hypefoundry.engine.renderer2D;

import java.util.*;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.hypefoundry.engine.core.GLGraphics;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.game.Game;
import com.hypefoundry.engine.world.World;
import com.hypefoundry.engine.world.WorldView;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.renderer2D.impl.GLCamera2D;
import com.hypefoundry.engine.util.GenericFactory;
import com.hypefoundry.engine.util.SpatialGrid2D;

// TODO sorting by distance from the screen



/**
 * An operation that renders the world contents.
 * 
 * @author paksas
 *
 */
public class Renderer2D extends GenericFactory< Entity, EntityVisual > implements WorldView
{
	private final int							MAX_SPRITES = 512;			// TODO: config
	private final int							MAX_ENTITIES = 512;			// TODO: config
	private final float 						VIEWPORT_WIDTH = 9.6f;		// TODO: config
	private final float 						VIEWPORT_HEIGHT = 4.8f;		// TODO: config
	
	private GLGraphics 							m_graphics;
	private List< Entity >						m_entitiesToAdd;
	private List< Entity >						m_entitiesToRemove;
	private SpatialGrid2D< EntityVisual >		m_visualsGrid;
	private List< EntityVisual >				m_visuals;
	private SpriteBatcher						m_batcher = null;
	
	private Camera2D							m_camera = null;
	
	private Comparator< EntityVisual >			m_comparator = new Comparator< EntityVisual >()
			{
				@Override
				public int compare( EntityVisual arg0, EntityVisual arg1 ) 
				{
					return (int) (arg1.getZ() - arg0.getZ());
				}
		
			};
	
	/**
	 * Constructor.
	 * 
	 * @param game
	 */
	public Renderer2D( Game game )
	{
		m_graphics = game.getGraphics();
		
		m_entitiesToAdd = new ArrayList< Entity >();
		m_entitiesToRemove = new ArrayList< Entity >();
		
		m_batcher = new SpriteBatcher( m_graphics, MAX_SPRITES );
		
		m_camera = new GLCamera2D( m_graphics, VIEWPORT_WIDTH, VIEWPORT_HEIGHT );
		
		m_visuals = new ArrayList< EntityVisual >( MAX_ENTITIES );
	}
	
	@Override
	public void onAttached( World world )
	{
		float cellSize = ( VIEWPORT_HEIGHT < VIEWPORT_WIDTH ) ? VIEWPORT_HEIGHT : VIEWPORT_WIDTH; 
		m_visualsGrid = new SpatialGrid2D< EntityVisual >( world.getWidth(), world.getHeight(), cellSize );
	}
	
	@Override
	public void onDetached( World world )
	{
		m_visualsGrid = null;
	}
	
	/**
	 * Returns the active camera.
	 * 
	 * @return
	 */
	final public Camera2D getCamera()
	{
		return m_camera;
	}
	
	/**
	 * Draws the contents of the view.
	 */
	public void draw()
	{
		if ( m_camera == null || m_visualsGrid == null )
		{
			// nothing to do for us here
			return;
		}
		
		// manage the incoming and outgoing entities
		manageEntities();
		
		// update the grid
		m_visualsGrid.update();
		
		// set the render state
		GL10 gl = m_graphics.getGL();
		gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
		m_camera.setViewportAndMatrices();
		
		gl.glEnable( GL10.GL_BLEND );
		gl.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
		
		gl.glEnable( GL10.GL_TEXTURE_2D );
		gl.glDisable( GL10.GL_DEPTH_TEST );
		
		// draw the visuals, sorting them first in their Z order
		List< EntityVisual > visuals = m_visualsGrid.getPotentialColliders( m_camera.getFrustum() );
		
		// TODO: napisac wlasne in-place, bo to za duzo pamieci zre: 
		// Collections.sort( visuals, m_comparator );
		
		int count = visuals.size();
		for ( int i = 0; i < count; ++i )
		{
			visuals.get(i).draw( m_batcher );
		}
		m_batcher.flush();
	}
	
	
	@Override
	public void onEntityAdded( Entity entity )
	{
		m_entitiesToRemove.remove( entity );
		m_entitiesToAdd.add( entity );
	}

	@Override
	public void onEntityRemoved( Entity entity ) 
	{
		m_entitiesToAdd.remove( entity );
		m_entitiesToRemove.add( entity );
	}
	
	/**
	 * Manages the addition and removal of the entities.
	 */
	private void manageEntities()
	{
		int count = m_entitiesToRemove.size();
		for ( int i = 0; i < count; ++i )
		{
			detachEntity( m_entitiesToRemove.get(i) );
		}
		m_entitiesToRemove.clear();
		
		count = m_entitiesToAdd.size();
		for ( int i = 0; i < count; ++i )
		{
			attachEntity( m_entitiesToAdd.get(i) );
		}
		m_entitiesToAdd.clear();
	}
	
	/**
	 * Actual addition of the entity and its representation to the view.
	 * 
	 * @param entity
	 */
	private void attachEntity( Entity entity )
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
			
			// add the visual to the render list
			if ( entity.hasAspect( DynamicObject.class ) )
			{
				m_visualsGrid.insertDynamicObject( visual );
			}
			else
			{
				m_visualsGrid.insertStaticObject( visual );
			}
			m_visuals.add( visual );
		}
		catch( IndexOutOfBoundsException e )
		{
			// ups... - no visual representation defined - notify about it
			Log.d( "Renderer2D", "Visual representation not defined for entity '" + entity.getClass().getName() + "'" );
		}
	}
	
	/**
	 * Actual removal of the entity and its representation from the view.
	 * 
	 * @param entity
	 */
	private void detachEntity( Entity entity )
	{
		EntityVisual visual = findVisualFor( entity );
		if ( visual != null )
		{
			m_visualsGrid.removeObject( visual );
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
		int count = m_visuals.size();
		for ( int i = 0; i < count; ++i )
		{
			EntityVisual visual = m_visuals.get(i);
			if ( visual.isVisualOf( entity ) )
			{
				return visual;
			}
		}
		
		return null;
	}

}
