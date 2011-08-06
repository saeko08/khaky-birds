package com.hypefoundry.engine.world;


/**
 * This is a visitor interface that allows to perform 
 * operations on the entities a world contains. 
 * 
 * A good example of such an operation would be a rendering
 * query that renders all entities present in the world.
 * 
 * @author paksas
 *
 */
public interface EntityOperation
{
	/**
	 * This is where you put the operation that will alter entity's state.
	 * 
	 * @param entity
	 */
	void visit( Entity entity );
}
