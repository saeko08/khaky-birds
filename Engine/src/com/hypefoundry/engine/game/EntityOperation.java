package com.hypefoundry.engine.game;


/**
 * This is a visitor interface that allows to perform 
 * operations on the entities a world contains. 
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
