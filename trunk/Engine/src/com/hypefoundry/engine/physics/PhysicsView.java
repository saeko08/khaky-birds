/**
 * 
 */
package com.hypefoundry.engine.physics;

import java.util.*;

import android.util.Log;

import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.game.Updatable;
import com.hypefoundry.engine.world.World;
import com.hypefoundry.engine.world.WorldView;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.physics.events.OutOfWorldBounds;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.util.GenericFactory;
import com.hypefoundry.engine.util.SpatialGrid2D;

/**
 * This world view runs the physics simulation.
 * 
 * @author Paksas
 *
 */
public class PhysicsView extends GenericFactory< Entity, PhysicalBody > implements WorldView, Updatable
{
	private float								m_cellSize = 2.0f;
	private final int							MAX_ENTITIES = 512;		// TODO: config
	private float								m_worldWidth = 0;
	private float								m_worldHeight = 0;
	private SpatialGrid2D< PhysicalBody >		m_bodiesGrid = null;
	private List< PhysicalBody > 				m_bodies;
	private List< Entity >		 				m_bodiesToAdd;
	private List< Entity > 						m_bodiesToRemove;
	
	
	/**
	 * Constructor.
	 */
	public PhysicsView( float cellSize )
	{
		m_cellSize = cellSize;
		m_bodies = new ArrayList< PhysicalBody >( MAX_ENTITIES );
		m_bodiesToAdd = new ArrayList< Entity >();
		m_bodiesToRemove = new ArrayList< Entity >();
	}
	
	@Override
	public void update( float deltaTime )
	{	
		if ( m_bodiesGrid == null )
		{
			return;
		}
		
		manageBodies();
		
		// run physics simulation
		simulateForces( deltaTime );
		
		// update the positions of dynamic objects in the grid AFTER the simulation changed them
		m_bodiesGrid.update();
		
		// resolve collisions
		resolveCollisions();
	}
	
	@Override
	public void onAttached( World world ) 
	{
		m_worldWidth = world.getWidth();
		m_worldHeight = world.getHeight();
		m_bodiesGrid = new SpatialGrid2D< PhysicalBody >( m_worldWidth, m_worldHeight, m_cellSize );
	}

	@Override
	public void onDetached( World world ) 
	{
		m_bodiesGrid = null;
		m_worldWidth = 0;
		m_worldHeight = 0;
	}

	@Override
	public void onEntityAdded( Entity entity )
	{
		m_bodiesToRemove.remove( entity );
		m_bodiesToAdd.add( entity );
	}

	@Override
	public void onEntityRemoved( Entity entity )
	{
		m_bodiesToAdd.remove( entity );
		m_bodiesToRemove.add( entity );
	}
	
	/**
	 * Adds or removes bodies to the view - such intermediate addition/removal
	 * mechanism is needed, because the bodies may be added from a different
	 * thread than the one that's running all the methods that operate
	 * on the bodies.
	 */
	private void manageBodies()
	{
		int count = m_bodiesToRemove.size();
		for ( int i = 0; i < count; ++i )
		{
			detachBody( m_bodiesToRemove.get(i) );
		}
		m_bodiesToRemove.clear();
		
		count = m_bodiesToAdd.size();
		for ( int i = 0; i < count; ++i )
		{
			attachBody( m_bodiesToAdd.get(i) );
		}
		m_bodiesToAdd.clear();
	}
	
	/**
	 * Performs the actual addition of an entity and creation of a body.
	 * 
	 * @param entity
	 */
	private void attachBody( Entity entity )
	{
		PhysicalBody body = findBodyFor( entity );
		if ( body != null )
		{
			// the entity already has a body created
			return;
		}
		
		try
		{
			body = create( entity );
			
			// add the visual to the render list
			if ( entity.hasAspect( DynamicObject.class ) )
			{
				m_bodiesGrid.insertDynamicObject( body );
			}
			else
			{
				m_bodiesGrid.insertStaticObject( body );
			}
			m_bodies.add( body );
		}
		catch( IndexOutOfBoundsException e )
		{
			// ups... - no visual representation defined - notify about it
			Log.d( "PhysicsView", "Physical representation not defined for entity '" + entity.getClass().getName() + "'" );
		}
	}
	
	/**
	 * Performs the actual removal of an entity and associated body from the view.
	 * 
	 * @param entity
	 */
	private void detachBody( Entity entity )
	{
		PhysicalBody body = findBodyFor( entity );
		if ( body != null )
		{
			m_bodiesGrid.removeObject( body );
			m_bodies.remove( body );
		}
	}
	
	/**
	 * Looks for a registered body for the specified entity
	 * 
	 * @param entity
	 * @return
	 */
	private PhysicalBody findBodyFor( Entity entity )
	{
		int count = m_bodies.size();
		for ( int i = 0; i < count; ++i )
		{
			PhysicalBody body = m_bodies.get(i);
			if ( body.isBodyOf( entity ) )
			{
				return body;
			}
		}
		
		return null;
	}
	
	/**
	 * Resolve the collisions.
	 */
	private void resolveCollisions() 
	{
		// go through all the entities
		int count = m_bodies.size();
		for( int i = 0; i < count; ++i )
		{
			PhysicalBody body = m_bodies.get(i);

			// test the collisions with other nearby objects
			List< PhysicalBody > collidingBodies = m_bodiesGrid.getPotentialColliders( body );
			
			int collidersCount = collidingBodies.size();
			for ( int j = 0; j < collidersCount; ++j )
			{
				PhysicalBody collider = collidingBodies.get(j);
				
				if ( body.doesOverlap( collider ) )
				{
					body.onCollision( collider );
				}
			}
			
			// check if the entity didn't roam outside world's bounds
			BoundingBox bb = body.getBounds();
			if ( bb.m_minX <= 0.0f || bb.m_maxX >= m_worldWidth )
			{
				OutOfWorldBounds event = body.m_entity.sendEvent( OutOfWorldBounds.class );
				if ( event != null )
				{
					event.m_side = OutOfWorldBounds.ExitSide.ES_X;
				}
			}
			if ( bb.m_minY <= 0.0f || bb.m_maxY >= m_worldHeight )
			{
				OutOfWorldBounds event = body.m_entity.sendEvent( OutOfWorldBounds.class );
				if ( event != null )
				{
					event.m_side = OutOfWorldBounds.ExitSide.ES_Y;
				}
			}

		}
	}
	
	/**
	 * Simulates the forces that apply to particular physical bodies.
	 * 
	 * @param deltaTime
	 */
	private void simulateForces( float deltaTime )
	{
		int count = m_bodies.size();
		for ( int i = 0; i < count; ++i )
		{
			m_bodies.get(i).simulate( deltaTime );
		}
	}

}
