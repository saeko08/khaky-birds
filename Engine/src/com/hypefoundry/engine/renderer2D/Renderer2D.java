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
import com.hypefoundry.engine.util.GenericFactory;
import com.hypefoundry.engine.util.SpatialGrid2D;
import com.hypefoundry.engine.util.Arrays;

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
	private final short							MAX_ENTITIES = 512;			// TODO: config
	private final float							PIXELS_TO_METERS = 0.01f;	// TODO: config								
	private float 								VIEWPORT_WIDTH;
	private float 								VIEWPORT_HEIGHT;
	
	private GLGraphics 							m_graphics;
	private List< Entity >						m_entitiesToAdd;
	private List< Entity >						m_entitiesToRemove;
	private SpatialGrid2D						m_visualsGrid;
	private List< EntityVisual >				m_visuals;
	private SpriteBatcher						m_batcher = null;
	private boolean								m_additiveMode = false;
	
	private EntityVisual[]						m_queryResult = new EntityVisual[MAX_ENTITIES];
	
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
	 * @param desiredViewportWidth
	 * @param desiredViewportHeight
	 */
	public Renderer2D( Game game, int desiredViewportWidth, int desiredViewportHeight )
	{
		VIEWPORT_WIDTH = (float)desiredViewportWidth * PIXELS_TO_METERS;
		VIEWPORT_HEIGHT = (float)desiredViewportHeight * PIXELS_TO_METERS;
		m_graphics = game.getGraphics();
		
		m_entitiesToAdd = new ArrayList< Entity >();
		m_entitiesToRemove = new ArrayList< Entity >();
		
		m_batcher = new SpriteBatcher( m_graphics, MAX_SPRITES );
		m_camera = new Camera2D( m_graphics, desiredViewportWidth, desiredViewportHeight, PIXELS_TO_METERS );
		m_visuals = new ArrayList< EntityVisual >( MAX_ENTITIES );
	}
	
	@Override
	public void onAttached( World world )
	{
		float cellSize = ( VIEWPORT_HEIGHT < VIEWPORT_WIDTH ) ? VIEWPORT_HEIGHT : VIEWPORT_WIDTH; 
		m_visualsGrid = new SpatialGrid2D( world.getWidth(), world.getHeight(), cellSize, MAX_ENTITIES );
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
	 * 
	 * @param deltaTime
	 */
	public void draw( float deltaTime )
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
		
		GL10 gl = m_graphics.getGL();
		
		// clear the buffers
		if ( m_additiveMode )
		{
			gl.glClear( GL10.GL_STENCIL_BUFFER_BIT );		
		}
		else
		{
			gl.glClear( GL10.GL_COLOR_BUFFER_BIT | GL10.GL_STENCIL_BUFFER_BIT );
		}
		
		// reset the texture matrix
		gl.glMatrixMode( GL10.GL_TEXTURE );
		gl.glLoadIdentity();

		// set the render state
		m_camera.setViewportAndMatrices();
		gl.glDisable( GL10.GL_DEPTH_TEST );
		
		// draw the visuals, sorting them first in their Z order		
		int count = m_visualsGrid.getPotentialColliders( m_camera.getFrustum(), m_queryResult );
		Arrays.quickSort( m_queryResult, count, m_comparator );
		
		for ( int i = 0; i < count; ++i )
		{
			m_queryResult[i].draw( m_batcher, m_camera, deltaTime );
		}
		
		// flush the batcher
		m_batcher.flush();
	}
	
	/**
	 * Enables/disables the mode in which the renderer will not clean
	 * the color buffer.
	 * 
	 * Ideal for situations where we want to have two cooperating renderers - one
	 * draws the background, the other one draws on top of that.
	 * 
	 * Stencil buffer will be cleaned though.
	 * 
	 * @param enable
	 * @return
	 */
	public void setAdditiveMode( boolean enable )
	{
		m_additiveMode = enable;
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
			visual.onRemoved();
			
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
