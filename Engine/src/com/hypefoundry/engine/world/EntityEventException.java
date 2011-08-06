/**
 * 
 */
package com.hypefoundry.engine.world;

/**
 * Exception transmitted when an event can't be sent to an entity.
 * @author Paksas
 *
 */
public class EntityEventException extends RuntimeException 
{

	/**
	 * Constructor.
	 * 
	 * @param reason
	 */
	public EntityEventException( String reason ) 
	{
		super( reason );
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
